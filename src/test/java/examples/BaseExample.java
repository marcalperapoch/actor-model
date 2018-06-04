package examples;

import com.perapoch.concurrency.ActorSystem;
import com.perapoch.concurrency.core.ActorSystemImpl;
import sun.tools.java.Environment;

public abstract class BaseExample {

    private static final int DEFAULT_NUM_THREADS =  Runtime.getRuntime().availableProcessors();

    protected final ActorSystem actorSystem;

    public BaseExample() {
        this(DEFAULT_NUM_THREADS);
    }

    public BaseExample(int numThreads) {
        actorSystem = new ActorSystemImpl(numThreads);
        actorSystem.start();
    }

    public abstract void test();
}
