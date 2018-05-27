package com.perapoch.concurrency.core;

import java.util.Objects;
import java.util.Optional;

public class Message {

    private ActorAddress from;
    private String value;

    public Message(String value) {
        this.value = value;
    }

    public Message(Integer number) {
        this.value = String.valueOf(number);
    }

    public Optional<ActorAddress> getFrom() {
        return Optional.ofNullable(from);
    }

    public void setFrom(ActorAddress from) {
        this.from = from;
    }

    public String getValue() {
        return value;
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
