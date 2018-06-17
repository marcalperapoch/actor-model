package examples.mapreduce;

import com.perapoch.concurrency.ActorRef;
import examples.BaseExample;

public class MapReduceExample extends BaseExample {

    private static final int NUM_MAPPERS = 3;
    private static final int NUM_REDUCERS = 3;

    public static void main(String[] args) {
        new MapReduceExample().test();
    }

    @Override
    public void test() {
        final ActorRef mapperActor = actorSystem.newActor(MapperActor.class, "mapper", NUM_MAPPERS);
        final ActorRef reducerActor = actorSystem.newActor(ReducerActor.class, "reducer", NUM_REDUCERS);

        final ActorRef orchestratorActor = actorSystem.newActor(OrchestratorActor.class,
                "orchestrator",
                mapperActor,
                reducerActor);

        orchestratorActor.tell("start");
    }
}
