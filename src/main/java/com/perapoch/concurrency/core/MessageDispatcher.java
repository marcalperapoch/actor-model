package com.perapoch.concurrency.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageDispatcher extends Thread {

    private final Map<Actor, Boolean> actors;
    private final Map<Actor, Mailbox> mailboxes;
    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final AtomicInteger pendingMessages;

    public MessageDispatcher(int numThreads) {
        super("MessageDispatcher");
        this.actors = new ConcurrentHashMap<>();
        this.mailboxes = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.pendingMessages = new AtomicInteger(0);
    }

    void addActor(Actor actor) {
        mailboxes.put(actor, new Mailbox());
        actors.put(actor, false);
    }

    void newMessage(final Actor actor, final Message message) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            mailboxes.get(actor).receive(message);
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
                actors.forEach((actor, consuming) -> {
                    if (!consuming && hasPendingMessages(actor)) {
                        actorsToRun.add(actor);
                    }
                });
                actorsToRun.forEach(actor -> {

                    actors.put(actor, true);
                    final Message msg = mailboxes.get(actor).getNextMessage();

                    executorService.submit(() -> {
                        actor.onReceive(msg);
                        pendingMessages.decrementAndGet();
                        actors.put(actor, false);
                    });
                });

            }
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    private boolean hasPendingMessages(Actor actor) {
        return mailboxes.get(actor).hasPendingMessages();
    }
}
