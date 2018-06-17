package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

public class AnonymousActorRef implements ActorRef {

    @Override
    public <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object... args) {
        throw new UnsupportedOperationException("Anonymous actor can't create new actors");
    }

    @Override
    public ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress) {
        throw new UnsupportedOperationException("Anonymous actor can not restart other actors");
    }

    @Override
    public <T> void tell(T msg, ActorRef from) {
        throw new UnsupportedOperationException("You can not send msg to a no sender address");
    }

    @Override
    public <T> void tell(T msg) {
        throw new UnsupportedOperationException("You can not send msg to a no sender address");
    }

    @Override
    public ActorAddress getAddress() {
        return ActorAddress.anonymousAddress();
    }

}
