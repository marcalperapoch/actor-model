package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;

import java.util.Objects;

public class Message<T> {

    private final ActorRef from;
    private final T value;
    private final Class<T> messageType;

    public Message(T value, ActorRef from) {
        this.value = value;
        this.messageType = (Class<T>) value.getClass();
        this.from = from;
    }

    ActorRef getFrom() {
        return from;
    }

    T getValue() {
        return value;
    }

    Class<T> getMessageType() {
        return messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(value, message.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Message{" +
                "value='" + value + '\'' +
                '}';
    }
}
