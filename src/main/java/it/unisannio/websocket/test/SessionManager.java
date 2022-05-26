package it.unisannio.websocket.test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.Session;

public class SessionManager {
    private final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    private final Set<Session> inPeers;
    private final Set<Session> outPeers;
    private final Map<String, Set<String>> subscriptions;

    private ExecutorService executorServiceIn, executorServiceOut;
    private static final int POOL_SIZE_IN = 10;
    private static final int POOL_SIZE_OUT = 5;
    //	private static final Pattern pattern = Pattern.compile("areaName\":\"[0-9a-zA-Z-_]+\"");
    private static final Pattern pattern = Pattern.compile("(areaName=)([0-9a-zA-Z-_]+)");
    private static final int REGEX_OFFSET = "areaname\":".length();

    private static SessionManager manager = null;

    public static SessionManager getInstance() {
        if (manager == null)
            manager = new SessionManager();
        return manager;
    }

    private SessionManager() {
        this.inPeers = Collections.synchronizedSet(new HashSet<Session>());
        this.outPeers = Collections.synchronizedSet(new HashSet<Session>());
        this.executorServiceIn = Executors.newFixedThreadPool(POOL_SIZE_IN);
        this.executorServiceOut = Executors.newFixedThreadPool(POOL_SIZE_OUT);
        this.subscriptions = new ConcurrentHashMap<String, Set<String>>();
        LOGGER.log(Level.INFO, "Session Manager Started");
    }

    public void addSession(Session session, boolean in) {
        if (in)
            this.inPeers.add(session);
        else {
            this.outPeers.add(session);
            this.subscriptions.put(session.getId(), new HashSet<String>());
        }
    }

    public void removeSession(Session session, boolean in) {
        if (in)
            this.inPeers.remove(session);
        else {
            this.outPeers.remove(session);
            this.subscriptions.remove(session.getId());
        }
    }

    //TODO: the access could be synchronized
    public void broadcast(String message) {
//        LOGGER.log(Level.INFO, "SessionManager.broadcast: message = " + message);
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
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String area = matcher.group(2);
                for (Session session : outPeers) {
                    try {
                        if (subscriptions.get(session.getId()).contains(area)) {
                            LOGGER.log(Level.FINE, "Sending to: " + session.getId() + ", message = " + message);
                            session.getBasicRemote().sendText(this.message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class SubscriptionWorker implements Runnable {
        private Logger LOGGER = Logger.getAnonymousLogger();
        private String sessionId;
        private Set<String> newSubscritpions;

        public SubscriptionWorker(String sessionId, Set<String> newSubscritpions) {
            this.sessionId = sessionId;
            this.newSubscritpions = newSubscritpions;
        }

        @Override
        public void run() {
            Object[] objects = this.newSubscritpions.toArray();
            StringBuilder sb = new StringBuilder((String) objects[0]);
            for (int i = 1; i < objects.length; i++){
                sb.append(",");
                sb.append((String) objects[i]);
            }
            LOGGER.log(Level.INFO,"Adding subscription to: " + sb.toString());
            subscriptions.put(sessionId, newSubscritpions);
        }

    }

}
