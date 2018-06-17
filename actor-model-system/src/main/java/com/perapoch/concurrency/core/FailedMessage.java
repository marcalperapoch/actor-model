package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.util.Optional;

class FailedMessage {

    private final Message originalMessage;
    private final ActorRef toAddress;

    FailedMessage(Message original, ActorRef to) {
        this.originalMessage = original;
        this.toAddress = to;
    }

    Message getOriginalMessage() {
        return originalMessage;
    }

    ActorRef getDestinatary() {
        return toAddress;
    }
}
