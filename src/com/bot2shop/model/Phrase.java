package com.bot2shop.model;

import com.bot2shop.processor.Phrases;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 *  Phrases, we able to recognize
 */


public class Phrase<KeyWordType> {

    public enum GoesAfter {AFTERPREVIOUSSTRICT, AFTERPREVIOUS, AFTERTOPICSTRICT, AFTERTOPIC, AFTERALL}

    public enum Action {SAY, OBSERVETITLES, ADDTOCART, SAVEINFO, ENDSESSION}

    private static int nextId = 0; // autoincrement next identifier of Phrase for Hashtable
    public Integer id = nextId++; // autoincrement identifier of Phrase for Hashtable


    // user provided values

    public String[] keyWords = {}; // key words, lead to Action
    public Integer[] nextPhrasesId = {}; // Phrase's expected after this Phrase
    public boolean strictQuestion = false; // allows only after-phrase Phrases, required to set nextPhrasesIfUnknownId to some children
    public boolean canBeLast = true; // if False, this phrase will not override lastPhrase

    public GoesAfter goesAfter = GoesAfter.AFTERALL; // after what we are expecting this Phrase
    public Action action = Action.SAY; // this word means this Action

    public String title = null; // Short description to show user options
    public String sayText = null; // Action=SAY, what to say
    //public ShopItem addtocartItem = null; // Action=ADDTOCART, item to add
    //public String saveinfoType = null; // Action=SAVEINFO, what we are saving

    public String topicShortName = ""; // speaking topic name
    //public boolean isTopicStart = false; // this Phrase is a starter of this Topic

    public boolean isTopicUnknown; // this Phrase is a Topic unknown phrase
    public Integer[] nextPhrasesIfUnknownId = {}; // id of Phrase if unknown

    public int timeoutSec = -1; // how much seconds passed to timeout // TODO: implement some watchdog
    public boolean showOnlyOnce = false; // show this phrase only once in session
    public int showChance = 100; // show this phrase in certain probability (1-100)


    // for internal purpose

    public Map<KeyWordType, Float> keyWordsTbl; // key words with weight as value, lead to Action
    public Phrase<KeyWordType>[] nextPhrases; // Phrase's expected after this Phrase
    public Topic topic = null; // speaking topic
    public Set<Phrase<KeyWordType>> afterPhrases = new HashSet<Phrase<KeyWordType>>(); // after what Phrases this goes
    public Phrase<KeyWordType>[] nextPhrasesIfUnknown; // next Phrase if unknown


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Phrase) o).id;
    } // for Hashtable

    public int hashCode() { // for Hashtable
        return id;
    } // for Hashtable

}