package com.bot2shop.storage;

import com.bot2shop.interfaces.ILogger;

public class ConsoleLogger implements ILogger {

    // logging
    public void Log(int connId, String sessId, String text) {
        System.out.println("connId=" + connId + " sessId=" + sessId + " " + text);
    }

    // logging income messages
    public void LogIncome(int connId, String sessId, String text) {
        Log(connId, sessId, "IN: " + text);
    }

    // logging outcome messages
    public void LogOutcome(int connId, String sessId, String text){
        Log(connId, sessId, "OUT: " + text);
    }

    // logging error messages
    public void LogError(int connId, String sessId, String text) {
        Log(connId, sessId, "ERROR: " + text);
    }


}