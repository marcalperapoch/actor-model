package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

public final class RootSupervisor extends Actor {

    @Override
    protected MessageHandler createMessageHandler() {
        return MessageHandler.builder()
                .withHandler(FailedMessage.class, this::onReceive)
                .build();
    }

    private void onReceive(FailedMessage failedMessage, ActorRef sender) {
        getActorRef().restart(failedMessage.getDestinatary(),
                failedMessage.getOriginalMessage(),
                sender);
    }
}
