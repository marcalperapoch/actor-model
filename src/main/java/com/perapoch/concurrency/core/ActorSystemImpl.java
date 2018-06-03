package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorContext;
import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.ActorSystem;

import java.nio.file.Paths;

public class ActorSystemImpl implements ActorSystem {

    private final MessageDispatcher messageDispatcher;
    private final ActorRegistry actorRegistry;
    private final ActorContext baseContext;
    private final ActorAddress rootSupervisor;

    public ActorSystemImpl(int numThreads) {
        this.actorRegistry = new ActorRegistryImpl();
        this.messageDispatcher = new MessageDispatcher(actorRegistry, numThreads);
        this.baseContext = new ActorContextImpl(Paths.get("/"), messageDispatcher, actorRegistry);
        this.rootSupervisor = baseContext.newActor(RootSupervisor.class, "root");
    }

    @Override
    public void start() {
        messageDispatcher.start();
    }

    @Override
    public <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object... args) {
        return rootSupervisor.getContext().newActor(klass, name, args);
    }

    @Override
    public <T extends Actor> ActorAddress restart(ActorAddress address) {
        throw new UnsupportedOperationException("Actor system can not restart actors!");
    }
}
