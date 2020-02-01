package com.bot2shop.model;

import com.bot2shop.interfaces.IConnection;

import java.time.ZonedDateTime;

/*
*  Stores user sessions and it's parameters
*  */

public class Session {

    public int connId; // connection identifier
    public String sessionId; // session identifier
    public IConnection conn; // connection to messenger
    private ZonedDateTime createDate; // when session created
    private ZonedDateTime lastAccess; // last session access

    public Session(int connId, String sessionId, IConnection conn) {
        this.connId = connId;
        this.sessionId = sessionId;
        this.conn = conn;
        createDate = ZonedDateTime.now();
    }

    public Session refresh() {
        lastAccess = ZonedDateTime.now();
        return this;
    }

}
