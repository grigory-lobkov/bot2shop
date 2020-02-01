package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

public class IncomeText<KeyWordType> {

    // each word of user preparator
    private IPreparator<KeyWordType> preparator;
    public void setPreparator(IPreparator<KeyWordType> preparator) { this.preparator = preparator; }

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // phrases
    private Phrases phrases;
    public void setPhrases(Phrases phrases) { this.phrases = phrases; }

    // Process incoming user message
    void processMessage(Session session, String inText){
        KeyWordType[] userWords = preparator.prepareInput(inText);
        Phrase phrase = phrases.findPhraseByKeywords(userWords);
        //TODO
        String outText = inText+"!";
        logger.LogOutcome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
    }

}
