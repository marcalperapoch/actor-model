package com.perapoch.concurrency.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActorRegistryImpl implements ActorRegistry {

    private final Map<ActorAddress, Actor> registry;

    ActorRegistryImpl() {
        this.registry = new ConcurrentHashMap<>();
    }

    @Override
    public Actor getActorByAddress(ActorAddress address) {
        return registry.get(address);
    }

    @Override
    public void registerActor(final Actor actor) {

        registry.compute(actor.getAddress(), (path, oldActor) -> {
            if (oldActor == null) {
                return actor;
            } else {
                actor.copyMessagesFrom(oldActor);
                return actor;
            }
        });
    }


}
