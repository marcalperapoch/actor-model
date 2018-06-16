package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ActorRefImpl implements ActorRef {

    private final ActorAddress address;
    private final MessageDispatcher messageDispatcher;
    private final ActorRegistry registry;
    private final Map<ActorAddress, ActorRecipe> actorRecipes;

    ActorRefImpl(final ActorAddress address,
                 final MessageDispatcher messageDispatcher,
                 final ActorRegistry registry) {
        this.address = address;
        this.messageDispatcher = messageDispatcher;
        this.registry = registry;
        this.actorRecipes = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void tell(T msg, ActorRef from) {
        tell(this, msg, from);
    }

    @Override
    public <T> void tell(T msg) {
        tell(this, msg, NO_SENDER);
    }

    private <T> void tell(ActorRef to, T msg, ActorRef from) {
        final Message<T> message = new Message<>(msg, from);
        messageDispatcher.newMessage(registry.getActorByActorRef(to), message);
    }

    @Override
    public ActorAddress getAddress() {
        return address;
    }

    @Override
    public <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object... args) {
        try {
            final Class<?>[] types = toTypes(args);
            final Constructor<? extends Actor> ctr = ConstructorUtils.getMatchingAccessibleConstructor(klass, types);
            final Actor actor = ctr.newInstance(args);
            final ActorRef address = register(actor, name, args);
            messageDispatcher.onNewActor(actor);
            actor.onActorRegistered();
            return address;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress) {
        final ActorAddress path = address.getAddress();
        final ActorRecipe recipe = actorRecipes.get(path);
        final ActorRef newActor = newActor(recipe.getKlass(), recipe.getName(), recipe.getArgs());
        if (senderAddress != null) {
            newActor.tell(lostMessage.getValue(), senderAddress);
        } else {
            newActor.tell(lostMessage.getValue());
        }
        return newActor;
    }


    private ActorRef register(Actor actor, String name, Object[] args) {
        final ActorRef actorRef = createNewAddress(name);
        actor.setActorRef(actorRef);

        actorRecipes.computeIfAbsent(actorRef.getAddress(), path ->  new ActorRecipe(actor.getClass(), name, args));

        registry.registerActor(actor);

        return actorRef;
    }

    private ActorRef createNewAddress(String name) {
        final ActorAddress newAddress = ActorAddress.of(address, name);
        return new ActorRefImpl(newAddress, messageDispatcher, registry);
    }

    private Class<?>[] toTypes(Object ...  args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorRefImpl actorRef = (ActorRefImpl) o;
        return Objects.equals(address, actorRef.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "ActorRefImpl{" +
                "address=" + address +
                '}';
    }
}
