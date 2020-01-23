package com.bot2shop.ext;

import com.bot2shop.interfaces.IExtConnection;

import java.util.Scanner;

public class ConsoleChat implements IExtConnection {

    public void setup(String[] params) { } // no need to set up
    public void incomeText(String sessionId, String inText) { } // will be overridden
    public void LogError(String sessionId, String errorText) { } // will be overridden

    // Integer identifier of connection
    private int id;
    public void setConnId(int id) {
        this.id = id;
    }
    public int getConnId() {
        return id;
    }

    // Send text message to session
    public boolean sendText(String sessionId, String textMessage) {
        System.out.println("> " + textMessage);
        return true;
    }

    // Register to external server, awaiting for users
    public void start() {
        String input;
        Scanner in = new Scanner(System.in);
        do {
            input = in.nextLine();
            incomeText("Console", input);
        } while (input.compareTo("/exit") != 0);
        in.close();
    }
}
