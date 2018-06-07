package examples.failure;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static examples.TestUtils.sleep;
import static java.lang.String.format;

public class CrashingPongActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrashingPongActor.class);
    private final int failingBoundary;

    private int totalPings;

    public CrashingPongActor(int failingBoundary) {
        this.failingBoundary = failingBoundary;
        this.totalPings = 1;
    }


    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("Got {} message ({} pings so far)", msg.getValue(), totalPings);
        final int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
        if (randomNum < failingBoundary) {
            throw new IllegalArgumentException(format("Time to crash for message %s. Random %d, Boundary %d", msg.getValue(), randomNum, failingBoundary));
        } else {
            ++totalPings;
            sleep(ThreadLocalRandom.current().nextInt(500, 2000), TimeUnit.MILLISECONDS);
            msg.getFrom().ifPresent(sender -> sender.tell(new Message("pong")));
        }
    }
}
