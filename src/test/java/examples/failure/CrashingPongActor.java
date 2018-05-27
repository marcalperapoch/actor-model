package examples.failure;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static examples.TestUtils.sleep;

public class CrashingPongActor extends Actor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrashingPongActor.class);
    private final String crashingMessage;

    private int totalPings;

    public CrashingPongActor(String crashingMessage) {
        this.crashingMessage = crashingMessage;
        this.totalPings = 1;
    }


    @Override
    protected void onReceive(Message msg) {
        LOGGER.info("Got {} messages ({} pings so far)", msg.getValue(), totalPings);
        if (crashingMessage.equals(msg.getValue())) {
            throw new IllegalArgumentException("I don't like processing " + msg.getValue());
        } else {
            ++totalPings;
            sleep(ThreadLocalRandom.current().nextInt(500, 2000), TimeUnit.MILLISECONDS);
            msg.getFrom().ifPresent(sender -> sender.tell(new Message("pong")));
        }
    }
}
