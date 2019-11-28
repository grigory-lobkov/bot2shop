package com.bot2shop.model;

public class Word<UserWordType> {
    Rooms.Room roomStart = null;
    Rooms.Room roomNext = null;
    UserWord<UserWordType>[] words;
}