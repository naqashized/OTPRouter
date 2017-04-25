package com.otprouter.actors;

import akka.actor.ActorSystem;

/**
 * This is a singleton class for actor system .
 *
 */
public class ActorSystemContainer {

    private ActorSystem sys;
    /**
     * Constructor.
     *
     */
    private ActorSystemContainer() {
        /* create actor system called OTPRouter */
        sys = ActorSystem.create("OTPRouter");
    }

    /**
     * Returns ActorSystem instance
     *
     * @access    public
     */
    public ActorSystem getSystem() {
        return sys;
    }

    private static ActorSystemContainer instance = null;

    /**
     * Returns ActorSystemContainer instance
     *
     * @access    public static synchronized
     */
    public static synchronized ActorSystemContainer getInstance() {
        if (instance == null) {
            instance = new ActorSystemContainer();
        }
        return instance;
    }
}
