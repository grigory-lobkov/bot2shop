package com.bot2shop.interfaces;

/*
 *   Interface for Prepare words and phrases for a further work
 *   Implementations package: com.bot2shop.prepare
 */

public interface IPreparator<KeyTo, InpTo> {

    // Preparing key words
    KeyTo prepareKey(String word);

    // Preparing user input
    InpTo prepareInput(String string);

}
