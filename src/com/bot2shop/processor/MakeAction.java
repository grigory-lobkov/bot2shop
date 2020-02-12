package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

import java.util.SplittableRandom;


public class MakeAction<KeyWordType> {

    private SplittableRandom random = new SplittableRandom();

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
    private Phrase.Action doPhraseAction(Phrase phrase, Session session) {
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
    private Phrase.Action doPhraseActionSAY(Phrase phrase, Session session) {
        String outText = phrase.sayText;
        logger.LogOutcome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
        return phrase.action;
    }

    // Process incoming user message
    Phrase.Action processMessage(Session session, String inText) {

        // search by keywords
        KeyWordType[] userWords = preparator.prepareInput(inText);
        Phrase<KeyWordType>[] foundPhrases = phrases.findPhraseByKeywords(userWords, session.lastRoom, session.lastPhrase);
        if (foundPhrases != null && foundPhrases.length > 0) {
            for (Phrase p: foundPhrases) {
                if(p.showOnlyOnce) {
                    if(!session.getShown(p)) continue;
                }
                if(p.showChance < 100) {
                    int rnd = random.nextInt(100);
                    if(rnd>=p.showChance) continue;
                }
                if(p.showOnlyOnce) {
                    session.setShown(p);
                }
                return doPhraseAction(p, session);
            }
        }

        // search by last state
        foundPhrases = phrases.findPhraseByLast(session.lastRoom, session.lastPhrase);
        if (foundPhrases.length > 0) {
            for (Phrase p: foundPhrases) {
                if(p.showOnlyOnce) {
                    if(!session.getShown(p)) continue;
                }
                if(p.showChance < 100) {
                    int rnd = random.nextInt(100);
                    if(rnd>=p.showChance) continue;
                }
                if(p.showOnlyOnce) {
                    session.setShown(p);
                }
                return doPhraseAction(p, session);
            }
        }
        return null;
    }

}
