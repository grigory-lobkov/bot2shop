package com.bot2shop.processor;

import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Session;

import java.util.*;

/*
 *   User sessions
 *
 *   TODO: periodical idle session search and safe kill
 */


public class Sessions<KeyWordType> {

    // processor for logging
    private ILogger logger;

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    static private Map<String, Session> sessions = new Hashtable<>(); // users sessions identifiers hashtable

    String getHashKey(int connId, String sessionId) {
        return connId + "-" + sessionId;
    }

    Session<KeyWordType> getSession(int connId, String sessionId, List<IConnection> connections) {
        // check if exists
        String hashKey = getHashKey(connId, sessionId);
        Session<KeyWordType> session = sessions.get(hashKey);
        if (session != null) return session.refresh();
        // new session
        logger.Log(connId, sessionId, "Start session");
        session = new Session<KeyWordType>(connId, sessionId, connections.get(connId));
        sessions.put(hashKey, session);
        return session;
    }

    void endSession(int connId, String sessionId) {
        // check if exists
        String hashKey = getHashKey(connId, sessionId);
        Session<KeyWordType> session = sessions.get(hashKey);
        if (session != null) {
            // kill session
            logger.Log(connId, sessionId, "Exit session");
            sessions.remove(hashKey);
        }
    }

}