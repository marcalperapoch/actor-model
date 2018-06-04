package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ActorRefImpl implements ActorRef {

    private final Path path;
    private final MessageDispatcher messageDispatcher;
    private final ActorRegistry registry;
    private final Map<Path, ActorRecipe> actorRecipes;
    private final String name;

    ActorRefImpl(final Path path,
                 final MessageDispatcher messageDispatcher,
                 final ActorRegistry registry) {
        this.path = path;
        this.messageDispatcher = messageDispatcher;
        this.registry = registry;
        this.actorRecipes = new ConcurrentHashMap<>();
        this.name = (path.getNameCount() > 0) ? path.getName(path.getNameCount() - 1).toString() : "root";
    }

    @Override
    public void tell(Message msg, ActorRef from) {
        tell(this, msg, from);
    }

    @Override
    public void tell(Message msg) {
        tell(this, msg, NO_SENDER);
    }

    private void tell(ActorRef to, Message msg, ActorRef from) {
        msg.setFrom(from);
        messageDispatcher.newMessage(registry.getActorByAddress(to), msg);
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object... args) {
        try {
            final Class<?>[] types = toTypes(args);
            final Constructor<? extends Actor> ctr = ConstructorUtils.getMatchingAccessibleConstructor(klass, types);
            final Actor actor = ctr.newInstance(args);
            final ActorRef address = register(actor, name, args);
            messageDispatcher.onNewActor(actor);
            return address;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress) {
        final Path path = address.getPath();
        final ActorRecipe recipe = actorRecipes.get(path);
        final ActorRef newActor = newActor(recipe.getKlass(), recipe.getName(), recipe.getArgs());
        if (senderAddress != null) {
            newActor.tell(lostMessage, senderAddress);
        } else {
            newActor.tell(lostMessage);
        }
        return newActor;
    }


    private ActorRef register(Actor actor, String name, Object[] args) {
        final ActorRef actorRef = createNewAddress(name);
        actor.setActorRef(actorRef);

        actorRecipes.computeIfAbsent(actorRef.getPath(), path ->  new ActorRecipe(actor.getClass(), name, args));

        registry.registerActor(actor);

        return actorRef;
    }

    private ActorRef createNewAddress(String name) {
        final Path newPath = path.resolve(name);
        return new ActorRefImpl(newPath, messageDispatcher, registry);
    }

    private Class<?>[] toTypes(Object ...  args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorRefImpl actorRef = (ActorRefImpl) o;
        return Objects.equals(path, actorRef.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "ActorRefImpl{" +
                "path=" + path +
                ", name='" + name + '\'' +
                '}';
    }
}
