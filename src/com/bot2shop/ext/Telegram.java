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

    // Set base parameters for connection (api keys, tokens, passwords, etc)
    public void setup(String[] params) {
        if (params.length != 2) {
            System.out.println("Telegram. Not all parameters sent, need {\"username\", \"token\"}"); // TODO: raise error
        } else {
            username = params[0];
            token = params[1];
        }
    }

    public String getBotToken() { return this.token; }

    public String getBotUsername() { return this.username; }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            String text = inMessage.getText();
            String chatId = inMessage.getChatId().toString();
            this.incomeText(chatId, text);
        }
    }

    // Function to @Override - receives message and returns an answer, if return value is set
    public void incomeText(String sessionId, String inText) { }

    // Function to @Override - logs an errors
    public void LogError(String sessionId, String errorText) {
        System.out.println(errorText);
    }

    // Integer identifier of connection
    private int id;
    public void setConnId(int id) {
        this.id = id;
    }
    public int getConnId() {
        return id;
    }

    // Send text message to session
    public boolean sendText(String sessionId, String textMessage) {
        try {
            SendMessage outMessage = new SendMessage();
            outMessage.setChatId(Long.getLong(sessionId));
            outMessage.setText(textMessage);
            this.execute(outMessage);
            return true;
        } catch (TelegramApiException e) {
            LogError(sessionId, e.getMessage());
        }
        return false;
    }

    // Register to external server, awaiting for users
    public void start() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

}
