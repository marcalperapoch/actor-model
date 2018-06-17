package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageDispatcher extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);

    private final ActorRegistry registry;
    private final Map<Actor, Boolean> runningActors;
    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final AtomicInteger pendingMessages;

    MessageDispatcher(ActorRegistry registry, int numThreads) {
        super("MessageDispatcher");
        this.registry = registry;
        this.runningActors = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.pendingMessages = new AtomicInteger(0);
    }

    void sendMessage(final ActorRef to, final Message message) {
        sendMessage(registry.getActorByActorRef(to), message);
    }

    void sendMessage(final Actor actor, final Message message) {

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            actor.enqueueMessage(message);
            pendingMessages.incrementAndGet();
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }

    void onNewActor(final Actor actor) {
        registry.registerActor(actor);
        runningActors.putIfAbsent(actor, false);
        LOGGER.info("*** New actor \"{}\". Total actors = {}", actor.getAddress(), runningActors.size());
    }

    @Override
    public void run() {
        try {
            while (true) {

                final ReentrantLock lock = this.lock;
                lock.lockInterruptibly();
                try {
                    while (pendingMessages.get() == 0) {
                        notFull.await();
                    }
                } finally {
                    lock.unlock();
                }

                runningActors.forEach((actor, consuming) -> {

                    if (!consuming && actor.hasPendingMessages()) {
                        final Message message = actor.getNextMessage();
                        if (message != null) {
                            runningActors.put(actor, true);
                            executorService.submit(() -> {
                                try {
                                    actor.onReceive(message);
                                    pendingMessages.decrementAndGet();
                                    runningActors.put(actor, false);
                                } catch (Exception ex) {
                                    LOGGER.error("Couldn't deliver {} to {} ({})", message, actor, ex.getMessage());
                                    runningActors.remove(actor);
                                    final ActorAddress address = actor.getAddress();
                                    address.getParentAddress().ifPresent(parentAddress -> {
                                        final Actor parentActor = registry.getActorByAddress(parentAddress);
                                        parentActor.getActorRef()
                                                .tell(new FailedMessage(message, actor.getActorRef()), message.getFrom());
                                    });
                                }
                            });
                        }
                    }

                });

            }
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

}
