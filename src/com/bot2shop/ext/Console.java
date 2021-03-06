package com.bot2shop.ext;

/*
 *
 *   Test purpose console bot,
 *   using standard System.in
 *
 *
 */


import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.IProcessor;

import java.util.Scanner;

public class Console implements IConnection, Runnable {

    public void setup(String... params) {
    } // no need to set up

    // Send text message to session
    public boolean sendText(String sessionId, String textMessage) {
        System.out.println("> " + textMessage);
        return true;
    }

    // Send title variants to session
    public boolean sendTextVariants(String sessionId, String textMessage, String[] variants, String[] callbacks) {
        System.out.println("> " + textMessage);
        for (int i = 0; i < variants.length; i++) {
            System.out.println(callbacks[i] + " > " + variants[i]);
        }
        return true;
    }

    // Register to external server, awaiting for users
    public void run() {
        String input;
        Scanner in = new Scanner(System.in);
        do {
            input = in.nextLine();
            incomeTextProcessor.process(id, "Console", input);
        } while (input.compareTo("/exit") != 0);
        in.close();
    }

    // Integer identifier of connection
    private int id;

    public void setConnId(int id) {
        this.id = id;
    }
    //public int getConnId() { return id; }

    // Set processor to log errors
    private IProcessor<Exception> logErrorProcessor;
    public void setErrorProcessor(IProcessor<Exception> logErrorProcessor) {
        this.logErrorProcessor = logErrorProcessor;
    }

    // Set processor to process income user messages
    private IProcessor<String> incomeTextProcessor;
    public void setIncomeTextProcessor(IProcessor<String> incomeTextProcessor) {
        this.incomeTextProcessor = incomeTextProcessor;
    }

}