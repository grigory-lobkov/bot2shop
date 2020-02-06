package com.bot2shop.model;

public class Config {

    public final static int DECIMALS_IN_COST = 2; // Quantity of decimals after the point (all costs is integers)

    // determines the cost of phrase
    public final static float FOLLOW_EXCLUSIVE_PHRASE_COST = 6f; // constant if phrase.goesAfter == AFTERPREVIOUS & phrase.afterPhrases.contains(lastPhrase)
    public final static float FOLLOW_PHRASE_COST = 3f;           // multiply if phrase.goesAfter == AFTERROOM
    public final static float FOLLOW_EXCLUSIVE_ROOM_COST = 2f;   // multiply if phrase.afterPhrases.contains(lastPhrase)
    public final static float FOLLOW_ROOM_COST = 2f;             // multiply if phrase.goesAfter == Phrase.GoesAfter.AFTERROOM & phrase.room == lastRoom
    public final static float KEYWORD_MULTIPLIER_COST = 1.1f;    // multiply on some conditions of preparator

}
