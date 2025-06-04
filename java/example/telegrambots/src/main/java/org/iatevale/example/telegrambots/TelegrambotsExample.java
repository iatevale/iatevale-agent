package org.iatevale.example.telegrambots;

import org.iatevale.example.telegrambots.impl.MyAmazingBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class TelegrambotsExample {

    public static void main(String[] args) {

        String botToken = "7729074055:AAEWoroVxq9CHlVmvWWs0teD-9ADFfQMItc";
        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new MyAmazingBot(botToken));
            System.out.println("MyAmazingBot successfully started!");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
