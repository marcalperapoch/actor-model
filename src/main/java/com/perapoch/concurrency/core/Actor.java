package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.nio.file.Path;
import java.util.Objects;

public abstract class Actor {

    private final Mailbox mailbox;

    private ActorRef actorRef;

    public Actor() {
        this.mailbox = new Mailbox();
    }

    protected abstract void onReceive(Message msg);

    void setActorRef(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    protected ActorRef self() {
        return actorRef;
    }

    ActorRef getActorRef() {
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

    ActorRef getContext() {
        return actorRef;
    }

    Path getPath() {
        return actorRef.getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(actorRef.getPath(), actor.actorRef.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorRef.getPath());
    }


}
