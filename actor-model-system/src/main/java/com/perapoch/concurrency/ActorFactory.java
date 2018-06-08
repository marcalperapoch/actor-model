package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;

public interface ActorFactory {

    <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object ... args);

    ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress);
}
