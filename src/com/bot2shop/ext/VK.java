package com.bot2shop.ext;


/*
 *   VK External Connection, v 1.0.6
 *
 * Dependencies: sdk-1.0.6.jar
 * Url: https://github.com/VKCOM/vk-java-sdk
 * Download: https://search.maven.org/search?q=g:com.vk.api%20AND%20a:sdk
 *
 * HowTo: Download Source code from URL, compile it into .jar with dependencies,
 *        File -> Project Structure  -> Add "Dependencies" (+) -> Select your .jar file
 *
 */
/*
 *
 *    Create access token
 *
 * Открываем группу в ВК. Настройки -> API
 * Open VK group page.
 *   Manage -> API usage
 *     Create token -> Allow access to community messages
 *     Long Poll API -> Long Poll API: Enabled
 *     Event types -> Message received
 *   Messages
 *     Community messages: Enabled
 *     Bot settings
 *       Bot abilities: Enabled
 *       Ability to add this community to chats
 *
 */


import com.bot2shop.interfaces.IConnection;
import com.bot2shop.interfaces.IProcessor;
import com.google.common.base.Strings;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


public class VK implements IConnection, Runnable {

    private int groupId;
    private String access_token;

    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private static int maxMsgId = -1;
    private int randomId = 0;

    public VK() {
    }

    public void setup(String... params) {
        if (params.length != 2) {
            Exception e = new RuntimeException("VK. Not all parameters sent, need: \"groupId\", \"access_token\"");
            logErrorProcessor.process(id, "", new RuntimeException(e));
        } else {
            groupId = Integer.valueOf(params[0]);
            access_token = params[1];
        }
    }

    // Register to external server, awaiting for users
    public void run() {
        // check, if parameters set
        if (groupId <= 0 || Strings.isNullOrEmpty(access_token)) {
            Exception e = new RuntimeException("VK. Not all parameters bound, use first: setup(\"groupId\", \"access_token\")");
            logErrorProcessor.process(id, "", e);
            return;
        }
        // init vk
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(groupId, access_token);
        try {
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            // wait for message
            while (true) {
                Thread.sleep(500);
                this.checkMessages();
            }
        } catch (Exception e) {
            logErrorProcessor.process(id, "", e);
        }
    }

    public boolean sendText(String sessionId, String textMessage) {
        try {
            vk.messages().send(actor).peerId(Integer.valueOf(sessionId)).randomId(randomId++).message(textMessage).execute();
            return true;
        } catch (Exception e) {
            logErrorProcessor.process(id, sessionId, e);
        }
        return false;
    }

    public boolean sendTextVariants(String sessionId, String textMessage, String[] variants, String[] callbacks) {
        try {
            String textVariants = "";
            List<List<KeyboardButton>> buttons = new ArrayList<List<KeyboardButton>>(2);
            List<KeyboardButton> rowInline = new ArrayList<>(8);
            for (int i = 0; i < variants.length; i++) {
                if(i>0 && i%8==0) {
                    buttons.add(rowInline);
                    rowInline = new ArrayList<>(8);
                }
                String option=String.valueOf(i+1);
                textVariants += "\n"+option+")  "+variants[i];
                KeyboardButtonAction action = new KeyboardButtonAction()
                        .setLabel(option)
                        .setPayload(callbacks[i].substring(4)) //TODO: корявое решение, но пока не нашел callback, setPayload - только для числа
                        .setType(KeyboardButtonActionType.TEXT);
                KeyboardButton inlineKeyboardButton = new KeyboardButton().setAction(action);
                rowInline.add(inlineKeyboardButton);
            }
            buttons.add(rowInline);
            Keyboard keyboard = new Keyboard().setInline(true).setButtons(buttons);
            vk.messages().send(actor).peerId(Integer.valueOf(sessionId)).keyboard(keyboard).randomId(randomId++).message(textMessage+textVariants).execute();
            return true;
        } catch (Exception e) {
            logErrorProcessor.process(id, sessionId, e);
        }
        return false;
    }

    // Integer identifier of connection
    private int id = -1;
    public void setConnId(int id) {
        this.id = id;
    }
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

    public void checkMessages() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages().getLongPollHistory(actor).ts(ts);
        if (maxMsgId > 0) {
            eventsQuery.maxMsgId(maxMsgId);
        }
        List<Message> messages = eventsQuery.execute().getMessages().getItems();
        if (messages != null && !messages.isEmpty()) {
            ts = vk.messages()
                    .getLongPollServer(actor)
                    .execute()
                    .getTs();
            for (Message m : messages) {
                if (!m.isOut()) {
                    int messageId = m.getId();
                    if (messageId > maxMsgId) {
                        maxMsgId = messageId;
                    }
                    String text = Strings.isNullOrEmpty(m.getPayload()) ? m.getText() : "/id="+m.getPayload(); //TODO: корявое решение, но пока не нашел callback
                    incomeTextProcessor.process(id, m.getPeerId().toString(), text);
                }
            }
        }
    }

}
