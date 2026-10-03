package com.waifu.persistence;

import com.waifu.model.Waifu;
import java.util.List;
import java.util.Optional;

public class InMemoryWaifuDAO implements WaifuDAO {
    private final WaifuTableDataGateway gateway;

    public InMemoryWaifuDAO(WaifuTableDataGateway gateway) { this.gateway = gateway; }

    public void save(Waifu waifu) { gateway.insert(waifu); }
    public Optional<Waifu> findById(int id) { return gateway.findById(id); }
    public List<Waifu> findAll() { return gateway.findAll(); }
}
