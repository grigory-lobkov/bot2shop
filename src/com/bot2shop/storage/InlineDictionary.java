package com.bot2shop.storage;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.interfaces.ILogger;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Phrase.*;
import com.bot2shop.model.Topic;

import java.util.ArrayList;
import java.util.List;

/*
 *   Phrases base, stored in program code
 */


public class InlineDictionary<KeyWordType> implements IDictionary {

    private List<Phrase<KeyWordType>> rawPhraseList; // list of phrases
    private List<Topic> rawTopicList; // list of topics
    private String topicNow; // default topic for currently adding words

    // logger
    private ILogger logger;
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    // one line generation helper
    public Phrase<KeyWordType> newP(String sayText, String[] keyWords) {
        Phrase<KeyWordType> p = new Phrase<KeyWordType>();
        p.topicShortName = topicNow;
        p.action = Action.SAY;
        p.sayText = sayText;
        p.keyWords = keyWords;
        rawPhraseList.add(p);
        return p;
    }

    public Phrase<KeyWordType> newP(String sayText) {
        return newP(sayText, new String[]{});
    }

    public String newT(String shortName, String name) {
        Topic t = new Topic();
        t.shortName = shortName;
        t.name = name;
        rawTopicList.add(t);
        return shortName;
    }

    public String newT(String shortName) {
        return newT(shortName, "");
    }

    // do actions, prepare connection/data
    public void process() {
        rawTopicList = new ArrayList<Topic>();
        rawPhraseList = new ArrayList<Phrase<KeyWordType>>();

        //
        // list of phrases - список фраз
        //
        // inside keywords, you can use only alphabet characters, without spaces, etc
        // в качестве ключей используйте только слова, без пробелов и знаков припинания
        //
        // each newT call adds the rawTopicList automatically
        // каждый вызов newT автоматически добавляет тему в результирующий список
        // each newP call adds the rawPhraseList automatically
        // каждый вызов newP автоматически добавляет фразу в результирующий список

        // Секция приветствия
        topicNow = newT("HELLO");

        Phrase pWelcome = newP("Здравствуйте. Я могу помочь Вам быстро оформить заказ в суши-баре Сакура. Вы можете заказать роллы, суши, горячее и десерты.",
                new String[]{"/start"});
        //pWelcome.isTopicStart = true;

        Phrase pHello = newP("Рад приветствовать! Что бы Вы хотели у нас заказать?",
                new String[]{"Привет", "Здравствуй", "Здравствуйте", "Здорово", "Хай", "Hi", "Hello"});
        //pHello.topicNext = Topic.ORDER;

        Phrase pHelloTO = newP("У нас очень вкусные роллы! Их изготавливает повар с черным поясом по Японской кухне!");
        pHelloTO.timeoutSec = 30; // 30 sec
        pHelloTO.showChance = 20;
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("Наш повар сделает себе харакири, если вы найдете недостаток в его творениях!");
        pHelloTO.timeoutSec = 60; // 1 min
        pHelloTO.showChance = 20;
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("У нас бесплатная доставка при заказе от 500 рублей!");
        pHelloTO.timeoutSec = 300; // 5 min
        pHelloTO.showChance = 20;
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("Тут Вы можете заказать очень вкусные роллы, отличные суши, ароматное горячее.");
        pHelloTO.showChance = 20;
        pHelloTO.timeoutSec = 600; // 10 min

        // Секция заказа
        topicNow = newT("ORDER");

        Phrase pOrder = newP("Выберите одну из категорий нашего ассортимента: роллы, суши, горячее, десерты",
                new String[]{"Заказать", "Заказ", "Заказывать"});
        //pOrder.isTopicStart = true;

        // Завершение, выход
        topicNow = newT("FINISH");

        Phrase pFinish = newP("Спасибо! Приходите к нам ещё!",
                new String[]{"/quit", "/exit"});
    }

    // returns list of phrases from storage
    public List<Phrase<KeyWordType>> getPhraseList() {
        return rawPhraseList;
    }

    // returns list of topics from storage
    public List<Topic> getTopicList() {
        return rawTopicList;
    }

}