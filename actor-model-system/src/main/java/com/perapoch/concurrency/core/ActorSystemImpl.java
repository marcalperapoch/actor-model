package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.ActorSystem;

public class ActorSystemImpl implements ActorSystem {

    private final MessageDispatcher messageDispatcher;
    private final ActorRef rootSupervisor;

    public ActorSystemImpl(int numThreads) {
        final ActorRegistry actorRegistry = new ActorRegistryImpl();
        this.messageDispatcher = new MessageDispatcher(actorRegistry, numThreads);
        final ActorRef systemRef = new ActorRefImpl(ActorAddress.rootAddress(), messageDispatcher);
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
