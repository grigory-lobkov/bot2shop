package com.bot2shop.model;
import java.util.Hashtable;

/*
     Phrases, we send to user
*/

public class Phrase<KeyWordType> {

    public enum GoesAfter {AFTERPREVIOUS, AFTERROOM, AFTERALL}

    public enum Action {SAY, ADDTOCART, SAVEINFO}

    public enum Room {HELLO, ORDER, ORDERITEM, CONTACTS, FINISH}

    private static int nextId = 0; // autoincrement next identifier of Phrase for Hashtable
    private final int id = nextId++; // autoincrement identifier of Phrase for Hashtable

    public Hashtable<KeyWordType, Integer> keyWords; // key words, lead to Action
    public Phrase[] nextPhrases; // Phrase's expected after this Phrase

    public GoesAfter goesAfter = GoesAfter.AFTERALL; // after what we are expecting this Phrase
    public int chance; // chance to get it

    public Action action = null; // this word means this Action
    public String sayText = null; // Action=SAY, what to say
    public ShopItem addtocartItem = null; // Action=ADDTOCART, item to add
    public String saveinfoType = null; // Action=SAVEINFO, what we are saving

    public Room room = null; // speaking room
    //public Room roomStart = null; // speaking room, which starts from this Phrase
    public boolean isRoomStart = false; // this Phrase is a starter of this Room
    public Room roomNext = null; // speaking room, if this Phrase found

    public Hashtable<Integer, Phrase> afterPhrases = null; // after what Phrases this goes

    public int timeoutSec = -1; // how much seconds passed to timeout
    public Phrase timeoutPhrase = null; // where to go after timeout

    @Override
    public boolean equals(Object o) { // for Hashtable
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id == ((Phrase) o).id;
    }

    @Override
    public int hashCode() { // for Hashtable
        return id;
    }
}