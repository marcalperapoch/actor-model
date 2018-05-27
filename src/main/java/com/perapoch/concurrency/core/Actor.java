package com.perapoch.concurrency.core;

public abstract class Actor {

    private ActorAddress address;
    private final Mailbox mailbox;

    public Actor() {
        this.mailbox = new Mailbox();
    }

    protected abstract void onReceive(Message msg);


    void setAddress(ActorAddress address) {
        this.address = address;
    }

    protected ActorAddress self() {
        return address;
    }

    boolean hasPendingMessages() {
        return mailbox.hasPendingMessages();
    }

    void enqueueMessage(Message msg) {
        mailbox.receive(msg);
    }

    Message getNextMessage() {
        return mailbox.getNextMessage();
    }
}
