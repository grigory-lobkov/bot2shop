package com.bot2shop.ext;

import com.bot2shop.interfaces.IExtConnection;

import java.util.Scanner;

public class ConsoleChat implements IExtConnection {

    @Override
    public void setup(String[] params) {
    }

    @Override
    public void incomeText(String sessionId, String inText) { }

    @Override
    public void LogError(String sessionId, String errorText) {

    }

    @Override
    public boolean sendText(String sessionId, String textMessage) {
        System.out.println(textMessage);
        return true;
    }

    @Override
    public void setConnId(int id) {

    }

    @Override
    public int getConnId() {
        return 0;
    }

    @Override
    public void start() {

    }
}
