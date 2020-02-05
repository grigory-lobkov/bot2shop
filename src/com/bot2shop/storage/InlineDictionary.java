package com.bot2shop.storage;

import com.bot2shop.interfaces.IDictionary;
import com.bot2shop.model.Phrase;
import com.bot2shop.model.Phrase.*;

import java.util.ArrayList;
import java.util.List;

public class InlineDictionary<KeyWordType> implements IDictionary {

    private List<Phrase<KeyWordType>> rawPhraseList;
    private Room roomNow;

    // one line generation helper
    public Phrase<KeyWordType> newP(String sayText, String[] keyWords) {
        Phrase<KeyWordType> p = new Phrase<KeyWordType>();
        p.room = roomNow;
        p.action = Action.SAY;
        p.sayText = sayText;
        p.keyWords = keyWords;
        rawPhraseList.add(p);
        return p;
    }
    public Phrase<KeyWordType> newP(String sayText) {
        return newP(sayText, new String[]{});
    }

    // returns list of phrases from storage
    public List<Phrase<KeyWordType>> getRawPhraseList() {
        rawPhraseList = new ArrayList<Phrase<KeyWordType>>();

        // в качестве ключей используйте только слова, без пробелов и знаков припинания
        // каждый вызов newP автоматически добавляет фразу в результирующий список

        // Секция приветствия
        roomNow = Room.HELLO;

        Phrase pWelcome = newP("Здравствуйте. Я могу помочь Вам быстро оформить заказ в суши-баре Сакура. Вы можете заказать роллы, суши, горячее и десерты.",
                new String[]{"/start"});
        pWelcome.isRoomStart = true;

        Phrase pHello = newP("Рад приветствовать! Что бы Вы хотели у нас заказать?",
                new String[]{"Привет", "Здравствуй", "Здравствуйте", "Здорово", "Хай", "Hi", "Hello"});
        pHello.roomNext = Room.ORDER;

        Phrase pHelloTO = newP("У нас очень вкусные роллы! Их изготавливает повар с черным поясом по Японской кухне!");
        pHelloTO.timeoutSec = 30; // 30 sec
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("Наш повар сделает себе харакири, если вы найдете недостаток в его творениях!");
        pHelloTO.timeoutSec = 60; // 1 min
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("У нас бесплатная доставка при заказе от 500 рублей!");
        pHelloTO.timeoutSec = 300; // 5 min
        pHelloTO.showOnlyOnce = true;

        pHelloTO = newP("Тут Вы можете заказать очень вкусные роллы, отличные суши, ароматное горячее.");
        pHelloTO.timeoutSec = 600; // 10 min

        // Секция заказа
        roomNow = Room.ORDER;

        Phrase pOrder = newP("Выберите одну из категорий нашего ассортимента: роллы, суши, горячее, десерты",
                new String[]{"Заказать", "Заказ", "Заказывать"});
        pOrder.isRoomStart = true;

        // Завершение, выход
        roomNow = Room.FINISH;

        Phrase pFinish = newP("Спасибо! Приходите к нам ещё!",
                new String[]{"/quit", "/exit"});

        return rawPhraseList;
    }

}
