package examples;

import com.perapoch.concurrency.core.ActorRegistry;
import com.perapoch.concurrency.core.ActorRegistryImpl;
import com.perapoch.concurrency.core.MessageDispatcher;

public abstract class BaseExample {

    private static final int DEFAULT_NUM_THREADS = 10;

    protected final ActorRegistry actorRegistry;

    public BaseExample() {
        this(DEFAULT_NUM_THREADS);
    }

    public BaseExample(int numThreads) {
        actorRegistry = new ActorRegistryImpl(new MessageDispatcher(10));
    }

    public abstract void test();
}
