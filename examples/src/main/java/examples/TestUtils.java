package examples;

import java.util.concurrent.TimeUnit;

public final class TestUtils {

    private TestUtils() {}

    public static void sleep(long amount, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(amount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
