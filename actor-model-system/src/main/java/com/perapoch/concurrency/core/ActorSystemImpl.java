package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.ActorSystem;

public class ActorSystemImpl implements ActorSystem {

    private final MessageDispatcher messageDispatcher;
    private final ActorRegistry actorRegistry;
    private final ActorRef systemRef;
    private final ActorRef rootSupervisor;

    public ActorSystemImpl(int numThreads) {
        this.actorRegistry = new ActorRegistryImpl();
        this.messageDispatcher = new MessageDispatcher(actorRegistry, numThreads);
        this.systemRef = new ActorRefImpl(ActorAddress.rootAddress(), messageDispatcher, actorRegistry);
        this.rootSupervisor = systemRef.newActor(RootSupervisor.class, "root");
    }

    @Override
    public void start() {
        messageDispatcher.start();
    }

    @Override
    public <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object... args) {
        return rootSupervisor.newActor(klass, name, args);
    }

    @Override
    public ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress) {
        throw new UnsupportedOperationException("Actor system can not restart actors!");
    }
}
