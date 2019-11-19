/*
*   Telegram External Connection, v 4.4.0
*
* Dependencies: telegrambots-4.4.0-jar-with-dependencies.jar
* Url: https://github.com/rubenlagus/TelegramBots/releases
*
* HowTo: Download Source code from URL, compile it into .jar with dependencies,
*        File -> Project Structure  -> Add "Dependencies" (+) -> Select your .jar file
*
* */
package com.bot2shop.ext;

import com.bot2shop.extenders.EExtConnection;
import com.bot2shop.interfaces.IExtConnection;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Telegram extends TelegramLongPollingBot implements IExtConnection {

    private String username;
    private String token;


    protected Telegram() {
        ApiContextInitializer.init();
    }
    public void setup(String[] params) {
        if (params.length != 2) {
            System.out.println("Telegram. Not all parameters sent, need {\"username\", \"token\"}"); // TODO: rise error
        } else {
            username = params[0];
            token = params[1];
        }
    }

    public String getBotToken() { return this.token; }

    public String getBotUsername() { return this.username; }

    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                String text = inMessage.getText();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());
                outMessage.setText(this.income(inMessage.getFrom().getId().toString(), text));
                this.execute(outMessage);
            }
        } catch (TelegramApiException var5) {
            var5.printStackTrace();
        }

    }

    // function to @Override
    public String income(String sessId, String inText) {
        return inText;
    }

    public void start() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(this);
        } catch (TelegramApiRequestException var3) {
            var3.printStackTrace();
        }

    }

}
