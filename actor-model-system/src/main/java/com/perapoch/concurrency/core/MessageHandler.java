package com.perapoch.concurrency.core;

import com.perapoch.concurrency.ActorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    private final Map<Class<?>, BiConsumer<?, ActorRef>> handlers;

    public MessageHandler(Builder builder) {
        this.handlers = builder.handlers;
    }

    <T> void handle(Message<T> message) {
        final T value = message.getValue();
        final Class<T> klass = message.getMessageType();

        final BiConsumer<T, ActorRef> consumer = (BiConsumer<T, ActorRef>) handlers.getOrDefault(klass,
                (msg, actorRef) -> LOGGER.info("Message {} was lost because it can not be handled", value));
        consumer.accept(value, message.getFrom());
    }

    public static Builder builder() {
        return new MessageHandler.Builder();
    }


    public static final class Builder {

        private Map<Class<?>, BiConsumer<?, ActorRef>> handlers;

        private Builder() {
            this.handlers = new HashMap<>();
        }

        public <T> Builder withHandler(Class<T> klass, BiConsumer<T, ActorRef> handler) {
            this.handlers.put(klass, handler);
            return this;
        }

        public MessageHandler build() {
            return new MessageHandler(this);
        }
    }
}
