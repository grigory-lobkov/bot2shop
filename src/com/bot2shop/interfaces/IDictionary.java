package com.bot2shop.interfaces;

import com.bot2shop.model.Phrase;
import com.bot2shop.model.Topic;

import java.util.List;

/*
 *   Returns dictionary data set
 */


public interface IDictionary<KeyWordType> {

    // returns list of phrases from storage
    List<Phrase<KeyWordType>> getPhraseList();

    // returns list of topics from storage
    List<Topic> getTopicList();

    // logger
    void setLogger(ILogger logger);

    // do actions, prepare connection/data
    void process();

}