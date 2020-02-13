package com.bot2shop.processor;

import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IProcessor;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

import java.util.ArrayList;
import java.util.List;

public class Connections<KeyWordType> {

    // processor for logging
    private ILogger logger;
    private IProcessor<Exception> logErrorProcessor;
    public void setLogger(ILogger logger) {
        this.logger = logger;
        this.logErrorProcessor = logger::LogError;
    }

    // session list class
    private Sessions<KeyWordType> sessions;
    public void setSessions(Sessions<KeyWordType> sessions) { this.sessions = sessions; }

    // processor for incoming messages
    private IProcessor<String> incomeTextProcessor;
    public void setIncomeTextProcessor(MakeAction<KeyWordType> makeAction) {
        this.incomeTextProcessor = (connId, sessionId, parameter) -> {
            logger.LogIncome(connId, sessionId, parameter);
            try {
                Session<KeyWordType> session = sessions.getSession(connId, sessionId, connections);
                Phrase.Action action = makeAction.processMessage(session, parameter);
                if (action != null) {
                    switch (action) {
                        case ENDSESSION:
                            sessions.endSession(connId, sessionId);
                            break;
                    }
                }
            } catch (Exception e) {
                logger.LogError(connId, sessionId, e);
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
