package com.bot2shop.model;

import com.bot2shop.interfaces.IConnection;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/*
 *  Stores user sessions and it's parameters
 */


public class Session<KeyWordType> {

    public int connId; // connection identifier
    public String sessionId; // session identifier
    public IConnection conn; // connection to messenger
    private ZonedDateTime createDate; // when session created
    private ZonedDateTime lastAccess; // last session access
    public Phrase<KeyWordType> lastPhrase;
    public Topic lastTopic;

    public Session(int connId, String sessionId, IConnection conn) {
        this.connId = connId;
        this.sessionId = sessionId;
        this.conn = conn;
        this.createDate = ZonedDateTime.now();
        this.lastTopic = null;
    }

    public Session refresh() {
        lastAccess = ZonedDateTime.now();
        return this;
    }

    static private Set<Phrase> shownPhrases = new HashSet<>(); // how many times Phrase shown

    public boolean getShown(Phrase phrase) {
        return shownPhrases.contains(phrase);
    }

    public void setShown(Phrase phrase) {
        shownPhrases.add(phrase);
    }

}