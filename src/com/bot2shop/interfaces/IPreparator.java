package com.bot2shop.interfaces;

/*
 *   Interface for Prepare words and phrases for a further work
 *   Implementations package: com.bot2shop.prepare
 */

public interface IPreparator<KeyWordType> {

    // Preparing key words
    KeyWordType prepareKey(String word);

    // Preparing user input
    KeyWordType[] prepareInput(String string);

}
