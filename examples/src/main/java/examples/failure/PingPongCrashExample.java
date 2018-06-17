package examples.failure;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Message;
import examples.BaseExample;

import java.io.IOException;

public class PingPongCrashExample extends BaseExample {

    private static final int FAILING_PERCENTAGE = 20;

    public void test() {

        final ActorRef ponger = actorSystem.newActor(CrashingPongActor.class, "crasheable ponger", FAILING_PERCENTAGE);
        final ActorRef pinger = actorSystem.newActor(PingActor.class, "pinger", ponger);

        pinger.tell("start");

    }

    public static void main(String[] args) throws IOException {
        //System.in.read();
        new PingPongCrashExample().test();
    }
}
