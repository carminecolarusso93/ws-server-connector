package it.unisannio.connector;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;

public class WSSinkConnectorConfig extends AbstractConfig{

	private static final String EMPTY_STRING = "";
	
	//Topics config
	public static final String TOPICS_CONFIG = WSSinkConnector.TOPICS_CONFIG;
	private static final String TOPICS_DOC =
	      "A list of kafka topics for the sink connector, separated by commas";
	public static final String TOPICS_DEFAULT = EMPTY_STRING;
	private static final String TOPICS_DISPLAY = "Kafka topics";
	
	//Topics regex config
	public static final String TOPICS_REGEX_CONFIG = "topics.regex";
	private static final String TOPICS_REGEX_DOC =
	      "Regular expression giving topics to consume. "
	          + "Under the hood, the regex is compiled to a <code>java.util.regex.Pattern</code>. "
	          + "Only one of "
	          + TOPICS_CONFIG
	          + " or "
	          + TOPICS_REGEX_CONFIG
	          + " should be specified.";
	public static final String TOPICS_REGEX_DEFAULT = EMPTY_STRING;
	private static final String TOPICS_REGEX_DISPLAY = "Topics regex";

	//Websocket connection uri config
	//TODO: update values
	public static final String CONNECTION_URI_CONFIG = "connection.uri";
	private static final String CONNECTION_URI_DEFAULT = "ws://0.0.0.0:8025/ws";
	private static final String CONNECTION_URI_DISPLAY = "Websocket Connection URI";
	private static final String CONNECTION_URI_DOC = "Connection URI";

	/*
	public static final String TOPIC_OVERRIDE_CONFIG = "topic.override.%s.%s";
	private static final String TOPIC_OVERRIDE_DEFAULT = EMPTY_STRING;
	private static final String TOPIC_OVERRIDE_DISPLAY = "Per topic configuration overrides.";
	public static final String TOPIC_OVERRIDE_DOC =
	      "The overrides configuration allows for per topic customization of configuration. "
	          + "The customized overrides are merged with the default configuration, to create the specific configuration for a topic.\n"
	          + "For example, ``topic.override.foo.collection=bar`` will store data from the ``foo`` topic into the ``bar`` collection.\n"
	          + "Note: All configuration options apart from '"
	          + CONNECTION_URI_CONFIG
	          + "' and '"
	          + TOPICS_CONFIG
	          + "' are overridable.";

	static final String PROVIDER_CONFIG = "provider";
	*/
	
	/*public WSSinkConnectorConfig(ConfigDef definition, Map<String, ?> originals) {
		super(definition, originals);
	}*/
	
	public static final String WS_GROUP = "Websocket";
	public static final ConfigDef CONFIG = createConfigDef();
	
	public WSSinkConnectorConfig(Map<String, ?> originals) {
		super(CONFIG, originals);
	}

	private static ConfigDef createConfigDef() {
		ConfigDef config = new ConfigDef();
		int orderInGroup = 0;
		config.define(
				TOPICS_CONFIG,
				Type.LIST,
				TOPICS_DEFAULT,
				Importance.HIGH,
				TOPICS_DOC,
				WS_GROUP,
				++orderInGroup,
				Width.MEDIUM,
				TOPICS_DISPLAY
		).define(
				TOPICS_REGEX_CONFIG,
				Type.STRING,
				TOPICS_REGEX_DEFAULT,
				Importance.HIGH,
				TOPICS_REGEX_DOC,
				WS_GROUP,
				++orderInGroup,
				Width.MEDIUM,
				TOPICS_REGEX_DISPLAY		
		).define(
				CONNECTION_URI_CONFIG,
				Type.STRING,
				CONNECTION_URI_DEFAULT,
				Importance.HIGH,
				CONNECTION_URI_DOC,
				WS_GROUP,
				++orderInGroup,
				Width.MEDIUM,
				CONNECTION_URI_DISPLAY			
		);
		return config;
	}
}
