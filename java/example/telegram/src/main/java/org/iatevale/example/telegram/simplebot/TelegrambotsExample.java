package org.iatevale.example.telegram.simplebot;

import org.iatevale.config.IATevaleConfig;
import org.iatevale.example.telegram.simplebot.impl.MyAmazingBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class TelegrambotsExample {

    static final String BOT_TOKEN = IATevaleConfig.getTelegramToken();

    public static void main(String[] args) {

        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(BOT_TOKEN, new MyAmazingBot(BOT_TOKEN));
            System.out.println("MyAmazingBot successfully started!");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
