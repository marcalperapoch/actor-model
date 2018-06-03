package com.perapoch.concurrency.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashMap;
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

    public MessageDispatcher(ActorRegistry registry, int numThreads) {
        super("MessageDispatcher");
        this.registry = registry;
        this.runningActors = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.pendingMessages = new AtomicInteger(0);
    }

    void newMessage(final Actor actor, final Message message) {

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
        runningActors.putIfAbsent(actor, false);
        LOGGER.info("*** New actor \"{}\". Total actors = {}", actor.getPath(), runningActors.size());
    }

    @Override
    public void run() {
        try {
            while (true) {

                final Map<Actor, Message> actorsToRun = new HashMap<>();

                final ReentrantLock lock = this.lock;
                lock.lockInterruptibly();
                try {
                    while (pendingMessages.get() == 0) {
                        notFull.await();
                    }
                    runningActors.forEach((actor, consuming) -> {
                        if (!consuming && actor.hasPendingMessages()) {
                            runningActors.put(actor, true);
                            actorsToRun.put(actor, actor.getNextMessage());
                        }
                    });
                } finally {
                    lock.unlock();
                }

                actorsToRun.forEach((actor, msg) -> {

                    executorService.submit(() -> {
                        try {
                            actor.onReceive(msg);
                            pendingMessages.decrementAndGet();
                            runningActors.put(actor, false);
                        } catch (Exception ex) {
                            LOGGER.error("Process message error", ex);
                            runningActors.remove(actor);
                            final Path path = actor.getPath();
                            final Path parentPath = path.getParent();
                            if (parentPath != null && !"/".equals(parentPath)) { // not the root actor
                                final Actor parentActor = registry.getActorByPath(parentPath);
                                parentActor.getAddress().tell(new FailedMessage("Not delivered message", msg, actor.getAddress()));
                            }
                        }
                    });

                });

            }
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

}
