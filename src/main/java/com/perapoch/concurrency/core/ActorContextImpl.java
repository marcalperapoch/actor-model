package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.ActorContext;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActorContextImpl implements ActorContext {

    private final Path path;
    private final MessageDispatcher messageDispatcher;
    private final ActorRegistry registry;
    private final Map<Path, ActorRecipe> actorRecipes;

    public ActorContextImpl(final Path path,
                            final MessageDispatcher messageDispatcher,
                            final ActorRegistry registry) {
        this.path = path;
        this.messageDispatcher = messageDispatcher;
        this.registry = registry;
        this.actorRecipes = new ConcurrentHashMap<>();
    }

    @Override
    public void tell(ActorAddress to, Message msg, ActorAddress from) {
        msg.setFrom(from);
        messageDispatcher.newMessage(registry.getActorByAddress(to), msg);
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object... args) {
        try {
            final Class<?>[] types = toTypes(args);
            final Constructor<? extends Actor> ctr = ConstructorUtils.getMatchingAccessibleConstructor(klass, types);
            final Actor actor = ctr.newInstance(args);

            return register(actor, name, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Actor> ActorAddress restart(ActorAddress address) {
        final Path path = address.getPath();
        final ActorRecipe recipe = actorRecipes.get(path);
        return newActor(recipe.getKlass(), recipe.getName(), recipe.getArgs());
    }


    private ActorAddress register(Actor actor, String name, Object[] args) {
        final ActorAddress actorAddress = createNewAddress(name);
        actor.setAddress(actorAddress);

        actorRecipes.computeIfAbsent(actorAddress.getPath(), path ->  new ActorRecipe(actor.getClass(), name, args));

        registry.registerActor(actor);

        return actorAddress;
    }

    private ActorAddress createNewAddress(String name) {
        final Path newPath = path.resolve(name);
        return new ActorAddressImpl(new ActorContextImpl(newPath, messageDispatcher, registry));
    }

    private Class<?>[] toTypes(Object ...  args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    @Override
    public String toString() {
        return "ActorContextImpl{" +
                "path=" + path +
                '}';
    }
}
