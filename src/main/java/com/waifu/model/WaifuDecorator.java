package com.waifu.model;

public abstract class WaifuDecorator implements WaifuPower {
    protected final WaifuPower wrapped;

    protected WaifuDecorator(WaifuPower wrapped) { this.wrapped = wrapped; }

    public String getName() { return wrapped.getName(); }
    public int attack() { return wrapped.attack(); }
    public int defense() { return wrapped.defense(); }
    public int hp() { return wrapped.hp(); }
}
