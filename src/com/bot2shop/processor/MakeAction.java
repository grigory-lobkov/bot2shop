package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

import java.util.SplittableRandom;

/*
 *   Actions, after target phrases found
 */


public class MakeAction<KeyWordType> {

    private SplittableRandom random = new SplittableRandom(); // random generator

    // each word of user preparator
    private IPreparator<KeyWordType> preparator;

    public void setPreparator(IPreparator<KeyWordType> preparator) {
        this.preparator = preparator;
    }

    // logger
    private ILogger logger;

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    // phrases
    private Phrases phrases;

    public void setPhrases(Phrases phrases) {
        this.phrases = phrases;
    }

    // do Phrase Action
    private Phrase.Action doPhraseAction(Phrase phrase, Session session) {
        Phrase.Action result = null;
        switch (phrase.action) {
            case SAY:
                try {
                    result = doPhraseActionSAY(phrase, session);
                } catch (Exception e) {
                    logger.LogError(session.connId, session.sessionId, e);
                }
                break;
            default:
        }
        if (phrase.canBeLast) {
            session.lastPhrase = phrase;
            session.lastTopic = phrase.topic;
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
        Phrase<KeyWordType>[] foundPhrases = phrases.findPhraseByKeywords(userWords, session.lastTopic, session.lastPhrase);
        if (foundPhrases != null && foundPhrases.length > 0) {
            for (Phrase p : foundPhrases) {
                if (p.showOnlyOnce) if (!session.getShown(p)) continue;
                if (p.showChance < 100) {
                    int rnd = random.nextInt(100);
                    if (rnd >= p.showChance) continue;
                }
                if (p.showOnlyOnce) session.setShown(p);
                return doPhraseAction(p, session);
            }
        }

        // search by last state
        foundPhrases = phrases.findPhraseByLast(session.lastTopic, session.lastPhrase);
        if (foundPhrases.length > 0) {
            for (Phrase p : foundPhrases) {
                if (p.showOnlyOnce) if (!session.getShown(p)) continue;
                if (p.showChance < 100) {
                    int rnd = random.nextInt(100);
                    if (rnd >= p.showChance) continue;
                }
                if (p.showOnlyOnce) session.setShown(p);
                return doPhraseAction(p, session);
            }
        }

        // search for default /start phrase
        if(session.lastTopic == null && session.lastPhrase == null) {
            KeyWordType[] startUserWords = preparator.prepareInput("/start");
            foundPhrases = phrases.findPhraseByKeywords(startUserWords, null, null);
            if (foundPhrases != null && foundPhrases.length > 0) {
                for (Phrase p : foundPhrases) {
                    return doPhraseAction(p, session);
                }
            }
        }
        return null;
    }

}