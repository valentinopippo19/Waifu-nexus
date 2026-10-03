package com.waifu.controller;

import com.waifu.facade.AnimeGameFacade;
import com.waifu.model.*;
import com.waifu.strategy.*;
import com.waifu.view.ConsoleView;

public class WaifuController {
    private final AnimeGameFacade facade;
    private final ConsoleView view;

    public WaifuController(AnimeGameFacade facade, ConsoleView view) {
        this.facade = facade;
        this.view = view;
    }

    public void runDemo() {
        view.title();

        Waifu akari = facade.createAndSave("fire");
        Waifu mizuki = facade.createAndSave("water");
        Waifu yoru = facade.createAndSave("shadow");
        Waifu imported = facade.importAndSave("Luna");

        view.section("MVC + FACTORY + ADAPTER + REPOSITORY");
        facade.list().forEach(view::showWaifu);

        view.section("COMPOSITE");
        Squad squad = facade.buildSquad("Team Eclipse", akari, mizuki, imported);
        squad.print("");

        view.section("DECORATOR");
        WaifuPower build = facade.createBuild(yoru);
        view.showBuild(build);

        view.section("STRATEGY + STATE + OBSERVER");
        facade.fight(akari, mizuki, new AggressiveStrategy());
        facade.fight(yoru, akari, new BalancedStrategy());
        facade.fight(akari, mizuki, new DefensiveStrategy());
        mizuki.receiveDamage(200);
        facade.fight(mizuki, akari, new AggressiveStrategy());

        view.section("PERSISTENCIA");
        view.showCount(facade.list().size());
    }
}
