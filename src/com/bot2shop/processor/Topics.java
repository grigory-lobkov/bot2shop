package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.model.Topic;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Topics<KeyWordType> {

    private Map<Integer, Topic> topics;

    // access to data
    private IDictionary<KeyWordType> dictionary;

    public void setDictionary(IDictionary<KeyWordType> dictionary) {
        this.dictionary = dictionary;
        processDictionary();
    }

    // prepare class
    public void processDictionary() {
        List<Topic> topicList = dictionary.getTopicList();
        topics = new Hashtable<Integer, Topic>(topicList.size());

        for(Topic topic: topicList) {
            // prepare topics hashtable
            topics.put(topic.id, topic);
        }
    }

    // get topic by identifier
    public Topic getById(Integer id) {
        return topics.get(id);
    }

}
