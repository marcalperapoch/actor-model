package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class Actor {

    private final Mailbox mailbox;
    private final MessageHandler messageHandler;

    private ActorRef actorRef;

    public Actor() {
        this.mailbox = new Mailbox();
        this.messageHandler = createMessageHandler();
    }

    protected abstract MessageHandler createMessageHandler();

    <T> void onReceive(Message<T> msg) {
        messageHandler.handle(msg);
    }

    void setActorRef(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    protected void onActorRegistered() {}

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

    ActorAddress getAddress() {
        return actorRef.getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(actorRef.getAddress(), actor.actorRef.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorRef.getAddress());
    }


}
