package com.waifu;

import com.waifu.facade.AnimeGameFacade;
import com.waifu.model.Waifu;
import com.waifu.persistence.*;
import com.waifu.ui.WaifuGameFrame;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WaifuTableDataGateway gateway = new WaifuTableDataGateway();
            WaifuDAO dao = new InMemoryWaifuDAO(gateway);
            WaifuRepository repository = new WaifuRepositoryImpl(dao);
            AnimeGameFacade facade = new AnimeGameFacade(repository);

            facade.createAndSave("fire");
            facade.createAndSave("water");
            facade.createAndSave("shadow");
            facade.importAndSave("Luna");
            facade.createAndSave("wind");
            facade.createAndSave("light");

            List<Waifu> waifus = facade.list();
            new WaifuGameFrame(facade, waifus).setVisible(true);
        });
    }
}
