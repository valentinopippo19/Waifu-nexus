package com.waifu.persistence;

import com.waifu.model.Waifu;
import java.util.List;
import java.util.Optional;

public interface WaifuDAO {
    void save(Waifu waifu);
    Optional<Waifu> findById(int id);
    List<Waifu> findAll();
}
