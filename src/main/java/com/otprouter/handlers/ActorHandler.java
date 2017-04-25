package com.otprouter.handlers;

import akka.actor.*;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.otprouter.actors.ActorSystemContainer;
import com.otprouter.actors.WorkerActor;
import org.json.JSONObject;
import org.opentripplanner.routing.services.GraphService;
import org.opentripplanner.standalone.CommandLineParameters;
import org.opentripplanner.standalone.OTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.concurrent.TimeUnit;


/**
 * This class allows to start OTP graph services.
 * It is an untyped actor and implement its methods
 *
 */
public class ActorHandler {

    private final Logger log = LoggerFactory.getLogger(ActorHandler.class);
    private final CommandLineParameters params;
    public GraphService graphService = null;

    /**
     * Constructor
     *
     * @access    public
     * @param     {Object}  CommandLineParameters object
     */
    public ActorHandler(CommandLineParameters params) {
        this.params = params;
        this.graphService = new GraphService(params.autoReload);
    }


    private String processMessage(Object request) {

        String response = null;
        JSONObject json = new JSONObject(request);
        ActorSystem sys = ActorSystemContainer.getInstance().getSystem();
        ActorRef actor = sys.actorFor(WorkerActor.PATH+json.get("workerId"));
        if(!actor.isTerminated()) {
            Timeout t = new Timeout(5, TimeUnit.SECONDS);
            Future<Object> fut = Patterns.ask(actor, request, t);

            try {
                response = (String) Await.result(fut, t.duration());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;

    }

}
