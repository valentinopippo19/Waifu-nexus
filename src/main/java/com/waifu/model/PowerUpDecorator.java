package com.waifu.model;

public class PowerUpDecorator extends WaifuDecorator {
    public PowerUpDecorator(WaifuPower wrapped) { super(wrapped); }
    public String getName() { return wrapped.getName() + " + Núcleo Arcano"; }
    public int attack() { return wrapped.attack() + 25; }
}
