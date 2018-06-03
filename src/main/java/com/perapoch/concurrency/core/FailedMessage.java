package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorAddress;

public class FailedMessage extends Message {

    private final Message originalMessage;
    private final ActorAddress toAddress;

    public FailedMessage(String value, Message original, ActorAddress to) {
        super(value);
        this.originalMessage = original;
        this.toAddress = to;
    }

    public Message getOriginalMessage() {
        return originalMessage;
    }

    public ActorAddress getDestinatary() {
        return toAddress;
    }
}
