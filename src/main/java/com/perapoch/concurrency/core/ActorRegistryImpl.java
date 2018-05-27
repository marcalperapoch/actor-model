package com.perapoch.concurrency.core;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActorRegistryImpl implements ActorRegistry {

    private final Map<ActorAddress, Actor> registry;
    private final Map<String, ActorRecipe> actorRecipes;
    private final MessageDispatcher messageDispatcher;
    private final ActorAddress rootSupervisor;

    public ActorRegistryImpl(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
        this.registry = new ConcurrentHashMap<>();
        this.actorRecipes = new ConcurrentHashMap<>();
        this.rootSupervisor = newActor(RootSupervisor.class, "root");
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
            return register(actor, name, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Actor> void restart(String actorName) {

        final ActorRecipe recipe = actorRecipes.get(actorName);
        if (recipe != null) {
            final ActorAddress restartedActor = newActor(recipe.getKlass(), recipe.getName(), recipe.getArgs());
            messageDispatcher.markAsHealthy(restartedActor);
        }
    }

    private Class<?>[] toTypes(Object ...  args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    private ActorAddress register(Actor actor, String name, Object ... args) {
        final ActorAddress actorAddress = new ActorAddress(this, name);
        actor.setAddress(actorAddress);
        actorRecipes.computeIfAbsent(name, actorName ->  new ActorRecipe(actor.getClass(), name, args));

        registry.compute(actorAddress, (address, oldActor) -> {
            if (oldActor == null) {
                actor.setParentAddress(rootSupervisor);
                return actor;
            } else {
                actor.setParentAddress(oldActor.getParentAddress());
                while (oldActor.hasPendingMessages()) {
                    messageDispatcher.newMessage(actor, oldActor.getNextMessage());
                }
                return actor;
            }
        });


        return actorAddress;
    }



}
