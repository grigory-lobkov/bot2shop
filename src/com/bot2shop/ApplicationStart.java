package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.prepare.*;
import com.bot2shop.processor.*;
import com.bot2shop.storage.*;

/*
 *   Link dependencies, start program
 */


class ApplicationStart<KeyWordType> {

    private ILogger logger = new ConsoleLogger(); // log anything
    private MakeAction<KeyWordType> makeActionProcessor = new MakeAction<KeyWordType>(); // processor for incoming messages
    private Sessions<KeyWordType> sessions = new Sessions<KeyWordType>(); // Session list
    private Connections<KeyWordType> connections = new Connections<KeyWordType>(); // Connection list
    private IDictionary<KeyWordType> dictionary = new XmlParseDictionary<KeyWordType>(); // Connection to dictionary
    private Topics<KeyWordType> topics = new Topics<KeyWordType>(); // Bot knowledge base, topics
    private Phrases<KeyWordType> phrases = new Phrases<KeyWordType>(); // Bot knowledge base, phrases
    private IPreparator<KeyWordType> preparator = new LowCasePreparator(); // User words and phrases keyword preparator

    // Start of the BOT
    public void start() {
        dictionary.setLogger(logger);
        dictionary.process();
        topics.setDictionary(dictionary);
        phrases.setLogger(logger);
        phrases.setPreparator(preparator);
        phrases.setTopics(topics);
        phrases.setDictionary(dictionary);
        makeActionProcessor.setLogger(logger);
        makeActionProcessor.setPreparator(preparator);
        makeActionProcessor.setPhrases(phrases);
        sessions.setLogger(logger);
        connections.setLogger(logger);
        connections.setSessions(sessions);
        connections.setIncomeTextProcessor(makeActionProcessor);
        // Proxy if needed (for Tor browser in memory):
//        System.getProperties().put("proxySet", "true");
//        System.getProperties().put("socksProxyHost", "127.0.0.1");
//        System.getProperties().put("socksProxyPort", "9150");
//        org.telegram.telegrambots.ApiContextInitializer.init(); // telegram specific
//        connections.add(new Telegram(), "Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE");
        connections.add(new ConsoleChat()); // for debug
        connections.startAll();
    }

    public static void main(String[] args) {
        new ApplicationStart<String>().start();
    }

}