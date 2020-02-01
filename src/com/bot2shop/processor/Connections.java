package com.bot2shop.processor;

import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IProcessor;
import com.bot2shop.model.Session;
import com.bot2shop.processor.IncomeText;

import java.util.ArrayList;
import java.util.List;

public class Connections {

    // processor for logging
    private ILogger logger;
    private IProcessor<String> logErrorProcessor;
    public void setLogger(ILogger logger) {
        this.logger = logger;
        this.logErrorProcessor = logger::LogError;
    }

    // session list class
    private Sessions sessions;
    public void setSessions(Sessions sessions) { this.sessions = sessions; }

    // processor for incoming messages
    private IncomeText incomeText;
    private IProcessor<String> incomeTextProcessor;
    public void setIncomeTextProcessor(IncomeText incomeText) {
        this.incomeText = incomeText;
        this.incomeTextProcessor = (connId, sessionId, parameter) -> {
            logger.LogIncome(connId, sessionId, parameter);
            try {
                Session session = sessions.getSession(connId, sessionId, connections);
                incomeText.processMessage(session, parameter);
            } catch (Exception e) {
                logger.LogError(connId, sessionId, "incomeText "+e);
            }
        };
    }

    // list of connections to different messengers
    private List<IConnection> connections = new ArrayList<>();

    // add new connection with some parameters
    public void add(IConnection conn, String... params) {
        int id = connections.size();
        conn.setConnId(id);
        conn.setErrorProcessor(logErrorProcessor);
        conn.setIncomeTextProcessor(incomeTextProcessor);
        connections.add(conn);
        conn.setup(params);
    }

    // start all connections
    public void startAll() {
        for (IConnection conn: connections){
            conn.start(); // TODO: Start multiple bots simultaneously, restart if failed
        }
    }

}
