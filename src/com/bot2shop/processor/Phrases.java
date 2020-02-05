package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/*
* Bot knowledge base
* */

public class Phrases<KeyWordType> {

    class KeyWordLink {
        Phrase<KeyWordType> phrase;
        float weight;
        public KeyWordLink(Phrase<KeyWordType> phrase,float weight) {
            this.phrase = phrase;
            this.weight = weight;
        }
    }

    private Hashtable<Integer, Phrase<KeyWordType>> phrases;
    private Hashtable<KeyWordType, List<KeyWordLink>> keyWordsTbl;
    private Hashtable<Phrase.Room, List<Phrase>> roomStartPhrases;

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // each phrase keyword preparator
    private IPreparator<KeyWordType> preparator;
    public void setPreparator(IPreparator<KeyWordType> preparator) { this.preparator = preparator; }

    // access to data
    private IDictionary<KeyWordType> dictionary;
    public void setDictionary(IDictionary<KeyWordType> dictionary) { this.dictionary = dictionary; }

    // load and process phrases
    public void processDictionary() {
        phrases = new Hashtable<Integer, Phrase<KeyWordType>>();
        keyWordsTbl = new Hashtable<KeyWordType, List<KeyWordLink>>();
        List<Phrase<KeyWordType>> phraseList = dictionary.getRawPhraseList();

        // iteration 1
        for (Phrase<KeyWordType> phrase : phraseList) {
            // prepare phrases hashtable
            phrases.put(phrase.id, phrase);
            // prepare key words
            phrase.keyWordsTbl = new Hashtable<KeyWordType, Float>();
            for (String key : phrase.keyWords) {
                // prepare keyWordsTbl hashtable
                KeyWordType keyWord = preparator.prepareKey(key);
                float keyWordWeight = preparator.getBaseKeywordWeight(keyWord);
                phrase.keyWordsTbl.put(keyWord, keyWordWeight);
                // prepare keyWordsTbl hashtable
                KeyWordLink kwl = new KeyWordLink(phrase, keyWordWeight);
                List<KeyWordLink> kwlList = keyWordsTbl.get(keyWord);
                if (kwlList == null) {
                    kwlList = new ArrayList<KeyWordLink>();
                    keyWordsTbl.put(keyWord, kwlList);
                }
                kwlList.add(kwl);
            }
        }
        // iteration 2
        for (Phrase<KeyWordType> phrase : phraseList) {
            // prepare nextPhrases, afterPhrases
            if (phrase.nextPhrasesId.length > 0) {
                HashSet<Phrase<KeyWordType>> added = new HashSet<Phrase<KeyWordType>>();
                for (int id : phrase.nextPhrasesId) {
                    Phrase<KeyWordType> phrase2 = phrases.get(id);
                    if (phrase2 == null) continue;
                    phrase2.afterPhrases.add(phrase2);
                    added.add(phrase2);
                }
                phrase.nextPhrases = (Phrase<KeyWordType>[]) added.toArray();
            } else if (phrase.nextPhrases.length > 0) {
                HashSet<Integer> added = new HashSet<Integer>();
                for (Phrase<KeyWordType> phrase2 : phrase.nextPhrases) {
                    phrase2.afterPhrases.add(phrase2);
                    added.add(phrase2.id);
                }
                phrase.nextPhrasesId = (Integer[]) added.toArray();
            }
        }
    }

    // calculate basic weight of phrase
    public float getPhraseBasicWeight(Phrase<KeyWordType> phrase, Phrase.Room lastRoom, Phrase<KeyWordType> lastPhrase) {
        if (phrase.goesAfter == Phrase.GoesAfter.AFTERPREVIOUS) {
            return lastPhrase != null && phrase.afterPhrases.contains(lastPhrase) ? 6f : 0f;
        }
        float weight = 1f;
        if (lastPhrase != null) {
            if (phrase.afterPhrases.contains(lastPhrase)) weight *= 3f;
        }
        if (phrase.goesAfter == Phrase.GoesAfter.AFTERROOM) {
            return phrase.room == lastRoom ? weight * 2f : 0;
        }
        if (phrase.room == lastRoom) weight *= 2f;

        return weight;
    }

    // find out, which phrase is the most suitable
    public Phrase<KeyWordType> findPhraseByKeywords(KeyWordType[] srchWords, Phrase.Room lastRoom, Phrase<KeyWordType> lastPhrase) {
        Hashtable<Phrase<KeyWordType>, Float> foundPhrases = new Hashtable<Phrase<KeyWordType>, Float>();

        // calculate weights
        for (KeyWordType srchWord: srchWords) {
            List<KeyWordLink> kwlList = keyWordsTbl.get(srchWord);
            if (kwlList != null) {
                for (KeyWordLink kwl : kwlList) {
                    Float weight = foundPhrases.get(kwl.phrase);
                    if(weight == null) {
                        weight = getPhraseBasicWeight(kwl.phrase, lastRoom, lastPhrase);
                    }
                    weight = weight * kwl.weight;
                    foundPhrases.put(kwl.phrase, weight);
                }
            }
        }

        // find max weight
        Float maxWeight = 0f;
        Phrase<KeyWordType> maxWPhrase = null;
        for(Map.Entry<Phrase<KeyWordType>, Float> entry : foundPhrases.entrySet()) {
            Float weight = entry.getValue();
            if(weight > maxWeight) {
                maxWeight = weight;
                maxWPhrase = entry.getKey();
            }
        }

        return maxWPhrase;
    }
/*
    // find out, which phrase is the most suitable, without keywords
    public Phrase<KeyWordType> findPhraseByKeywords(Phrase.Room lastRoom, Phrase<KeyWordType> lastPhrase) {
        Hashtable<Phrase<KeyWordType>, Float> foundPhrases = new Hashtable<Phrase<KeyWordType>, Float>();

        // calculate weights
        for (: ) {
        }

        // find max weight
        Float maxWeight = 0f;
        Phrase<KeyWordType> maxWPhrase = null;
        for(Map.Entry<Phrase<KeyWordType>, Float> entry : foundPhrases.entrySet()) {
            Float weight = entry.getValue();
            if(weight > maxWeight) {
                maxWeight = weight;
                maxWPhrase = entry.getKey();
            }
        }

        return maxWPhrase;
    }
*/
}
