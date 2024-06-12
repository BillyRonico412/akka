package m2dl.pcr.akka.crible;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Launcher extends UntypedActor {
    private final Integer size;
    private Integer n = 3;
    private final ActorRef cribleActorRef = getContext().actorOf(Props.create(Crible.class, 2), "crible-actor");

    public Launcher(Integer size) {
        this.size = size;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (n < size) {
            cribleActorRef.tell(n, getSelf());
            n++;
        }
    }
}
