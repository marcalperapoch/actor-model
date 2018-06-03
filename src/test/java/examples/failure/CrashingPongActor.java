package examples.failure;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static examples.TestUtils.sleep;

public class CrashingPongActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrashingPongActor.class);
    private final int failingMultiple;

    private int totalPings;

    public CrashingPongActor(int failingMultiple) {
        this.failingMultiple = failingMultiple;
        this.totalPings = 1;
    }


    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("Got {} message ({} pings so far)", msg.getValue(), totalPings);
        if (totalPings % failingMultiple == 0) {
            throw new IllegalArgumentException("Time to crash " + msg.getValue() + " is multiple of " + failingMultiple);
        } else {
            ++totalPings;
            sleep(ThreadLocalRandom.current().nextInt(500, 2000), TimeUnit.MILLISECONDS);
            msg.getFrom().ifPresent(sender -> sender.tell(new Message("pong")));
        }
    }
}
