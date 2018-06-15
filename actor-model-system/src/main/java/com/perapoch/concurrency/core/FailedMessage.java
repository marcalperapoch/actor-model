package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.util.Optional;

public class FailedMessage {

    private final Message originalMessage;
    private final ActorRef toAddress;

    public FailedMessage(Message original, ActorRef to) {
        this.originalMessage = original;
        this.toAddress = to;
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }

    public ActorRef getDestinatary() {
        return toAddress;
    }
}
