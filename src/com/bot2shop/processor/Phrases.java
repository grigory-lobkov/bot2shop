package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;

import java.util.Hashtable;
import java.util.List;

/*
* Bot knowledge base
* */

public class Phrases<KeyWordType> {

    class KeyWordLink {
        int weight;
        Phrase<KeyWordType> phrase;
    }

    private Hashtable<Integer, Phrase<KeyWordType>> phrases;
    private Hashtable<KeyWordType, List<KeyWordLink>> keyWords;

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    // each phrase keyword preparator
    private IPreparator preparator;
    public void setPreparator(IPreparator preparator) { this.preparator = preparator; }

    // access to data
    private IDictionary dictionary;
    public void setDictionary(IDictionary dictionary) { this.dictionary = dictionary; }

    public void processDictionary() {
        //TODO: load data from dictionary

    }

}
