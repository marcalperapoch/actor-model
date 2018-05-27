package com.perapoch.concurrency.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RootSupervisor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootSupervisor.class);

    @Override
    protected void onReceive(Message msg) {
        if ("FailedMessage".equals(msg.getValue())) {
            final FailedMessage failedMessage = FailedMessage.class.cast(msg);

            LOGGER.error("Message {} could not be delivered to {}. Restarting destinatary...",
                    failedMessage.getOriginalMessage(),
                    failedMessage.getDestinatary());

            failedMessage.getDestinatary().restart();
        }
    }

}
