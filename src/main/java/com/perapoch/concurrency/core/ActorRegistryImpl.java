package com.perapoch.concurrency.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ActorRegistryImpl implements ActorRegistry {

    private final Map<ActorAddress, Actor> registry;
    private final MessageDispatcher messageDispatcher;

    public ActorRegistryImpl(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
        this.registry = new ConcurrentHashMap<>();
        this.messageDispatcher.start();
    }

    @Override
    public void tell(ActorAddress to, Message msg, ActorAddress from) {
        msg.setFrom(from);
        messageDispatcher.newMessage(registry.get(to), msg);
    }

    @Override
    public <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object ... args) {
        try {
            final Class<?>[] types = toTypes(args);
            final Actor actor = klass.getConstructor(types).newInstance(args);
            return register(actor, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?>[] toTypes(Object ...  args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    private ActorAddress register(Actor actor, String name) {
        final ActorAddress actorAddress = new ActorAddress(this, name);
        actor.setAddress(actorAddress);
        registry.put(actorAddress, actor);
        messageDispatcher.addActor(actor);
        return actorAddress;
    }
}
