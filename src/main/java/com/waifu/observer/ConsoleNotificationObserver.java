package com.waifu.observer;

public class ConsoleNotificationObserver implements Observer {
    public void update(String event) {
        System.out.println("[OBSERVER] " + event);
    }
}
