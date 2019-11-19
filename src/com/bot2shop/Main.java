package com.bot2shop;

import com.bot2shop.ext.Telegram;
import com.bot2shop.interfaces.IExtConnection;

import java.util.Scanner;

class Main {

    // Start of the BOT
    public static void main(String[] args) {
        // Proxy if needed
        //System.getProperties().put("proxySet", "true");
        //System.getProperties().put("socksProxyHost", "127.0.0.1");
        //System.getProperties().put("socksProxyPort", "9150");

        IExtConnection bot1 = new Telegram() {
            @Override
            public String income(String sessId, String inText) {
                return Main.income(1, sessId, inText);
            }
        };
        bot1.setup(new String[]{"Pizza24testingbot", "1011637303:AAE7o8myLHhW96fgAlnHI3EQeAwEbs12_fE"});
        //bot1.start(); // TODO: How to start multiple bots ?
        test();
    }

    /** @noinspection SameParameterValue*/
    // receives message and returns an answer
    // sessId - session, we are working with
    // inText - incoming message from user
    // returns text, sending to user back
    private static String income(int connId, String sessId, String inText) {
        return inText;
    }

    private static void test() {
        Scanner in = new Scanner(System.in);
        String input;
        try {
            do {
                input = in.nextLine();
                System.out.println(income(1, "2", input));
            } while (input.compareTo("/exit") != 0);
        } finally {
            in.close();
        }
    }
}
