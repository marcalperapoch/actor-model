package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.ActorAddress;
import com.perapoch.concurrency.core.Message;

public interface ActorRef extends ActorFactory {

    ActorRef NO_SENDER = new ActorRef() {

        @Override
        public <T extends Actor> ActorRef newActor(Class<T> klass, String name, Object... args) {
            return null;
        }

        @Override
        public ActorRef restart(ActorRef address, Message lostMessage, ActorRef senderAddress) {
            return null;
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
            return null;
        }

    };

    <T> void tell(T msg, ActorRef from);

    <T> void tell(T msg);

    ActorAddress getAddress();
}
