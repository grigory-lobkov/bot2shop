package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.model.Session;
import com.bot2shop.processor.IncomeText;
import com.bot2shop.storage.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

class Main {

    static private List<IExtConnection> connections = new ArrayList<>(); // list of bots (connections to different messengers)
    static private ILogger logger = new ConsoleLogger(); // log anything
    static private List<Session> sessions = new ArrayList<>(); // users sessions
    static private Hashtable<String, Integer> sessionsIds = new Hashtable<>(); // users sessions identifiers hashtable
    static private IncomeText incomeTextProcessor = new IncomeText(); // processor for incoming messages

    // Start of the BOT
    public static void main(String[] args) {
        IExtConnection bot;
        /*
        // Proxy if needed (for Tor browser in memory):
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "127.0.0.1");
        System.getProperties().put("socksProxyPort", "9150");

        bot = new Telegram() {
            @Override
            public void incomeText(String sessionId, String inText) {
                Main.incomeText(this.getConnId(), sessionId, inText);
            }
            @Override
            public void LogError(String sessionId, String errorText) {
                logger.LogError(this.getConnId(), sessionId, errorText);
            }
        };
        bot.setConnId(connections.size());
        connections.add(bot);
        bot.setup(new String[]{"Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE"});
        bot.start(); // TODO: How to start multiple bots ?
        */
        bot = new ConsoleChat() {
            @Override
            public void incomeText(String sessionId, String inText) {
                Main.incomeText(this.getConnId(), sessionId, inText);
            }
            @Override
            public void LogError(String sessionId, String errorText) {
                logger.LogError(this.getConnId(), sessionId, errorText);
            }
        };
        bot.setConnId(connections.size());
        connections.add(bot);
        bot.setup(null);
        bot.start();
    }

    private static Session getSession(int connId, String sessionId) {
        // check if exists
        String hashKey = connId+"-"+sessionId;
        Integer sessKey = sessionsIds.get(hashKey);
        if(sessKey!=null) return sessions.get(sessKey).refresh();
        // new session
        logger.Log(connId, sessionId, "Start session");
        Session newSession = new Session(connId, sessionId, connections.get(connId));
        sessKey = sessions.size();
        sessionsIds.put(hashKey, sessKey);
        sessions.add(newSession);
        return newSession.refresh();
    }

    /** @noinspection SameParameterValue*/
    // receives message and returns an answer
    // sessionId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back
    private static void incomeText(int connId, String sessionId, String inText) {
        logger.LogIncome(connId, sessionId, inText);
        //try {
            Session session = getSession(connId, sessionId);
            incomeTextProcessor.processMessage(session, inText);
        //} catch (Exception e) {
        //    logger.LogError(connId, sessionId, "incomeText "+e);
        //}
    }

    // for development purpose
    private static void test() {
        String input;
        Scanner in = new Scanner(System.in);
        do {
            input = in.nextLine();
            incomeText(0, "2", input);
        } while (input.compareTo("/exit") != 0);
        in.close();
    }
}
