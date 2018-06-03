package com.perapoch.concurrency.core;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActorRegistryImpl implements ActorRegistry {

    private final Map<Path, Actor> registry;

    public ActorRegistryImpl() {
        this.registry = new ConcurrentHashMap<>();
    }

    @Override
    public Actor getActorByPath(Path path) {
        return registry.get(path);
    }

    @Override
    public void registerActor(final Actor actor) {

        registry.compute(actor.getPath(), (path, oldActor) -> {
            if (oldActor == null) {
                return actor;
            } else {
                actor.copyMessagesFrom(oldActor);
                return actor;
            }
        });
    }


}
