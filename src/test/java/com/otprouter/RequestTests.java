package com.otprouter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.otprouter.actors.ActorSystemContainer;
import com.otprouter.actors.DistributorActor;
import com.otprouter.handlers.ActorHandler;
import org.opentripplanner.common.model.GenericLocation;
import org.opentripplanner.routing.core.RoutingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static org.opentripplanner.routing.core.TraverseMode.CAR;

/**
 * Test class
 */
public class RequestTests {

    private final Logger log = LoggerFactory.getLogger(RequestTests.class);

    private GenericLocation randomLocation() {
        return new GenericLocation(Math.random(), Math.random());
    }

    @Test
    public void graphTest(){

        log.info("Forwarding request");
        RoutingRequest request = new RoutingRequest();
        request.addIntermediatePlace(randomLocation());
        request.addMode(CAR);
        ActorSystem sys = ActorSystemContainer.getInstance().getSystem();
        ActorRef actor = sys.actorFor(DistributorActor.PATH);
        //actor.tell(request, null);
        String response = null;
        if(!actor.isTerminated()) {
            Timeout t = new Timeout(5, TimeUnit.SECONDS);
            Future<Object> fut = Patterns.ask(actor, request, t);

            try {
                response = (String) Await.result(fut, t.duration());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
