package com.bot2shop.interfaces;

/*
 *   Interface for External Connections to bot services
 *   Implementations package: com.bot2shop.ext
 */


public interface IConnection {


    // Set base parameters for connection (api keys, tokens, passwords, etc)

    void setup(String... params);

    // Function to @Override - receives message
    // sessionId - session, we are working with
    // inText - incoming message from user
    //void incomeText(String sessionId, String inText);

    // Function to @Override - logs an errors
    //void LogError(String sessionId, String errorText);

    // Procedure to send text message, if success, returns true
    boolean sendText(String sessionId, String textMessage);

    // Send title variants to session, if success, returns true
    boolean sendTextVariants(String sessionId, String textMessage, String[] variants, String[] callbacks);

    // Getter and setter of integer identifier of connection
    void setConnId(int id);
    //int getConnId();

    // Set processor to log errors
    void setErrorProcessor(IProcessor<Exception> logErrorProcessor);

    // Set processor to process income user messages
    void setIncomeTextProcessor(IProcessor<String> incomeTextProcessor);

}