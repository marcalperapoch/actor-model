package com.perapoch.concurrency.core;

public interface ActorRegistry {

    void tell(ActorAddress to, Message msg, ActorAddress from);

    <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object ... args);
}
