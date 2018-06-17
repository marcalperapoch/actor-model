package com.perapoch.concurrency.core;

import java.util.ArrayDeque;
import java.util.Queue;

final class Mailbox {

    private final Queue<Message> messages;

    Mailbox() {
        this.messages = new ArrayDeque<>();
    }

    void receive(final Message message) {
        messages.add(message);
    }

    boolean hasPendingMessages() {
        return !messages.isEmpty();
    }

    Message getNextMessage() {
        return messages.poll();
    }
}
