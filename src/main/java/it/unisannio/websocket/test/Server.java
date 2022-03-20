package it.unisannio.websocket.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.DeploymentException;

public class Server {
	private static final Logger LOGGER = Logger.getLogger("WSSinkConnector.main");
	private static final CountDownLatch latch = new CountDownLatch(1);
	
	public static void main(String[] args) {
		org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server(args[0], 8025, "/ws", new HashMap<>(), new HashSet<>(Arrays.asList(WebsocketServerEndpointIn.class, WebsocketServerEndpointOut.class)));
		try {     
			server.start();
			LOGGER.log(Level.INFO,"Server started!");
	        latch.await();
	    } catch (DeploymentException | InterruptedException e) {
	    	throw new RuntimeException(e);
	    } finally {
	    	server.stop();
	    }

	}

}
