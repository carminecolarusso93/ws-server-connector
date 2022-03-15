package it.unisannio.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;


public class WSSinkConnector extends SinkConnector{

	private Map<String, String> properties;
	
	@Override
	public String version() {
		return WSSinkConnector.class.getPackage().getImplementationVersion();
	}

	@Override
	public ConfigDef config() {
		return WSSinkConnectorConfig.CONFIG;
	}

	@Override
	public void start(final Map<String, String> arg0) {
		this.properties = arg0;
		System.out.println("Connector started.");
	}

	@Override
	public void stop() {
		System.out.println("Connector stopped.");	
	}

	@Override
	public Class<? extends Task> taskClass() {
		return WSSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int arg0) {
		ArrayList<Map<String, String>> configs = new ArrayList<>();
	    for (int i = 0; i < arg0; i++) {
	      configs.add(this.properties);
	    }
	    return configs;
	}

}
