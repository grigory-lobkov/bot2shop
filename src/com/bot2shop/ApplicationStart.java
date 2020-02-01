package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.prepare.*;
import com.bot2shop.processor.*;
import com.bot2shop.storage.*;

class ApplicationStart {

    static private ILogger logger = new ConsoleLogger(); // log anything
    static private IncomeText<String> incomeTextProcessor = new IncomeText<String>(); // processor for incoming messages
    static private Sessions<String> sessions = new Sessions<String>(); // Session list
    static private Connections<String> connections = new Connections<String>(); // Connection list
    static private IDictionary dictionary = new InlineDictionary(); // Connection to dictionary
    static private Phrases<String> phrases = new Phrases<String>(); // Bot knowledge base, <KeyWordType>
    static private IPreparator<String> preparator = new LowCasePreparator(); // User words and phrases keyword preparator, <KeyWordType>

    // Start of the BOT
    public static void main(String[] args) {
        phrases.setLogger(logger);
        phrases.setPreparator(preparator);
        phrases.setDictionary(dictionary);
        phrases.processDictionary();
        incomeTextProcessor.setLogger(logger);
        incomeTextProcessor.setPhrases(phrases);
        incomeTextProcessor.setPreparator(preparator);
        sessions.setLogger(logger);
        connections.setLogger(logger);
        connections.setSessions(sessions);
        connections.setIncomeTextProcessor(incomeTextProcessor);
        // Proxy if needed (for Tor browser in memory):
//        System.getProperties().put("proxySet", "true");
//        System.getProperties().put("socksProxyHost", "127.0.0.1");
//        System.getProperties().put("socksProxyPort", "9150");
//        org.telegram.telegrambots.ApiContextInitializer.init(); // telegram specific
//        connections.add(new Telegram(), "Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE");
        connections.add(new ConsoleChat());
        connections.startAll();
    }

}
