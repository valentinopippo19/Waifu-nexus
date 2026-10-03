package com.waifu.model;

public class BaseWaifuPower implements WaifuPower {
    private final Waifu waifu;

    public BaseWaifuPower(Waifu waifu) { this.waifu = waifu; }

    public String getName() { return waifu.getName(); }
    public int attack() { return waifu.getAttack(); }
    public int defense() { return waifu.getDefense(); }
    public int hp() { return waifu.getHp(); }
}
