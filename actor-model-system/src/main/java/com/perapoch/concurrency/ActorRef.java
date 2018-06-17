package com.perapoch.concurrency;

import com.perapoch.concurrency.core.ActorAddress;
import com.perapoch.concurrency.core.AnonymousActorRef;

public interface ActorRef extends ActorFactory {

    ActorRef NO_SENDER = new AnonymousActorRef();

    <T> void tell(T msg, ActorRef from);

    <T> void tell(T msg);

    ActorAddress getAddress();
}
