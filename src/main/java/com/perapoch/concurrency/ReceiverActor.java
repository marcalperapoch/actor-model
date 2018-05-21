package com.perapoch.concurrency;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverActor.class);

    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("ReceiverActor has received a {} message!", msg.getValue());
        if ("hello from Sender".equals(msg.getValue())) {
            msg.getFrom().ifPresent(sender ->
                    sender.tell(new Message("hello from Receiver"))
            );
        } else {
            int magicNumber = Integer.parseInt(msg.getValue());
            msg.getFrom().ifPresent(sender -> sender.tell(new Message(magicNumber + 14)));
        }
    }
}
