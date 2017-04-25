package com.otprouter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.otprouter.actors.ActorSystemContainer;
import com.otprouter.actors.DistributorActor;
import com.otprouter.handlers.ActorHandler;
import org.opentripplanner.routing.services.GraphService;
import org.opentripplanner.standalone.CommandLineParameters;


/**
 * This is main class of the app
 *
 * @function  testAction
 * @access    public
 * @param     {Object}  data routing data object
 * @param     {Object}  req  request object
 * @param     {Object}  res  response object
 */
public class App {

    private final CommandLineParameters params;
    public GraphService graphService = null;

    /**
     * Constructor
     *
     * @access    public
     * @param     {Object}  CommandLineParameters object
     */
    public App(CommandLineParameters params) {
        this.params = params;
    }

    /**
     * This is main function
     *
     * @function  main
     * @access    public static
     * @param     {Object}  String array type
     */
    public static void main(String args[]){

        /* Parse and validate command line parameters. */
        CommandLineParameters params = new CommandLineParameters();
        /* Initialize the params */
        new App(params);

        /* get actor system and create parent actor i.e DistributorActor */
        ActorSystemContainer systemContainer = ActorSystemContainer.getInstance();
        ActorSystem sys = systemContainer.getSystem();
        ActorRef actor = sys.actorOf(Props.create(DistributorActor.class, new ActorHandler(params)),DistributorActor.PATH);

    }
}
