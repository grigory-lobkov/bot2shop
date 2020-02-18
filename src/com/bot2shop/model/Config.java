package com.bot2shop.model;

/*
 *  Configurable parameters, constants
 */


public class Config {

    public final static int DECIMALS_IN_COST = 2; // Quantity of decimals after the point (all costs is integers)

    // determines the cost of phrase
    public final static float FOLLOW_EXCLUSIVE_PHRASE_COST = 6f;  // constant if phrase.goesAfter == AFTERPREVIOUS & phrase.afterPhrases.contains(lastPhrase)
    public final static float FOLLOW_PHRASE_COST = 3f;            // multiply if phrase.goesAfter == AFTERTOPIC
    public final static float FOLLOW_EXCLUSIVE_TOPIC_COST = 2f;   // multiply if phrase.afterPhrases.contains(lastPhrase)
    public final static float FOLLOW_TOPIC_COST = 2f;             // multiply if phrase.goesAfter == Phrase.GoesAfter.AFTERTOPIC & phrase.topic == lastTopic
    public final static float KEYWORD_MULTIPLIER_COST = 1.1f;     // multiply on some conditions of preparator
    public final static float PHRASE_CUT_LIST_MULTIPLIER = 0.5f;  // multiply on some conditions of preparator

}