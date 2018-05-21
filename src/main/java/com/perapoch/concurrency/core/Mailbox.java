package com.perapoch.concurrency.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

final class Mailbox {

    final Queue<Message> messages;

    Mailbox() {
        this.messages = new ConcurrentLinkedDeque<>();
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
