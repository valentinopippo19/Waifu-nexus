package com.waifu.persistence;

import com.waifu.model.Waifu;
import java.util.List;
import java.util.Optional;

public interface WaifuRepository {
    void add(Waifu waifu);
    Optional<Waifu> get(int id);
    List<Waifu> all();
}
