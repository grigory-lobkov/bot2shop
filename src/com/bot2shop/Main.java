package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.model.Session;
import com.bot2shop.processor.*;
import com.bot2shop.storage.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

class Main {

    static private ILogger logger = new ConsoleLogger(); // log anything
    static private IncomeText incomeTextProcessor = new IncomeText(); // processor for incoming messages
    static private Sessions sessions = new Sessions(); // Session list
    static private Connections connections = new Connections(); // Connection list

    // Start of the BOT
    public static void main(String[] args) {
        incomeTextProcessor.setLogger(logger);
        sessions.setLogger(logger);
        connections.setLogger(logger);
        connections.setSessions(sessions);
        connections.setIncomeTextProcessor(incomeTextProcessor);
        // Proxy if needed (for Tor browser in memory):
        //System.getProperties().put("proxySet", "true");
        //System.getProperties().put("socksProxyHost", "127.0.0.1");
        //System.getProperties().put("socksProxyPort", "9150");
        //connections.add(new Telegram(), "Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE");
        connections.add(new ConsoleChat());
        connections.startAll();

        //IExtConnection bot;
        /*bot = new Telegram() {
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
        /*bot = new ConsoleChat() {
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
        bot.start();*/
    }

}
