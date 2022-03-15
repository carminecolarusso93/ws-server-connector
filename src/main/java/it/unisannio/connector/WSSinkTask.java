package it.unisannio.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.DeploymentException;
import javax.websocket.Session;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.glassfish.tyrus.client.ClientManager;

import it.unisannio.websocket.WebsocketEndpoint;

public class WSSinkTask extends SinkTask{
	//private static CountDownLatch latch;
	private Session session;
	
	@Override
	public String version() {
		return WSSinkTask.class.getPackage().getImplementationVersion();
	}

	@Override
	public void put(Collection<SinkRecord> arg0) {
		if(!arg0.isEmpty()) {
			arg0.forEach((record) -> {
				try {
					this.session.getBasicRemote().sendText(record.toString());
					System.out.println("Put invoked.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	public void start(Map<String, String> arg0) {
		ClientManager client = ClientManager.createClient();
		WSSinkConnectorConfig configProperties = new WSSinkConnectorConfig(arg0);
		try {
			URI uri = new URI(configProperties.getString(WSSinkConnectorConfig.CONNECTION_URI_CONFIG));
			this.session = client.connectToServer(WebsocketEndpoint.class, uri);
			System.out.println("Task started.");
			//latch.await();
		} catch (DeploymentException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		try {
			this.session.close(new CloseReason(CloseCodes.GOING_AWAY, "WSSinkTask stopped."));
			System.out.println("Task stopped.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
