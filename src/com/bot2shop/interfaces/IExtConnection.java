package com.bot2shop.interfaces;

/*
*   Interface for External Connections to bot services
*   Implementations package: com.bot2shop.ext
*/


public interface IExtConnection {


    // Set base parameters for connection (api keys, kokens, passwords, etc)

    void setup(String[] params);

    // Function to @Override - receives message and returns an answer
    // sessId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back

    String income(String sessId, String inText);

    // Register to external server, awaiting for users

    void start();


}
