package com.waifu.model;

public class ArmorDecorator extends WaifuDecorator {
    public ArmorDecorator(WaifuPower wrapped) { super(wrapped); }
    public String getName() { return wrapped.getName() + " + Armadura Lunar"; }
    public int defense() { return wrapped.defense() + 20; }
}
