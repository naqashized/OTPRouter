package com.otprouter.actors;

/**
 * Created by tahir.n on 20/04/2017.
 */
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.otprouter.handlers.ActorHandler;
import org.json.JSONObject;
import org.opentripplanner.routing.core.RoutingRequest;
import org.opentripplanner.routing.core.TraverseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.opentripplanner.routing.core.TraverseMode.BICYCLE;
import static org.opentripplanner.routing.core.TraverseMode.CAR;
import static org.opentripplanner.routing.core.TraverseMode.WALK;

/**
 * This class represents parent actor of the system.
 * It is an untyped actor and implement its methods
 *
 */
public class DistributorActor extends UntypedActor {

    private final Logger log = LoggerFactory.getLogger(DistributorActor.class);
    public static final String PATH = "distributor";
    private ActorHandler actorHandler;


    /**
     * Constructor
     *
     * @access    public
     * @param     {Object}  ActorHandler object
     */
    public DistributorActor(ActorHandler actorHandler){
        this.actorHandler = actorHandler;
    }

    @Override
    public void preStart(){
        log.info("Starting Parent Actor");
        /* create 3 worker actors based on modes */
        context().actorOf(Props.create(WorkerActor.class, CAR, actorHandler), CAR.toString());
        context().actorOf(Props.create(WorkerActor.class, WALK, actorHandler), WALK.toString());
        context().actorOf(Props.create(WorkerActor.class, BICYCLE, actorHandler), BICYCLE.toString());
    }

    @Override
    public void postStop(){
        log.info("Stopping Parent Actor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        /* Scheme to distribute work. But obviously needs revamp. I could not have much idea about OTP services */
        RoutingRequest request = (RoutingRequest)message;
        ActorRef actor;
        if(request.modes.getCar())
            actor = context().actorFor(WorkerActor.PATH+CAR);
        else if(request.modes.getBicycle())
            actor = context().actorFor(WorkerActor.PATH+BICYCLE);
        else
            actor = context().actorFor(WorkerActor.PATH+WALK);

        if(!actor.isTerminated())
            actor.tell(message, this.getSender());
        log.info(message.toString());
    }
}
