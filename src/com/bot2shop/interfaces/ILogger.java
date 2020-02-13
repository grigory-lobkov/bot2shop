package com.bot2shop.interfaces;

/*
 *   Logging of different actions
 */


public interface ILogger {

    // logging
    void Log(int connId, String sessId, String text);

    // logging income messages
    void LogIncome(int connId, String sessId, String text);

    // logging outcome messages
    void LogOutcome(int connId, String sessId, String text);

    // logging error messages
    void LogError(int connId, String sessId, Exception exception);

}