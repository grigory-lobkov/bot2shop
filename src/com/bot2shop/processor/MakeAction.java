package com.bot2shop.processor;

import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Session;

import java.util.ArrayList;
import java.util.List;
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
    private Phrase.Action doPhraseAction(Phrase phrase, Session session, Phrase<KeyWordType>[] foundPhrases) {
        Phrase.Action result = null;
        try {
            switch (phrase.action) {
                case SAY:
                    result = doPhraseActionSAY(phrase, session);
                    break;
                case OBSERVETITLES:
                    result = doPhraseActionOBSERVETITLES(phrase, session, foundPhrases);
                    break;
                default:
            }
        } catch (Exception e) {
            logger.LogError(session.connId, session.sessionId, e);
        }
        if (phrase.canBeLast) {
            session.lastPhrase = phrase;
            session.lastTopic = phrase.topic;
        }
        return result;
    }

    // do phrase action SAY
    private Phrase.Action doPhraseActionSAY(Phrase phrase, Session session) {
        String outText = phrase.sayText;
        logger.LogOutcome(session.connId, session.sessionId, outText);
        session.conn.sendText(session.sessionId, outText);
        return phrase.action;
    }

    // do phrase action OBSERVETITLES
    private Phrase.Action doPhraseActionOBSERVETITLES(Phrase phrase, Session session, Phrase<KeyWordType>[] foundPhrases) {
        String outText = phrase.sayText;
        String[] outVariants = new String[foundPhrases.length];
        String[] callbacks = new String[foundPhrases.length];
        int i = 0;

        logger.LogOutcome(session.connId, session.sessionId, outText);
        for (Phrase<KeyWordType> p : foundPhrases) {
            outVariants[i] = p.title;
            callbacks[i] = "/id=" + p.id;
            logger.LogOutcome(session.connId, session.sessionId, callbacks[i] + " > " + outVariants[i]);
            i++;
        }
        session.conn.sendTextVariants(session.sessionId, outText, outVariants, callbacks);
        return phrase.action;
    }

    Phrase<KeyWordType> checkPhraseShowable(Phrase<KeyWordType> phrase, Session session) {
        if (phrase.showOnlyOnce) if (!session.getShown(phrase)) return null;
        if (phrase.showChance < 100) {
            int rnd = random.nextInt(100);
            if (rnd >= phrase.showChance) return null;
        }
        if (phrase.showOnlyOnce) session.setShown(phrase);
        return phrase;
    }

    // Process incoming user message
    Phrase.Action processMessage(Session session, String inText) {
        Phrase<KeyWordType>[] foundPhrases;

        // search by action
        if(inText!=null && inText.substring(0,1).compareTo("/")==0) {
            // search by /id=xxx
            if(inText.substring(0,4).compareTo("/id=")==0) {
                try {
                    Integer idx = Integer.parseInt(inText.substring(4));
                    Phrase<KeyWordType> p = phrases.findPhraseById(idx);
                    return doPhraseAction(p, session, null);
                } catch (Exception e) {
                    logger.LogError(session.connId, session.sessionId, e);
                }
            }
        }

        // search by title
        foundPhrases = phrases.findPhraseByTitle(inText);
        if (foundPhrases != null && foundPhrases.length > 0) {
            for (Phrase p : foundPhrases) {
                p = checkPhraseShowable(p, session);
                if (p == null) continue;
                return doPhraseAction(p, session, null);
            }
            logger.LogError(session.connId, session.sessionId, new RuntimeException(foundPhrases.length + " variants by title found, no one able to show now. Using first."));
            return doPhraseAction(foundPhrases[0], session, null);
        }

        // search by keywords
        KeyWordType[] userWords = preparator.prepareInput(inText);
        foundPhrases = phrases.findPhraseByKeywords(userWords, session.lastTopic, session.lastPhrase);
        if (foundPhrases != null && foundPhrases.length > 0) {
            if (foundPhrases.length == 1) {
                return doPhraseAction(foundPhrases[0], session, null);
            } else {
                Phrase<KeyWordType>[] masterPhrases = phrases.findTopicObserver(session.lastTopic);
                if (masterPhrases.length > 0) {
                    Phrase<KeyWordType> masterPhrase = masterPhrases[0]; // maybe sometime will do a filtering
                    return doPhraseAction(masterPhrase, session, foundPhrases);
                } else {
                    logger.LogError(session.connId, session.sessionId, new RuntimeException(foundPhrases.length + " variants found, but OBSERVETITLES not exists."));
                    return doPhraseAction(foundPhrases[0], session, null);
                }
            }
        }

        // search by last state
        foundPhrases = phrases.findPhraseByLast(session.lastTopic, session.lastPhrase);
        if (foundPhrases.length > 0) {
            for (Phrase p : foundPhrases) {
                p = checkPhraseShowable(p, session);
                if (p == null) continue;
                return doPhraseAction(p, session, null);
            }
            logger.LogError(session.connId, session.sessionId, new RuntimeException(foundPhrases.length + " variants found, no one able to show now. Using first."));
            return doPhraseAction(foundPhrases[0], session, null);
        }

        // search for default /start phrase
        if (session.lastTopic == null && session.lastPhrase == null) {
            KeyWordType[] startUserWords = preparator.prepareInput("/start");
            foundPhrases = phrases.findPhraseByKeywords(startUserWords, null, null);
            if (foundPhrases != null && foundPhrases.length > 0) {
                for (Phrase p : foundPhrases) {
                    p = checkPhraseShowable(p, session);
                    if (p == null) continue;
                    return doPhraseAction(p, session, null);
                }
                logger.LogError(session.connId, session.sessionId, new RuntimeException(foundPhrases.length + " start variants found, no one able to show now. Using first."));
                return doPhraseAction(foundPhrases[0], session, null);
            }
        }
        return null;
    }

}