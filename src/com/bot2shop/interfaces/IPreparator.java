package com.bot2shop.interfaces;

/*
 *   Interface for Prepare words and phrases for a further work
 *   Implementations package: com.bot2shop.prepare
 */


public interface IPreparator<KeyWordType> {

    // Preparing key words
    KeyWordType prepareKey(String word);

    // Determine keyword base weight
    float getBaseKeywordWeight(KeyWordType keyWord);

    // Preparing user input
    KeyWordType[] prepareInput(String string);

}