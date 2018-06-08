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
        public void tell(Message msg, ActorRef from) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }

        @Override
        public void tell(Message msg) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }

        @Override
        public ActorAddress getAddress() {
            return null;
        }

    };

    void tell(Message msg, ActorRef from);

    void tell(Message msg);

    ActorAddress getAddress();
}
