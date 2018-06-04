package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.nio.file.Path;

public interface ActorRegistry {

    default Actor getActorByAddress(ActorRef address) {
        return getActorByPath(address.getPath());
    }

    Actor getActorByPath(Path path);

    void registerActor(Actor actor);
}
