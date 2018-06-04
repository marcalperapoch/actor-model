package examples.failure;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Message;
import examples.BaseExample;

import java.io.IOException;

public class PingPongCrashExample extends BaseExample {

    public void test() {

        final ActorRef ponger = actorSystem.newActor(CrashingPongActor.class, "crasheable ponger",5);
        final ActorRef pinger = actorSystem.newActor(PingActor.class, "ping", ponger);

        pinger.tell(new Message("start"));

        /*final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.schedule(() -> {
            ponger.tell(new Message("pum!"));
        }, 10, TimeUnit.SECONDS);*/

    }

    public static void main(String[] args) throws IOException {
        //System.in.read();
        new PingPongCrashExample().test();
    }
}
