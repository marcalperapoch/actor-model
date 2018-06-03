package examples.failure;

import com.perapoch.concurrency.ActorAddress;
import com.perapoch.concurrency.core.Message;
import examples.BaseExample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingPongCrashExample extends BaseExample {

    public void test() {

        final ActorAddress ponger = actorSystem.newActor(CrashingPongActor.class, "crasheable ponger","pum!");
        final ActorAddress pinger = actorSystem.newActor(PingActor.class, "ping", ponger);

        pinger.tell(new Message("start"));

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.schedule(() -> {
            ponger.tell(new Message("pum!"));
        }, 10, TimeUnit.SECONDS);

    }

    public static void main(String[] args) {
        new PingPongCrashExample().test();
    }
}
