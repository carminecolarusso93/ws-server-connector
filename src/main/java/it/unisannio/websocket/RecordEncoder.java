package it.unisannio.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.apache.kafka.connect.sink.SinkRecord;

import com.google.gson.Gson;

public class RecordEncoder implements Encoder.Text<SinkRecord>{
	private static Gson gson = new Gson();
	
	@Override
    public String encode(SinkRecord record) throws EncodeException {
        return gson.toJson(record);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
