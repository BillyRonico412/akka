package m2dl.pcr.akka.crible;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

import java.util.ArrayList;

public class Crible extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef nextCribleActorRef;
    private Integer n;
    private ArrayList<Integer> numbers = new ArrayList<>();

    public Crible(Integer n) {
        this.n = n;
        log.info("Crible constructor");
        getContext().become(behavior1);
    }

    Procedure<Object> behavior1 = new Procedure<Object>() {
        @Override
        public void apply(Object msg) throws Exception {
            if (msg instanceof Integer) {
                if ((Integer) msg % n != 0) {
                    log.info("Je suis un nombre premier : " + msg);
                    nextCribleActorRef = Crible.this.getContext().actorOf(Props.create(Crible.class, msg), "crible-actor-" + msg);
                    getContext().become(behavior2);
                } else {
                    unhandled(msg);
                }
                getSender().tell(new ACK(), getSelf());
            } else {
                unhandled(msg);
            }
        }
    };

    Procedure<Object> behavior2 = new Procedure<Object>() {
        @Override
        public void apply(Object msg) throws Exception {
            if (msg instanceof Integer) {
                if ((Integer) msg % n != 0) {
                    nextCribleActorRef.tell(msg, getSelf());
                    getContext().become(behavior3);
                } else {
                    unhandled(msg);
                }
                getSender().tell(new ACK(), getSelf());
            } else {
                unhandled(msg);
            }
        }
    };

    Procedure<Object> behavior3 = new Procedure<Object>() {
        @Override
        public void apply(Object msg) throws Exception {
            if (msg instanceof ACK) {
                getContext().become(behavior2);
                if (!numbers.isEmpty()) {
                    nextCribleActorRef.tell(numbers.remove(0), getSelf());
                }
            } else if (msg instanceof Integer && (Integer) msg % n != 0) {
                numbers.add((Integer) msg);
            } else {
                unhandled(msg);
            }
            getSender().tell(new ACK(), getSelf());
        }
    };

    @Override
    public void onReceive(Object o) throws Exception {
        behavior1.apply(o);
    }
}
