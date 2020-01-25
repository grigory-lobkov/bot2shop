package com.bot2shop.processor;

import com.bot2shop.interfaces.IExtConnection;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Session;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Sessions {

    private ILogger logger;

    static private List<Session> sessions = new ArrayList<>(); // users sessions
    static private Hashtable<String, Integer> sessionsIds = new Hashtable<>(); // users sessions identifiers hashtable

    Session getSession(int connId, String sessionId, List<IExtConnection> connections) {
        // check if exists
        String hashKey = connId+"-"+sessionId;
        Integer sessKey = sessionsIds.get(hashKey);
        if(sessKey!=null) return sessions.get(sessKey).refresh();
        // new session
        logger.Log(connId, sessionId, "Start session");
        Session newSession = new Session(connId, sessionId, connections.get(connId));
        sessKey = sessions.size();
        sessionsIds.put(hashKey, sessKey);
        sessions.add(newSession);
        return newSession.refresh();
    }

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

}
