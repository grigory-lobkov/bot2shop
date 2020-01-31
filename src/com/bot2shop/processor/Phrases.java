package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;

/*
* Bot knowledge base
* */

public class Phrases {

    private ILogger logger;
    public void setLogger(ILogger logger) { this.logger = logger; }

    private IDictionary dictionary;
    public void setDictionary(IDictionary dictionary) { this.dictionary = dictionary; }

    public void process() {
        //TODO: load data from dictionary
    }

}
