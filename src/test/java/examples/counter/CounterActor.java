package examples.counter;

import com.perapoch.concurrency.core.Actor;
import com.perapoch.concurrency.core.Message;

public class CounterActor extends Actor {

    private int counter;

    public CounterActor() {
        this.counter = 0;
    }

    @Override
    protected void onReceive(Message msg) {
        if ("inc".equals(msg.getValue())) {
            ++counter;
        } else {
            msg.getFrom().ifPresent(sender -> sender.tell(new Message(counter)));
        }
    }
}
