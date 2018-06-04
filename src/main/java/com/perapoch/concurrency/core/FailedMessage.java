package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.util.Optional;

public class FailedMessage extends Message {

    private final Message originalMessage;
    private final ActorRef toAddress;

    public FailedMessage(String value, Message original, ActorRef to) {
        super(value);
        this.originalMessage = original;
        this.toAddress = to;
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }

    public ActorRef getDestinatary() {
        return toAddress;
    }

    @Override
    public Optional<ActorRef> getFrom() {
        return originalMessage.getFrom();
    }
}
