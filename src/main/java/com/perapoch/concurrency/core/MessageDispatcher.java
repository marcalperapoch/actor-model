package com.perapoch.concurrency.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageDispatcher extends Thread {

    private final Map<Actor, Boolean> actors;
    private final ExecutorService executorService;

    public MessageDispatcher(int numThreads) {
        this.actors = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void addActor(Actor actor) {
        actors.put(actor, false);
    }

    @Override
    public void run() {
        while (true) {
            final List<Actor> actorsToRun = new ArrayList<>();
            actors.forEach((actor, consuming) -> {
                if (!consuming && actor.hasPendingMessages()) {
                    actorsToRun.add(actor);
                }
            });
            actorsToRun.forEach(actor -> {
                actors.put(actor, true);
                executorService.submit(() -> {
                    actor.consumeMessage();
                    actors.put(actor, false);
                });
            });
        }
    }

}
