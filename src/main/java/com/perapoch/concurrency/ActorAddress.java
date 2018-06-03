package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Message;

import java.nio.file.Path;

public interface ActorAddress {

    ActorAddress NO_SENDER = new ActorAddress() {

        @Override
        public void tell(Message msg, ActorAddress from) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }

        @Override
        public void tell(Message msg) {
            throw new UnsupportedOperationException("You can not send msg to a no sender address");
        }

        @Override
        public ActorContext getContext() {
            throw new UnsupportedOperationException("You can not get context of a no sender address");
        }

        @Override
        public Path getPath() {
            return null;
        }

    };

    void tell(Message msg, ActorAddress from);

    void tell(Message msg);

    ActorContext getContext();

    Path getPath();
}
