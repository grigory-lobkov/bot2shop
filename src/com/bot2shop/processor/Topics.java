package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.model.Topic;

import java.util.List;
import java.util.Map;

public class Topics<KeyWordType> {

    private List<Topic> topicList;
    private Map<String, Topic> topicByShortName;

    // access to data
    private IDictionary<KeyWordType> dictionary;

    public void setDictionary(IDictionary<KeyWordType> dictionary) {
        this.dictionary = dictionary;
        processDictionary();
    }

    public void processDictionary() {
        topicList = dictionary.getTopicList();
    }

}
