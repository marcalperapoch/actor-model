package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;

public interface ActorFactory {

    <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object ... args);

    <T extends Actor> ActorAddress restart(ActorAddress address);
}
