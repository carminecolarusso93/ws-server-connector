package it.unisannio.websocket.test;

import java.net.URI;
import java.util.Scanner;

import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

public class Client {

	public static final String SERVER = "ws://0.0.0.0:8025/ws/messages/in";
	
	public static void main(String[] args) throws Exception{
		ClientManager client = ClientManager.createClient();
        String message;

        // connect to server
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message:");
        String user = scanner.nextLine();
        Session session = client.connectToServer(WebsocketClientEndpoint.class, new URI(SERVER));
        System.out.println("You are logged in as: " + user);

        // repeatedly read a message and send it to the server (until quit)
        do {
            message = scanner.nextLine();
            session.getBasicRemote().sendText(message);
        } while (!message.equalsIgnoreCase("quit"));

	}

}
