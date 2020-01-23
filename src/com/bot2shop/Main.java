package com.bot2shop;

import com.bot2shop.ext.*;
import com.bot2shop.interfaces.*;
import com.bot2shop.storage.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {

    static private List<IExtConnection> connections = new ArrayList<>(); // list of bots (connections to different messengers)
    static private ILogger logger = new ConsoleLogger(); // log anything

    // Start of the BOT
    public static void main(String[] args) {
        // Proxy if needed (for Tor browser in memory):
        //System.getProperties().put("proxySet", "true");
        //System.getProperties().put("socksProxyHost", "127.0.0.1");
        //System.getProperties().put("socksProxyPort", "9150");

        IExtConnection bot1 = new Telegram() {
            @Override
            public String incomeText(String sessId, String inText) {
                return Main.incomeText(this.getConnId(), sessId, inText);
            }
            @Override
            public void LogError(String sessId, String errorText) {
                logger.LogError(this.getConnId(), sessId, errorText);
            }
        };
        bot1.setConnId(connections.size());
        connections.add(bot1);
        /*bot1.setup(new String[]{"Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE"});
        bot1.start();*/ // TODO: How to start multiple bots ?
        test();
    }

    /** @noinspection SameParameterValue*/
    // receives message and returns an answer
    // sessId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back
    private static String incomeText(int connId, String sessId, String inText) {
        logger.LogIncome(connId, sessId, inText);
        try {
            String outText = inText+"!"; // TODO: make a processor
            logger.LogOutcome(connId, sessId, outText);
            return outText;
        } catch (Exception e) {
            logger.LogError(connId, sessId, e.getMessage());
        }
        return "";
    }

    private static void test() {
        String input;
        try (Scanner in = new Scanner(System.in)) {
            do {
                input = in.nextLine();
                System.out.println(incomeText(0, "2", input));
            } while (input.compareTo("/exit") != 0);
        } catch (Exception e) {
            logger.LogError(0, "2", e.getMessage());
        }
    }
}
