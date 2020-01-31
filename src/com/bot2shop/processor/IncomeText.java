package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Session;

public class IncomeText {

    // each word of user preparator
    private IPreparator preparator;
    public void setPreparator(IPreparator preparator) {
        this.preparator = preparator;
    }

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // Process incoming user message
    void processMessage(Session session, String inText){
        String outText = inText+"!";
        logger.LogOutcome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
    }

}
