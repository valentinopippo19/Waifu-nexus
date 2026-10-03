package com.waifu.persistence;

import com.waifu.model.Waifu;
import java.util.List;
import java.util.Optional;

public class WaifuRepositoryImpl implements WaifuRepository {
    private final WaifuDAO dao;

    public WaifuRepositoryImpl(WaifuDAO dao) { this.dao = dao; }

    public void add(Waifu waifu) { dao.save(waifu); }
    public Optional<Waifu> get(int id) { return dao.findById(id); }
    public List<Waifu> all() { return dao.findAll(); }
}
