package com.bot2shop.processor;

import com.bot2shop.Main;
import com.bot2shop.ext.ConsoleChat;
import com.bot2shop.interfaces.IExtConnection;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Session;

import java.util.ArrayList;
import java.util.List;

public class Connections {

    // logging
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // session list class
    private Sessions sessions;
    public void setSessions(Sessions sessions) { this.sessions = sessions; }

    // processor for incoming messages
    IncomeText incomeTextProcessor;
    public void setIncomeTextProcessor(IncomeText incomeTextProcessor) { this.incomeTextProcessor = incomeTextProcessor; }

    // list of connections to different messengers
    protected List<IExtConnection> connections = new ArrayList<>();

    // add new connection with some parameters
    public <ExtConnClass extends IExtConnection> void add(String... params) {

        IExtConnection conn = new ExtConnClass() {

            @Override
            public void incomeText(String sessionId, String inText) {
                master.incomeText(this.getConnId(), sessionId, inText);
            }

            @Override
            public void LogError(String sessionId, String errorText) {
                logger.LogError(this.getConnId(), sessionId, errorText);
            }

        };
        conn.setSuperclass(this);
        conn.setConnId(connections.size());
        connections.add(conn);
        conn.setup(params);
        conn.start();
    }

    // start all connections
    public void startAll() {
        for (IExtConnection conn: connections){
            conn.start(); // TODO: Start multiple bots, restart if failed
        }
    }

    // receives message and returns an answer
    // sessionId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back
    private void incomeText(int connId, String sessionId, String inText) {
        logger.LogIncome(connId, sessionId, inText);
        try {
            Session session = sessions.getSession(connId, sessionId);
            incomeTextProcessor.processMessage(session, inText);
        } catch (Exception e) {
            logger.LogError(connId, sessionId, "incomeText "+e);
        }
    }

}
