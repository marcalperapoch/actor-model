package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.ActorAddress;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderActor.class);
    private static final int MAGIC_NUMBER = 43;

    private final ActorAddress consumer;

    public SenderActor(ActorAddress consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("SenderActor has received a {} message!", msg.getValue());
        if ("start".equals(msg.getValue())) {

            consumer.tell(new Message("hello from Sender"), self());

        } else if ("hello from Receiver".equals(msg.getValue())) {

            consumer.tell(new Message(MAGIC_NUMBER), self());
        }
    }
}
