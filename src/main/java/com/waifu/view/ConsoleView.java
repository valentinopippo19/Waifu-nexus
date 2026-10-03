package com.waifu.view;

import com.waifu.model.Waifu;
import com.waifu.model.WaifuPower;

public class ConsoleView {
    public void title() {
        System.out.println("==============================================");
        System.out.println("        WAIFU NEXUS - OO DESIGN LAB");
        System.out.println("==============================================");
    }

    public void section(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void showWaifu(Waifu waifu) {
        System.out.println(waifu);
    }

    public void showBuild(WaifuPower power) {
        System.out.printf("%s | ATK=%d DEF=%d HP=%d%n",
                power.getName(), power.attack(), power.defense(), power.hp());
    }

    public void showCount(int count) {
        System.out.println("Waifus persistidas: " + count);
    }
}
