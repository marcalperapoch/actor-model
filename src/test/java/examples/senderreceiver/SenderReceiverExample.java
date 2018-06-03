package examples.senderreceiver;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.core.*;
import examples.BaseExample;

public class SenderReceiverExample extends BaseExample {

    @Override
    public void test() {
        final ActorAddress receiverActor = actorSystem.newActor(ReceiverActor.class, "receiver");
        final ActorAddress senderActor = actorSystem.newActor(SenderActor.class, "sender", receiverActor);

        senderActor.tell(new Message("start"));
    }

    public static void main(String[] args) {
        new SenderReceiverExample().test();
    }
}
