package com.bot2shop.model;

public class Word<UserWordType> {

    public enum GoesAfter {AFTERPREVIOUS, AFTERROOM, AFTERALL}
    public enum Action {SAY, ADDTOCART, SAVEINFO}
    public enum Room {HELLO, ORDER, ORDERITEM, CONTACTS, FINISH}

    public UserWord<UserWordType>[] userWords; // UserWord's, lead to Action
    public Word<UserWordType>[] nextWords; // Word's expected after this Word

    public GoesAfter goesAfter = GoesAfter.AFTERALL; // after what we are expecting this Word
    public int chance; // chance to get it

    public Action action = null; // this word means this Action
    public String sayText = null; // Action=SAY, what to say
    public ShopItem addtocartItem = null; // Action=ADDTOCART, item to add
    public String saveinfoType = null; // Action=SAVEINFO, what we are saving

    public Room room = null; // speaking room
    //public Room roomStart = null; // speaking room, which starts from this Word
    public boolean isRoomStart = false; // this Word is a starter of this Room
    public Room roomNext = null; // speaking room, if this Word found

    public int timeoutSec = -1; // how much seconds passed to timeout
    public Word timeoutWord = null; // where to go after timeout

}