package com.perapoch.concurrency.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageDispatcher extends Thread {

    private final List<Actor> actors;
    private final ExecutorService executorService;

    public MessageDispatcher(int numThreads) {
        this.actors = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    @Override
    public void run() {
        while (true) {
            actors.forEach(actor -> {
                if (actor.hasPendingMessages()) {
                    executorService.submit(actor::consumeMessage);
                }
            });
        }
    }
}
