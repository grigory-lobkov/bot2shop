package com.bot2shop.ext;

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
 */


import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.IProcessor;
import com.google.common.base.Strings;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;

public class Telegram extends TelegramLongPollingBot implements IConnection, Runnable {

    private String username;
    private String token;

    public Telegram() {
        ApiContextInitializer.init();
    }

    // Set base parameters for connection (api keys, tokens, passwords, etc)
    public void setup(String... params) {
        if (params.length != 2) {
            Exception e = new RuntimeException("Telegram. Not all parameters sent, need: \"username\", \"token\"");
            logErrorProcessor.process(id, "", new RuntimeException(e));
        } else {
            username = params[0];
            token = params[1];
            //TODO: add setChatTitle, setChatDescription: https://core.telegram.org/bots/api#setchattitle
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
            // process sent text
            Message inMessage = update.getMessage();
            String text = inMessage.getText();
            String chatId = inMessage.getChatId().toString();
            incomeTextProcessor.process(id, chatId, text);
        } else if (update.hasCallbackQuery()) {
            // process InlineKeyboardButton
            CallbackQuery inCallbackQuery = update.getCallbackQuery();
            Message inMessage = inCallbackQuery.getMessage();
            String callbackData = inCallbackQuery.getData();
            Long chatId = inMessage.getChatId();

            try {
                // Change variants question to single, chosen question ... does it really need!?
//                EditMessageText updateMessage = new EditMessageText()
//                        .setChatId(chatId)
//                        .setMessageId(inMessage.getMessageId())
//                        .setText(callbackData); // we need to find text option, not callback message
//                execute(updateMessage);
                // process callback
                incomeTextProcessor.process(id, chatId.toString(), callbackData);
            } catch (Exception e) {
                logErrorProcessor.process(id, chatId.toString(), e);
            }
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
            logErrorProcessor.process(id, sessionId, e);
        }
        return false;
    }

    // Send title variants to session
    public boolean sendTextVariants(String sessionId, String textMessage, String[] variants, String[] callbacks) {
        try {
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>(variants.length);
            for (int i = 0; i < variants.length; i++) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>(1);
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton()
                        .setText(variants[i])
                        .setCallbackData(callbacks[i]);
                rowInline.add(inlineKeyboardButton);
                rowsInline.add(rowInline);
            }
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                    .setKeyboard(rowsInline);

            SendMessage outMessage = new SendMessage()
                    .setChatId(Long.parseLong(sessionId))
                    .setText(textMessage)
                    .setReplyMarkup(markupInline);
            this.execute(outMessage);
            return true;
        } catch (TelegramApiException e) {
            logErrorProcessor.process(id, sessionId, e);
        }
        return false;
    }

    // Register to external server, awaiting for users
    public void run() {
        // check, if parameters set
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(username)) {
            Exception e = new RuntimeException("Telegram. Not all parameters bound, use first: setup(\"username\", \"token\")");
            logErrorProcessor.process(id, "", e);
            return;
        }
        // init telegram
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            logErrorProcessor.process(id, "", e);
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
    private IProcessor<Exception> logErrorProcessor;

    public void setErrorProcessor(IProcessor<Exception> logErrorProcessor) {
        this.logErrorProcessor = logErrorProcessor;
    }

    // Set processor to process income user messages
    private IProcessor<String> incomeTextProcessor;

    public void setIncomeTextProcessor(IProcessor<String> incomeTextProcessor) {
        this.incomeTextProcessor = incomeTextProcessor;
    }


}