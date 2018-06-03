package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;

public interface ActorFactory {

    <T extends Actor> ActorAddress newActor(Class<T> klass, String name, Object ... args);

    ActorAddress restart(ActorAddress address, Message lostMessage, ActorAddress senderAddress);
}
