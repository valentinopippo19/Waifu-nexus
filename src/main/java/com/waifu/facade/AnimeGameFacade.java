package com.waifu.facade;

import com.waifu.adapter.AnimeApiAdapter;
import com.waifu.external.FakeAnimeApi;
import com.waifu.factory.WaifuFactory;
import com.waifu.model.*;
import com.waifu.observer.Observer;
import com.waifu.persistence.WaifuRepository;
import com.waifu.strategy.*;

import java.util.List;

public class AnimeGameFacade {
    private final WaifuFactory factory = new WaifuFactory();
    private final AnimeApiAdapter adapter = new AnimeApiAdapter(new FakeAnimeApi());
    private final WaifuRepository repository;

    public AnimeGameFacade(WaifuRepository repository) {
        this.repository = repository;
    }

    public Waifu createAndSave(String type) {
        Waifu waifu = factory.create(type);
        repository.add(waifu);
        return waifu;
    }

    public Waifu importAndSave(String alias) {
        Waifu waifu = adapter.importWaifu(alias);
        repository.add(waifu);
        return waifu;
    }

    public List<Waifu> list() { return repository.all(); }

    public int fight(Waifu attacker, Waifu defender, CombatStrategy strategy) {
        if (!attacker.canAttack()) return -1;
        int damage = strategy.calculateDamage(attacker, defender);
        defender.receiveDamage(damage);
        return damage;
    }

    public void registerObserver(Waifu waifu, Observer observer) {
        waifu.addObserver(observer);
    }

    public Squad buildSquad(String name, Waifu... members) {
        Squad squad = new Squad(name);
        for (Waifu member : members) squad.add(new SquadMember(member));
        return squad;
    }

    public WaifuPower createBuild(Waifu waifu) {
        return new ArmorDecorator(new PowerUpDecorator(new BaseWaifuPower(waifu)));
    }
}
