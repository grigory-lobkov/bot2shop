package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
        this.phrases = new Hashtable<Integer, Phrase<KeyWordType>>();
        keyWordsTbl = new Hashtable<KeyWordType, List<KeyWordLink>>();
        List<Phrase<KeyWordType>> phrases = dictionary.getRawPhraseList();

        // iteration 1
        for (Phrase<KeyWordType> phrase:phrases) {
            // prepare phrases hashtable
            this.phrases.put(phrase.id, phrase);
            // prepare key words
            phrase.keyWordsTbl = new Hashtable<KeyWordType, Float>();
            for (String key:phrase.keyWords) {
                // prepare keyWordsTbl hashtable
                KeyWordType keyWord = preparator.prepareKey(key);
                float keyWordWeight = preparator.getBaseKeywordWeight(keyWord);
                phrase.keyWordsTbl.put(keyWord, keyWordWeight);
                // prepare keyWordsTbl hashtable
                KeyWordLink kwl = new KeyWordLink(phrase, keyWordWeight);
                List<KeyWordLink> kwlList = keyWordsTbl.get(keyWord);
                if(kwlList == null) {
                    kwlList = new ArrayList<KeyWordLink>();
                    keyWordsTbl.put(keyWord, kwlList);
                }
                kwlList.add(kwl);
            }
        }
    }

    public Phrase findPhraseByKeywords(KeyWordType[] srchWords) {
        return null; //TODO
    }

}
