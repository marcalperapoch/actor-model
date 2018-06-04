package examples.counter;

import com.perapoch.concurrency.ActorRef;
import com.perapoch.concurrency.core.Message;
import examples.BaseExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CounterExample extends BaseExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterExample.class);

    private static final int MESSAGES_TO_SEND = 10000;
    private static final int NUM_THREADS = 4;
    private static final int MESSAGE_PER_THREAD = MESSAGES_TO_SEND / NUM_THREADS;
    private static final Message INCREMENT_MSG = new Message("inc");

    @Override
    public void test() {
        final ActorRef counter = actorSystem.newActor(CounterActor.class, "counter");
        final ActorRef checker = actorSystem.newActor(CheckerActor.class, "checker", counter);

        final ExecutorService service = Executors.newFixedThreadPool(NUM_THREADS);
        final CompletableFuture[] futures = new CompletableFuture[NUM_THREADS];
        try {
            for (int i = 0; i < NUM_THREADS; ++i) {
                futures[i] = CompletableFuture.runAsync(new Incrementer(counter, i), service);
            }

            CompletableFuture.allOf(futures).get();

            checker.tell(new Message("ask"));

        } catch (Exception e) {
           throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }

    }

    public static void main(String[] args) {
        final long initialTime = System.currentTimeMillis();
        new CounterExample().test();
        LOGGER.info("Time: {}ms", System.currentTimeMillis() - initialTime);
    }

    private static class Incrementer implements Runnable {

        private final ActorRef counter;
        private final String name;

        private Incrementer(ActorRef counter, int index) {
            this.counter = counter;
            this.name = "Incrementor[" + index + "]";
        }

        @Override
        public void run() {
            LOGGER.info("Starting {} ...", name);
            for(int i = 0; i < MESSAGE_PER_THREAD; ++i) {
                counter.tell(INCREMENT_MSG);
            }
            LOGGER.info("Finished {}", name);
        }
    }
}
