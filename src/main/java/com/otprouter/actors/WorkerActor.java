package com.otprouter.actors;

import akka.actor.UntypedActor;
import com.otprouter.handlers.ActorHandler;
import org.opentripplanner.routing.core.TraverseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents worker actors.
 * It is an untyped actor and implement its methods
 *
 */
public class WorkerActor extends UntypedActor {

    public static final String PATH ="/worker/";
    /* workerId to differentiate actors */
    private TraverseMode mode;
    private ActorHandler actorHandler;
    private final Logger log = LoggerFactory.getLogger(WorkerActor.class);

    public WorkerActor(TraverseMode mode, ActorHandler actorHandler){

        this.mode = mode;
        this.actorHandler = actorHandler;
    }

    @Override
    public void preStart(){

        log.info("Starting actor with id "+mode);

    }

    @Override
    public void postStop(){
        log.info("Stopping actor with id "+mode);
    }
    @Override
    public void onReceive(Object message) throws Exception {

        getContext().parent().tell("response here", this.getSender());
    }


}