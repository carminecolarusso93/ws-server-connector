package it.unisannio.websocket.test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/messages/in")
public class WebsocketServerEndpointIn {
    private final Logger LOGGER = Logger.getLogger(WebsocketServerEndpointIn.class.getName());
    private SessionManager manager = SessionManager.getInstance();

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.log(Level.INFO, "Session " + session.getId() + " opened.");
        manager.addSession(session, true);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
//    	System.out.println("[" + this.toString() + "]" + "Message received: " + message);
    	manager.broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        LOGGER.log(Level.INFO, "Session " + session.getId() + " closed.");
        manager.removeSession(session, true);
    }
}
