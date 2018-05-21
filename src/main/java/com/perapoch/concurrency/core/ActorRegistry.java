package com.perapoch.concurrency.core;

import java.util.List;

public interface ActorRegistry {

    void tell(ActorAddress to, Message msg, ActorAddress from);

    <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object ... args);

    List<Actor> getActors();
}
