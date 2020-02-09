package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

public class MakeAction<KeyWordType> {

    // each word of user preparator
    private IPreparator<KeyWordType> preparator;
    public void setPreparator(IPreparator<KeyWordType> preparator) { this.preparator = preparator; }

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // phrases
    private Phrases phrases;
    public void setPhrases(Phrases phrases) { this.phrases = phrases; }

    // do Phrase Action
    Phrase.Action doPhraseAction(Phrase phrase, Session session) {
        Phrase.Action result = null;
        switch (phrase.action) {
            case SAY:
                try {
                    result = doPhraseActionSAY(phrase, session);
                } catch (Exception e) {
                    logger.LogError(session.connId, session.sessionId, "MakeAction.doPhraseActionSAY " + e);
                }
                break;
        }
        if (phrase.canBeLast) {
            session.lastPhrase = phrase;
            session.lastRoom = phrase.room;
        }
        return result;
    }

    // do phrase action Say
    Phrase.Action doPhraseActionSAY(Phrase phrase, Session session) {
        String outText = phrase.sayText;
        logger.LogOutcome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
        return phrase.action;
    }

    // Process incoming user message
    Phrase.Action processMessage(Session session, String inText) {
        KeyWordType[] userWords = preparator.prepareInput(inText);
        Phrase[] phrase = phrases.findPhraseByKeywords(userWords, session.lastRoom, session.lastPhrase);
        if (phrase.length > 0) { // todo: check, phrase able to show in session
            return doPhraseAction(phrase[0], session);
        }
        return null;
    }

}
