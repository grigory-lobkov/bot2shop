package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Session;

public class IncomeText {

    private ILogger logger;

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    // Process incoming user message
    public void processMessage(Session session, String inText){
        String outText = inText+"!";
        logger.LogIncome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
    }

}
