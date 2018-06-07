package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.nio.file.Path;

public interface ActorRegistry {

    default Actor getActorByActorRef(ActorRef address) {
        return getActorByAddress(address.getAddress());
    }

    Actor getActorByAddress(ActorAddress address);

    void registerActor(Actor actor);
}
