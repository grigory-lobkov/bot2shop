package com.bot2shop.storage;

import com.bot2shop.interfaces.ILogger;

/*
 *   Put everything to console,
 *   using System.out
 */


public class ConsoleLogger implements ILogger {

    // logging
    public void Log(int connId, String sessId, String text) {
        System.out.println("log: connId=" + connId + " sessId=" + sessId + " " + text);
    }

    // logging income messages
    public void LogIncome(int connId, String sessId, String text) {
        Log(connId, sessId, "IN: " + text);
    }

    // logging outcome messages
    public void LogOutcome(int connId, String sessId, String text) {
        Log(connId, sessId, "OUT: " + text);
    }

    // logging error messages
    public void LogError(int connId, String sessId, Exception exception) {
        StringBuffer sb = new StringBuffer(1000);
        StackTraceElement[] st = exception.getStackTrace();
        sb.append(exception.getClass().getName() + ": " + exception.getMessage() + "\n");
        for (int i = 0; i < st.length; i++) {
            sb.append("\t at " + st[i].toString() + "\n");
        }
        Log(connId, sessId, "ERROR: " + sb.toString());
    }


}