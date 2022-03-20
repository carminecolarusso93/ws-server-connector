package it.unisannio.websocket.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/messages/out")
public class WebsocketServerEndpointOut {
	
	private SessionManager manager = SessionManager.getInstance();
	
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session " + session.getId() + " opened.");
        manager.addSession(session, false);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        manager.setSubscription(session.getId(), new HashSet<String>(Arrays.asList(message)));
//    	System.out.println("[" + this.toString() + "]" + "Message received: " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Session " + session.getId() + " closed.");
        manager.removeSession(session, false);
    }
    
}
