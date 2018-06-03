package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.ActorContext;

import java.nio.file.Path;
import java.util.Objects;

public abstract class Actor {

    private final Mailbox mailbox;

    private ActorAddress address;

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

    ActorAddress getAddress() {
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

    void copyMessagesFrom(final Actor other) {
        while (other.hasPendingMessages()) {
            enqueueMessage(other.getNextMessage());
        }
    }

    ActorContext getContext() {
        return address.getContext();
    }

    Path getPath() {
        return getContext().getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(address.getPath(), actor.address.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address.getPath());
    }


}
