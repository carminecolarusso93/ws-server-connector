package it.unisannio.websocket.test;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class WebsocketClientEndpoint {
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println ("Session " + session.getId() + " opened.");
		//try {
			//session.getBasicRemote().sendText("start");
		//} catch (IOException e) {
			//throw new RuntimeException(e);
		//}
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		/*BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println ("--- Received " + message);
			String userInput = bufferRead.readLine();
			return userInput;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}*/
		System.out.println ("Received " + message);
		return message;
		//TODO: to be used for backpressure purposes
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Session " + session.getId() +
			" closed because " + closeReason);
		//latch.countDown();
	}
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println("Error in session " + session.getId());
		try {
			throw throwable;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//latch.countDown();
	}

}
