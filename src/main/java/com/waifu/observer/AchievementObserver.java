package com.waifu.observer;

import java.util.function.Consumer;

/** Observer que desbloquea logros sin depender de una consola. */
public class AchievementObserver implements Observer {
    private final Consumer<String> notification;
    private int events;

    public AchievementObserver() {
        this(message -> { });
    }

    public AchievementObserver(Consumer<String> notification) {
        this.notification = notification;
    }

    @Override
    public void update(String event) {
        events++;
        if (events == 3) {
            notification.accept("🏆 ¡Logro desbloqueado! Primer vínculo de batalla.");
        }
    }
}
