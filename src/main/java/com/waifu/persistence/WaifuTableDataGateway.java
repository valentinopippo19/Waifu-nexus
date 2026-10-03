package com.waifu.persistence;

import com.waifu.model.Waifu;
import java.util.*;

public class WaifuTableDataGateway {
    private final Map<Integer, Waifu> table = new LinkedHashMap<>();

    public void insert(Waifu waifu) {
        DatabaseConnection.getInstance().open();
        table.put(waifu.getId(), waifu);
    }

    public Optional<Waifu> findById(int id) {
        DatabaseConnection.getInstance().open();
        return Optional.ofNullable(table.get(id));
    }

    public List<Waifu> findAll() {
        DatabaseConnection.getInstance().open();
        return List.copyOf(table.values());
    }
}
