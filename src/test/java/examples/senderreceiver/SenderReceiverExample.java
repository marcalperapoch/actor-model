package examples.senderreceiver;

import com.perapoch.concurrency.core.*;
import examples.BaseExample;

public class SenderReceiverExample extends BaseExample {

    @Override
    public void test() {
        final ActorAddress receiverActor = actorRegistry.newActor(ReceiverActor.class, "receiver");
        final ActorAddress senderActor = actorRegistry.newActor(SenderActor.class, "sender", receiverActor);

        senderActor.tell(new Message("start"));
    }

    public static void main(String[] args) {
        new SenderReceiverExample().test();
    }
}
