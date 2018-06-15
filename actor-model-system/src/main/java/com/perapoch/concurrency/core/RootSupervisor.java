package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RootSupervisor extends Actor {


    protected void onReceive(FailedMessage failedMessage, ActorRef sender) {
        getContext().restart(failedMessage.getDestinatary(),
                failedMessage.getOriginalMessage(),
                sender);
    }

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(FailedMessage.class, this::onReceive)
                .build();
    }
}
