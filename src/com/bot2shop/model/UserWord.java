package com.bot2shop.model;

import com.bot2shop.interfaces.IUserWordPreparator;

public class UserWord<To> {

    String original;
    To prepared;

    UserWord(String userWord, IUserWordPreparator preparator) {
        this.original = userWord;
        this.prepared = (To) preparator.prepare(userWord);
    }

}