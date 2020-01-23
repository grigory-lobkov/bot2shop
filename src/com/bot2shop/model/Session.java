package com.bot2shop.model;

import com.bot2shop.interfaces.IExtConnection;

import java.time.ZonedDateTime;

/*
*  Stores user sessions and it's parameters
*  */

public class Session {

    public int connId; // connection identifier
    public String sessId; // session identifier
    public IExtConnection conn; // connection to messenger
    public ZonedDateTime createDate; // when session created
    public ZonedDateTime lastAccess; // last session access

    public Session(int connId, String sessId, IExtConnection conn) {
        this.connId = connId;
        this.sessId = sessId;
        this.conn = conn;
        createDate = ZonedDateTime.now();
    }

    public Session refresh() {
        lastAccess = ZonedDateTime.now();
        return this;
    }

}
