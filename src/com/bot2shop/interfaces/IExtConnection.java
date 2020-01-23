package com.bot2shop.interfaces;

/*
*   Interface for External Connections to bot services
*   Implementations package: com.bot2shop.ext
*/


public interface IExtConnection {


    // Set base parameters for connection (api keys, tokens, passwords, etc)

    void setup(String[] params);

    // Function to @Override - receives message and returns an answer, if return value is set
    // sessId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back
    /** @noinspection unused*/
    String incomeText(String sessId, String inText);

    // Function to @Override - logs an errors
    void LogError(String sessId, String errorText);

    // Procedure to send text message, if success, returns true
    boolean sendText(String sessId, String textMessage);

    // Getter and setter of integer identifier of connection
    void setConnId(int id);
    int getConnId();

    // Register to external server, awaiting for users
    void start();


}
