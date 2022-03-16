package it.unisannio.websocket.test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.Session;

public class SessionManager {
	private final Set<Session> inPeers;
	private final Set<Session> outPeers;
	private final Map<String, Set<String>> subscriptions;
	
	private ExecutorService executorServiceIn, executorServiceOut;
	private static final int POOL_SIZE = 2;
	private static final Pattern pattern = Pattern.compile("areaName\":\"[0-9a-zA-Z-_]+\"");
	private static final int REGEX_OFFSET = "areaname\":".length();
	
	private static SessionManager manager = null;
	
	public static SessionManager getInstance() {
		if(manager == null)
			manager = new SessionManager();
		return manager;
	}
	
	private SessionManager() {
		this.inPeers = Collections.synchronizedSet(new HashSet<Session>());
		this.outPeers = Collections.synchronizedSet(new HashSet<Session>());
		this.executorServiceIn = Executors.newFixedThreadPool(POOL_SIZE);
		this.executorServiceOut = Executors.newFixedThreadPool(POOL_SIZE);
		this.subscriptions = Collections.synchronizedMap(new HashMap<String, Set<String>>());
	}
	
	public void addSession(Session session, boolean in) {
		if(in)
			this.inPeers.add(session);
		else {
			this.outPeers.add(session);
			this.subscriptions.put(session.getId(), new HashSet<String>());
		}
	}
	
	public void removeSession(Session session, boolean in) {
		if(in)
			this.inPeers.remove(session);
		else {
			this.outPeers.remove(session);
			this.subscriptions.remove(session.getId());
		}
	}
	
	//TODO: the access could be synchronized
	public void broadcast(String message) {
		this.executorServiceIn.submit(new BroadcastWorker(message));
	}
	
	public void setSubscription(String sessionId, Set<String> newSubscritpions) {
		this.executorServiceOut.submit(new SubscriptionWorker(sessionId, newSubscritpions));
	}
	
	private class BroadcastWorker implements Runnable {

		private String message;
		
		public BroadcastWorker(String message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			String area = pattern.matcher(message).group(1);
			area = area.substring(REGEX_OFFSET, area.length()-1);
			System.out.println("area: " + area );
			for(Session session : outPeers) {
				try {
					if(subscriptions.get(session.getId()).contains(area))
						session.getBasicRemote().sendText(this.message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class SubscriptionWorker implements Runnable {

		private String sessionId;
		private Set<String> newSubscritpions;
		
		public SubscriptionWorker(String sessionId, Set<String> newSubscritpions) {
			this.sessionId = sessionId;
			this.newSubscritpions = newSubscritpions;
		}
		
		@Override
		public void run() {
			subscriptions.put(sessionId, newSubscritpions);
		}
		
	}
	
}
