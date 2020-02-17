package com.bot2shop.processor;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.interfaces.IPreparator;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Topic;

import java.util.*;

import static com.bot2shop.model.Config.*;

/*
 *   Bot knowledge base, search implementation
 */


public class Phrases<KeyWordType> {

    class KeyWordLink {
        Phrase<KeyWordType> phrase;
        float weight;

        public KeyWordLink(Phrase<KeyWordType> phrase, float weight) {
            this.phrase = phrase;
            this.weight = weight;
        }
    }

    private Map<Integer, Phrase<KeyWordType>> phrases;
    private Map<KeyWordType, List<KeyWordLink>> keyWordsTbl;
    private Map<Topic, List<Phrase<KeyWordType>>> topicStartPhrases;
    private Map<Phrase<KeyWordType>, List<Phrase<KeyWordType>>> lastPhrasePhrases;

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    // each phrase keyword preparator
    private IPreparator<KeyWordType> preparator;
    public void setPreparator(IPreparator<KeyWordType> preparator) {
        this.preparator = preparator;
    }

    // access to data
    private IDictionary<KeyWordType> dictionary;

    public void setDictionary(IDictionary<KeyWordType> dictionary) {
        this.dictionary = dictionary;
        processDictionary();
    }

    // topics
    private Topics topics;

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    // load and process phrases
    public void processDictionary() {
        phrases = new Hashtable<Integer, Phrase<KeyWordType>>();
        keyWordsTbl = new Hashtable<KeyWordType, List<KeyWordLink>>();
        List<Phrase<KeyWordType>> phraseList = dictionary.getPhraseList();
        topicStartPhrases = new Hashtable<Topic, List<Phrase<KeyWordType>>>();
        lastPhrasePhrases = new Hashtable<Phrase<KeyWordType>, List<Phrase<KeyWordType>>>();


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

            // prepare topicStartPhrases hashtable
//            for (Topic topic : phrase.unknownForTopics) {
////                List<Phrase<KeyWordType>> topicList = topicStartPhrases.get(topic);
////                if (topicList == null) {
////                    topicList = new ArrayList<Phrase<KeyWordType>>();
////                    topicStartPhrases.put(topic, topicList);
////                }
////                topicList.add(phrase);
////            }
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
            }/* else if (phrase.nextPhrases.length > 0) {
                HashSet<Integer> added = new HashSet<Integer>();
                for (Phrase<KeyWordType> phrase2 : phrase.nextPhrases) {
                    phrase2.afterPhrases.add(phrase2);
                    added.add(phrase2.id);
                }
                phrase.nextPhrasesId = (Integer[]) added.toArray();
            }*/

            // prepare lastPhrasePhrases hashtable
            if (phrase.nextPhrasesIfUnknownId.length > 0) {
                int len = phrase.nextPhrasesIfUnknownId.length;
                Phrase<KeyWordType>[] phs = (Phrase<KeyWordType>[]) new Object[len];
                for (int i = 0; i < len; i++) {
                    phs[i] = phrases.get(phrase.nextPhrasesIfUnknownId[i]);
                }
                phrase.nextPhrasesIfUnknown = phs;
            }
        }
    }

    // calculate basic weight of phrase
    public float getPhraseBasicWeight(Phrase<KeyWordType> phrase, Topic lastTopic, Phrase<KeyWordType> lastPhrase) {
        if (phrase.goesAfter == Phrase.GoesAfter.AFTERPREVIOUS) {
            return lastPhrase != null && phrase.afterPhrases.contains(lastPhrase) ? FOLLOW_EXCLUSIVE_PHRASE_COST : 0f;
        }
        float weight = 1f;
        if (lastPhrase != null) {
            if (phrase.afterPhrases.contains(lastPhrase)) weight *= FOLLOW_PHRASE_COST;
        }
        if (phrase.goesAfter == Phrase.GoesAfter.AFTERTOPIC) {
            return phrase.topic == lastTopic ? weight * FOLLOW_EXCLUSIVE_TOPIC_COST : 0;
        }
        if (phrase.topic == lastTopic) weight *= FOLLOW_TOPIC_COST;

        return weight;
    }

    // find phrase in hashMap with maximum Float weight as value
    Phrase<KeyWordType> findPhraseHashMaxWeight(Map<Phrase<KeyWordType>, Float> phrasesMap) {
        Float maxWeight = 0f;
        Phrase<KeyWordType> maxWPhrase = null;
        for (Map.Entry<Phrase<KeyWordType>, Float> entry : phrasesMap.entrySet()) {
            Float weight = entry.getValue();
            if (weight > maxWeight) {
                maxWeight = weight;
                maxWPhrase = entry.getKey();
            }
        }
        return maxWPhrase;
    }

    // find phrase in hashMap, sorted from maximum to minimum weight as value
    Phrase<KeyWordType>[] findPhraseOrdered(Map<Phrase<KeyWordType>, Float> phrasesMap) {

        // create temporary list
        List<Map.Entry<Phrase<KeyWordType>, Float>> entries = new ArrayList<Map.Entry<Phrase<KeyWordType>, Float>>(
                phrasesMap.entrySet()
        );

        // sort list
        Collections.sort(entries, new Comparator<Map.Entry<Phrase<KeyWordType>, Float>>() {
            public int compare(Map.Entry<Phrase<KeyWordType>, Float> a, Map.Entry<Phrase<KeyWordType>, Float> b) {
                return -Float.compare(b.getValue(), a.getValue());
            }
        });

        // generate array
        int i = 0;
        Phrase<KeyWordType>[] result = new Phrase[entries.size()];
        for (Map.Entry<Phrase<KeyWordType>, Float> e : entries) {
            result[i++] = e.getKey();
        }
        return result;
    }

    // find out, which phrase is the most suitable, using keywords
    public Phrase<KeyWordType>[] findPhraseByKeywords(KeyWordType[] srchWords, Topic lastTopic, Phrase<KeyWordType> lastPhrase) {
        Map<Phrase<KeyWordType>, Float> foundPhrases = new Hashtable<Phrase<KeyWordType>, Float>();

        // calculate weights
        for (KeyWordType srchWord : srchWords) {
            List<KeyWordLink> kwlList = keyWordsTbl.get(srchWord);
            if (kwlList != null) {
                for (KeyWordLink kwl : kwlList) {
                    if (kwl.phrase.goesAfter == Phrase.GoesAfter.AFTERPREVIOUSSTRICT) {
                        if (!kwl.phrase.afterPhrases.contains(lastPhrase)) continue;
                    } else if (kwl.phrase.goesAfter == Phrase.GoesAfter.AFTERTOPICSTRICT) {
                        if (kwl.phrase.topic != lastTopic) continue;
                    }
                    Float weight = foundPhrases.get(kwl.phrase);
                    if (weight == null) {
                        weight = getPhraseBasicWeight(kwl.phrase, lastTopic, lastPhrase);
                    }
                    weight = weight * kwl.weight;
                    foundPhrases.put(kwl.phrase, weight);
                }
            }
        }

        // TODO: add cache for the same existing keywords, lastTopic and lastPhrase

        // sort by weight and return array
        return findPhraseOrdered(foundPhrases);
    }

    // find out, which phrase is the most suitable, using previous topic and phrase
    public Phrase<KeyWordType>[] findPhraseByLast(Topic lastTopic, Phrase<KeyWordType> lastPhrase) {
        Hashtable<Phrase<KeyWordType>, Float> foundPhrases = new Hashtable<Phrase<KeyWordType>, Float>();

        // calculate after-phrase weights
        if (lastPhrase != null && lastPhrase.nextPhrasesIfUnknown != null) {
            for (Phrase phrase : lastPhrase.nextPhrasesIfUnknown) {
                Float weight = foundPhrases.get(phrase);
                weight = weight == null ? FOLLOW_EXCLUSIVE_PHRASE_COST : weight * FOLLOW_EXCLUSIVE_PHRASE_COST;
                foundPhrases.put(phrase, weight);
            }
        }

        // calculate after-topic weights
        if (lastTopic != null) {
            List<Phrase<KeyWordType>> phrases = topicStartPhrases.get(lastTopic);
            if (phrases != null) {
                for (Phrase phrase : phrases) {
                    Float weight = foundPhrases.get(phrase);
                    weight = weight == null ? FOLLOW_EXCLUSIVE_TOPIC_COST : weight * FOLLOW_EXCLUSIVE_TOPIC_COST;
                    foundPhrases.put(phrase, weight);
                }
            }
        }

        // sort by weight and return array
        return findPhraseOrdered(foundPhrases);
    }

    // find out, which phrase is the most suitable
//    public Phrase<KeyWordType> findPhrase(KeyWordType[] srchWords, Topic lastTopic, Phrase<KeyWordType> lastPhrase) {
//        // search by keywords
//        Phrase phrase = findPhraseByKeywords(srchWords, lastTopic, lastPhrase);
//        if (phrase != null) {
//            return phrase;
//        }
//        // search by previous state
//        phrase = findPhraseByLast(lastTopic, lastPhrase);
//        if (phrase != null) {
//            return phrase;
//        }
//        return null;
//    }

}