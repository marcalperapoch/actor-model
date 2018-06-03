package examples.noactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NoActorCounterExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoActorCounterExample.class);

    private static final int NUM_THREADS = 4;
    private static final int MESSAGES_TO_SEND = 10000;
    private static final int MESSAGE_PER_THREAD = MESSAGES_TO_SEND / NUM_THREADS;

    public void test() {

        final Counter counter = new Counter();

        final ExecutorService service = Executors.newFixedThreadPool(NUM_THREADS);
        final CompletableFuture[] futures = new CompletableFuture[NUM_THREADS];
        try {
            for (int i = 0; i < NUM_THREADS; ++i) {
                futures[i] = CompletableFuture.runAsync(new Incrementer(counter, i), service);
            }

            CompletableFuture.allOf(futures).get();

            final int result = counter.get();
            LOGGER.info("Result {}", result);

        } catch (Exception e) {
           throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }

    }

    public static void main(String[] args) {
        final long initialTime = System.currentTimeMillis();
        new NoActorCounterExample().test();
        LOGGER.info("Time: {}ms", System.currentTimeMillis() - initialTime);
    }

    private static class Incrementer implements Runnable {

        private final Counter counter;
        private final String name;

        private Incrementer(Counter counter, int index) {
            this.counter = counter;
            this.name = "Incrementor[" + index + "]";
        }

        @Override
        public void run() {
            LOGGER.info("Starting {} ...", name);
            for(int i = 0; i < MESSAGE_PER_THREAD; ++i) {
                counter.inc();
            }
            LOGGER.info("Finished {}", name);
        }
    }

    private static final class Counter {

        private final AtomicInteger counter;

        public Counter() {
            this.counter = new AtomicInteger(0);
        }

        public void inc() {
            counter.incrementAndGet();
        }

        public int get() {
            return counter.get();
        }
    }
}
