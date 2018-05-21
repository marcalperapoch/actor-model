package com.perapoch.concurrency.core;

public abstract class Actor {

    private final Mailbox mailbox;
    private ActorAddress address;

    public Actor() {
        this.mailbox = new Mailbox();
    }

    Mailbox getMailbox() {
        return mailbox;
    }

    protected abstract void onReceive(Message msg);

    boolean hasPendingMessages() {
        return mailbox.hasPendingMessages();
    }

    void consumeMessage() {
        final Message message = mailbox.getNextMessage();
        onReceive(message);
    }

    void setAddress(ActorAddress address) {
        this.address = address;
    }

    protected ActorAddress self() {
        return address;
    }
}
