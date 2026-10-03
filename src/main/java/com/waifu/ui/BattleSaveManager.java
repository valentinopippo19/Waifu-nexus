package com.waifu.ui;

import com.waifu.model.Waifu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/** Persistencia simple del combate actual para poder continuar incluso después de cerrar el juego. */
public class BattleSaveManager {
    private final Path saveFile = Path.of(System.getProperty("user.home"), ".waifu-nexus-battle.properties");

    public boolean exists() { return Files.exists(saveFile); }

    public void save(List<Waifu> all, List<Waifu> allies, List<Waifu> enemies,
                     int turn, int enemyTurnIndex, int selectedAllyId, int selectedEnemyId,
                     String strategy, List<String> log, Map<Integer, Integer> damageDealt) {
        Properties p = new Properties();
        p.setProperty("turn", String.valueOf(turn));
        p.setProperty("enemyTurnIndex", String.valueOf(enemyTurnIndex));
        p.setProperty("selectedAlly", String.valueOf(selectedAllyId));
        p.setProperty("selectedEnemy", String.valueOf(selectedEnemyId));
        p.setProperty("strategy", strategy == null ? "EQUILIBRADA" : strategy);
        p.setProperty("allies", ids(allies));
        p.setProperty("enemies", ids(enemies));
        for (Waifu w : all) p.setProperty("hp." + w.getId(), String.valueOf(w.getHp()));
        p.setProperty("log", String.join("\u001F", log));
        p.setProperty("damage", damageDealt.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
        try (var writer = Files.newBufferedWriter(saveFile, StandardCharsets.UTF_8)) {
            p.store(writer, "Waifu Nexus - Combate guardado");
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el combate", e);
        }
    }

    public SavedBattle load(List<Waifu> all) {
        if (!exists()) return null;
        Properties p = new Properties();
        try (var reader = Files.newBufferedReader(saveFile, StandardCharsets.UTF_8)) {
            p.load(reader);
        } catch (IOException e) {
            return null;
        }
        Map<Integer, Waifu> byId = all.stream().collect(Collectors.toMap(Waifu::getId, w -> w));
        List<Waifu> allies = resolve(p.getProperty("allies", ""), byId);
        List<Waifu> enemies = resolve(p.getProperty("enemies", ""), byId);
        for (Waifu w : all) w.restoreBattleState(Integer.parseInt(p.getProperty("hp." + w.getId(), String.valueOf(w.getMaxHp()))));
        List<String> log = p.getProperty("log", "").isBlank()
                ? new ArrayList<>() : new ArrayList<>(List.of(p.getProperty("log").split("\u001F", -1)));
        Map<Integer,Integer> damage = new HashMap<>();
        String rawDamage = p.getProperty("damage", "");
        if (!rawDamage.isBlank()) for (String item : rawDamage.split(",")) {
            String[] pair = item.split(":");
            if (pair.length == 2) damage.put(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
        }
        return new SavedBattle(Integer.parseInt(p.getProperty("turn", "1")),
                Integer.parseInt(p.getProperty("enemyTurnIndex", "0")),
                Integer.parseInt(p.getProperty("selectedAlly", "-1")),
                Integer.parseInt(p.getProperty("selectedEnemy", "-1")),
                p.getProperty("strategy", "EQUILIBRADA"), allies, enemies, log, damage);
    }

    public void delete() { try { Files.deleteIfExists(saveFile); } catch (IOException ignored) {} }

    private String ids(List<Waifu> list) { return list.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.joining(",")); }
    private List<Waifu> resolve(String value, Map<Integer, Waifu> byId) {
        if (value.isBlank()) return new ArrayList<>();
        return Arrays.stream(value.split(",")).map(Integer::parseInt).map(byId::get).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
    }

    public record SavedBattle(int turn, int enemyTurnIndex, int selectedAllyId, int selectedEnemyId,
                              String strategy, List<Waifu> allies, List<Waifu> enemies,
                              List<String> log, Map<Integer,Integer> damageDealt) {}
}
