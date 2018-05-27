package com.perapoch.concurrency.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageDispatcher extends Thread {

    private final Map<Actor, Boolean> runningActors;
    private final Set<ActorAddress> crashedActors;
    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final AtomicInteger pendingMessages;

    public MessageDispatcher(int numThreads) {
        super("MessageDispatcher");
        this.runningActors = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.pendingMessages = new AtomicInteger(0);
        this.crashedActors = new CopyOnWriteArraySet<>();
    }

    void newMessage(final Actor actor, final Message message) {
        runningActors.putIfAbsent(actor, false);

        if (crashedActors.contains(actor)) {
            System.out.println("Trying to send a msg to a dead actor");
        }

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

                final List<Actor> actorsToRun = new ArrayList<>();
                runningActors.forEach((actor, consuming) -> {
                    if (!crashedActors.contains(actor.self()) && !consuming && actor.hasPendingMessages()) {
                        actorsToRun.add(actor);
                    }
                });
                actorsToRun.forEach(actor -> {

                    runningActors.put(actor, true);
                    final Message msg = actor.getNextMessage();

                    executorService.submit(() -> {
                        try {
                            actor.onReceive(msg);
                            pendingMessages.decrementAndGet();
                            runningActors.put(actor, false);
                        } catch (Exception ex) {
                            crashedActors.add(actor.getAddress());
                            runningActors.remove(actor);
                            actor.getParentAddress().tell(new FailedMessage("FailedMessage", msg, actor.getAddress()));
                        }
                    });
                });

            }
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    public void markAsHealthy(ActorAddress restartedActor) {
        crashedActors.remove(restartedActor);
        if (pendingMessages.get() > 0) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                notFull.signal();
            } finally {
                lock.unlock();
            }
        }
    }

}
