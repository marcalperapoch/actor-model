package com.perapoch.concurrency.core;

import java.util.Objects;

public abstract class Actor {

    private final Mailbox mailbox;

    private ActorAddress address;
    private ActorAddress parentAddress;

    public Actor() {
        this.mailbox = new Mailbox();
    }

    protected abstract void onReceive(Message msg);

    void setParentAddress(ActorAddress parentAddress) {
        this.parentAddress = parentAddress;
    }

    ActorAddress getParentAddress() {
        return parentAddress;
    }

    void setAddress(ActorAddress address) {
        this.address = address;
    }

    protected ActorAddress self() {
        return address;
    }

    protected ActorAddress getAddress() {
        return self();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(address, actor.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
