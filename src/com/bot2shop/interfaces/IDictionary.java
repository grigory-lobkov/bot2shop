package com.bot2shop.interfaces;

/*
* Returns dictionary data set
* */

import com.bot2shop.model.Phrase;

import java.util.List;

public interface IDictionary<KeyWordType> {

    // returns list of phrases from storage
    List<Phrase<KeyWordType>> getRawPhraseList();

}
