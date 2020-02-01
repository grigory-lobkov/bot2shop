/*
*   Telegram External Connection, v 4.6
*
* Dependencies: telegrambots-4.6-jar-with-dependencies.jar
* Url: https://github.com/rubenlagus/TelegramBots
* Ftp: https://repo1.maven.org/maven2/org/telegram/telegrambots/
*
* HowTo: Download Source code from URL, compile it into .jar with dependencies,
*        File -> Project Structure  -> Add "Dependencies" (+) -> Select your .jar file
*
* */
package com.bot2shop.ext;

import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.IProcessor;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Telegram extends TelegramLongPollingBot implements IConnection {

    private String username;
    private String token;

    public Telegram() {
        ApiContextInitializer.init();
    }

    // Set base parameters for connection (api keys, tokens, passwords, etc)
    public void setup(String... params) {
        if (params.length != 2) {
            String errText = "Telegram. Not all parameters sent, need: \"username\", \"token\"";
            logErrorProcessor.process(id, "", errText);
            System.out.println(errText); // TODO: raise error
        } else {
            username = params[0];
            token = params[1];
        }
    }

    // for TelegramLongPollingBot
    public String getBotToken() { return this.token; }
    public String getBotUsername() { return this.username; }

    // when something happens, from TelegramLongPollingBot
    public void onUpdateReceived(Update update) {
        //logErrorProcessor.process(id, "-1", update.toString());
        /*Update {
            updateId = 343146593,
            message = Message {
                messageId = 12, from = User {
                    id = 679473869, firstName = 'Григорий', isBot = false, lastName = 'null', userName = 'Grig2', languageCode = 'en'
                }, date = 1580580183, chat = Chat {
                    id = 679473869, type = 'private', firstName = 'Григорий', lastName = 'null', userName = 'Grig2'
                }, text = 'Sample'
            },
        }*/
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            String text = inMessage.getText();
            String chatId = inMessage.getChatId().toString();
            incomeTextProcessor.process(id, chatId, text);
        }
    }

    // Send text message to session
    public boolean sendText(String sessionId, String textMessage) {
        try {
            SendMessage outMessage = new SendMessage();
            outMessage.setChatId(Long.parseLong(sessionId));
            outMessage.setText(textMessage);
            this.execute(outMessage);
            return true;
        } catch (TelegramApiException e) {
            logErrorProcessor.process(id, sessionId, e.getMessage());
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

    // Function to @Override - receives message and returns an answer, if return value is set
    //public void incomeText(String sessionId, String inText) { }

    // Function to @Override - logs an errors
    //public void LogError(String sessionId, String errorText) { System.out.println(errorText); }

    // Integer identifier of connection
    private int id = -1;
    public void setConnId(int id) { this.id = id; }
    //public int getConnId() { return id; }

    // Set processor to log errors
    private IProcessor<String> logErrorProcessor;
    public void setErrorProcessor(IProcessor<String> logErrorProcessor) {
        this.logErrorProcessor = logErrorProcessor;
    }

    // Set processor to process income user messages
    private IProcessor<String> incomeTextProcessor;
    public void setIncomeTextProcessor(IProcessor<String> incomeTextProcessor) {
        this.incomeTextProcessor = incomeTextProcessor;
    }


}
