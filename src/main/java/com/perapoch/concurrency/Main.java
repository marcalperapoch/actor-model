package com.perapoch.concurrency;

import com.perapoch.concurrency.core.MessageDispatcher;
import com.perapoch.concurrency.core.ActorAddress;
import com.perapoch.concurrency.core.ActorRegistry;
import com.perapoch.concurrency.core.ActorRegistryImpl;
import com.perapoch.concurrency.core.Message;

public class Main {

    public static void main(String[] args) {
        final ActorRegistry actorRegistry = new ActorRegistryImpl(new MessageDispatcher(10));

        final ActorAddress receiverActor = actorRegistry.newActor(ReceiverActor.class, "receiver");
        final ActorAddress senderActor = actorRegistry.newActor(SenderActor.class, "sender", receiverActor);

        senderActor.tell(new Message("start"));
    }
}
