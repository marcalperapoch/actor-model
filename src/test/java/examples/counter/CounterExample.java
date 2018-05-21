package examples.counter;

import com.perapoch.concurrency.core.ActorAddress;
import com.perapoch.concurrency.core.Message;
import examples.BaseExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CounterExample extends BaseExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterExample.class);

    private static final int NUM_THREADS = 20;
    private static final Message INCREMENT_MSG = new Message("inc");

    @Override
    public void test() {
        final ActorAddress counter = actorRegistry.newActor(CounterActor.class, "counter");
        final ActorAddress checker = actorRegistry.newActor(CheckerActor.class, "checker", counter);

        final ExecutorService service = Executors.newFixedThreadPool(NUM_THREADS);
        final CompletableFuture[] futures = new CompletableFuture[NUM_THREADS];
        try {
            for (int i = 0; i < NUM_THREADS; ++i) {
                futures[i] = CompletableFuture.runAsync(new Incrementer(counter, i), service);
            }

            CompletableFuture.allOf(futures).get();

            TimeUnit.SECONDS.sleep(1);

            checker.tell(new Message("ask"));

        } catch (Exception e) {
           throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }

    }

    public static void main(String[] args) {
        new CounterExample().test();
    }

    private static class Incrementer implements Runnable {

        private final ActorAddress counter;
        private final String name;

        private Incrementer(ActorAddress counter, int index) {
            this.counter = counter;
            this.name = "Incrementor[" + index + "]";
        }

        @Override
        public void run() {
            LOGGER.info("Starting {} ...", name);
            for(int i = 0; i < 500; ++i) {
                counter.tell(INCREMENT_MSG);
            }
            LOGGER.info("Finished {}", name);
        }
    }
}
