package m2dl.pcr.akka.crible;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main runtime class.
 */
public class System {

    public static final Logger log = LoggerFactory.getLogger(m2dl.pcr.akka.helloworld3.System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("actor-system");

        final ActorRef launcherActorRef = actorSystem.actorOf(Props.create(Launcher.class, 100), "launcher-actor");

        launcherActorRef.tell("start", null);
    }
}
