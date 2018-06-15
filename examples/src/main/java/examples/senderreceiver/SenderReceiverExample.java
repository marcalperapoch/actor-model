package examples.senderreceiver;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.*;
import examples.BaseExample;

public class SenderReceiverExample extends BaseExample {

    @Override
    public void test() {
        final ActorRef receiverActor = actorSystem.newActor(ReceiverActor.class, "receiver");
        final ActorRef senderActor = actorSystem.newActor(SenderActor.class, "sender", receiverActor);

        senderActor.tell("start");
    }

    public static void main(String[] args) {
        new SenderReceiverExample().test();
    }
}
