package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private static PlayerDataManager instance;
    private final Map<UUID, PlayerData> playerData;
    private final Gson gson;
    
    private PlayerDataManager() {
        this.playerData = new ConcurrentHashMap<>();
        this.gson = new Gson();
        loadAllPlayerData();
    }
    
    public static PlayerDataManager getInstance() {
        if (instance == null) {
            instance = new PlayerDataManager();
        }
        return instance;
    }
    
    public void loadAllPlayerData() {
        // Laad data voor alle online spelers
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerData(player);
        }
    }
    
    public void updatePlayerData(Player player) {
        PlayerData data = new PlayerData();
        data.name = player.getName();
        data.uuid = player.getUniqueId().toString();
        data.hearts = (int) (player.getMaxHealth() / 2); // Convert to hearts
        data.currentHealth = (int) player.getHealth();
        data.kills = player.getStatistic(Statistic.PLAYER_KILLS);
        data.deaths = player.getStatistic(Statistic.DEATHS);
        data.playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 3600; // Convert to hours
        data.isOnline = true;
        data.lastSeen = System.currentTimeMillis();
        data.location = player.getLocation().getWorld().getName() + " (" + 
                       (int) player.getLocation().getX() + ", " + 
                       (int) player.getLocation().getY() + ", " + 
                       (int) player.getLocation().getZ() + ")";
        
        playerData.put(player.getUniqueId(), data);
    }
    
    public JsonArray getAllPlayersJson() {
        JsonArray players = new JsonArray();
        
        // Update data voor online spelers
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerData(player);
        }
        
        // Sorteer spelers op hearts (hoogste eerst)
        List<PlayerData> sortedPlayers = new ArrayList<>(playerData.values());
        sortedPlayers.sort((a, b) -> Integer.compare(b.hearts, a.hearts));
        
        for (PlayerData data : sortedPlayers) {
            JsonObject playerObj = new JsonObject();
            playerObj.addProperty("name", data.name);
            playerObj.addProperty("hearts", data.hearts);
            playerObj.addProperty("currentHealth", data.currentHealth);
            playerObj.addProperty("kills", data.kills);
            playerObj.addProperty("deaths", data.deaths);
            playerObj.addProperty("kdr", data.deaths > 0 ? (double) data.kills / data.deaths : data.kills);
            playerObj.addProperty("playtime", String.format("%.1f uur", data.playtime));
            playerObj.addProperty("isOnline", data.isOnline);
            playerObj.addProperty("location", data.location);
            playerObj.addProperty("lastSeen", new Date(data.lastSeen).toString());
            
            players.add(playerObj);
        }
        
        return players;
    }
    
    public JsonObject getPlayerByName(String name) {
        for (PlayerData data : playerData.values()) {
            if (data.name.equalsIgnoreCase(name)) {
                JsonObject playerObj = new JsonObject();
                playerObj.addProperty("name", data.name);
                playerObj.addProperty("uuid", data.uuid);
                playerObj.addProperty("hearts", data.hearts);
                playerObj.addProperty("currentHealth", data.currentHealth);
                playerObj.addProperty("kills", data.kills);
                playerObj.addProperty("deaths", data.deaths);
                playerObj.addProperty("kdr", data.deaths > 0 ? (double) data.kills / data.deaths : data.kills);
                playerObj.addProperty("playtime", data.playtime);
                playerObj.addProperty("isOnline", data.isOnline);
                playerObj.addProperty("location", data.location);
                playerObj.addProperty("lastSeen", new Date(data.lastSeen).toString());
                
                return playerObj;
            }
        }
        return null;
    }
    
    public JsonObject getLeaderboard() {
        JsonObject leaderboard = new JsonObject();
        
        // Update data
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerData(player);
        }
        
        List<PlayerData> players = new ArrayList<>(playerData.values());
        
        // Top Hearts
        JsonArray topHearts = new JsonArray();
        players.stream()
               .sorted((a, b) -> Integer.compare(b.hearts, a.hearts))
               .limit(10)
               .forEach(p -> {
                   JsonObject entry = new JsonObject();
                   entry.addProperty("name", p.name);
                   entry.addProperty("hearts", p.hearts);
                   topHearts.add(entry);
               });
        
        // Top Kills
        JsonArray topKills = new JsonArray();
        players.stream()
               .sorted((a, b) -> Integer.compare(b.kills, a.kills))
               .limit(10)
               .forEach(p -> {
                   JsonObject entry = new JsonObject();
                   entry.addProperty("name", p.name);
                   entry.addProperty("kills", p.kills);
                   topKills.add(entry);
               });
        
        // Top KDR
        JsonArray topKDR = new JsonArray();
        players.stream()
               .filter(p -> p.deaths > 0) // Avoid division by zero
               .sorted((a, b) -> Double.compare(
                   (double) b.kills / b.deaths,
                   (double) a.kills / a.deaths))
               .limit(10)
               .forEach(p -> {
                   JsonObject entry = new JsonObject();
                   entry.addProperty("name", p.name);
                   entry.addProperty("kdr", String.format("%.2f", (double) p.kills / p.deaths));
                   topKDR.add(entry);
               });
        
        leaderboard.add("hearts", topHearts);
        leaderboard.add("kills", topKills);
        leaderboard.add("kdr", topKDR);
        
        return leaderboard;
    }
    
    public void removePlayer(UUID uuid) {
        if (playerData.containsKey(uuid)) {
            playerData.get(uuid).isOnline = false;
            playerData.get(uuid).lastSeen = System.currentTimeMillis();
        }
    }
    
    public static class PlayerData {
        public String name;
        public String uuid;
        public int hearts;
        public int currentHealth;
        public int kills;
        public int deaths;
        public double playtime;
        public boolean isOnline;
        public String location;
        public long lastSeen;
    }
}