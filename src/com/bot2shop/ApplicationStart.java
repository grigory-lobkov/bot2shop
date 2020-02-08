package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.prepare.*;
import com.bot2shop.processor.*;
import com.bot2shop.storage.*;

class ApplicationStart<KeyWordType> {

    private ILogger logger = new ConsoleLogger(); // log anything
    private MakeAction<KeyWordType> makeActionProcessor = new MakeAction<KeyWordType>(); // processor for incoming messages
    private Sessions<KeyWordType> sessions = new Sessions<KeyWordType>(); // Session list
    private Connections<KeyWordType> connections = new Connections<KeyWordType>(); // Connection list
    private IDictionary dictionary = new InlineDictionary(); // Connection to dictionary
    private Phrases<KeyWordType> phrases = new Phrases<KeyWordType>(); // Bot knowledge base, <KeyWordType>
    private IPreparator<KeyWordType> preparator = new LowCasePreparator(); // User words and phrases keyword preparator, <KeyWordType>

    // Start of the BOT
    public void start() {
        phrases.setLogger(logger);
        phrases.setPreparator(preparator);
        phrases.setDictionary(dictionary);
        phrases.processDictionary();
        makeActionProcessor.setLogger(logger);
        makeActionProcessor.setPhrases(phrases);
        makeActionProcessor.setPreparator(preparator);
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
        connections.add(new ConsoleChat());
        connections.startAll();
    }

    public static void main(String[] args) {
        new ApplicationStart<String>().start();
    }

}
