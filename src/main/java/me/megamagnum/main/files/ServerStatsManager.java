package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.World;
import com.google.gson.JsonObject;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class ServerStatsManager {
    private static ServerStatsManager instance;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private long startTime;
    
    // Cache voor async-safe data
    private volatile int cachedTotalEntities = 0;
    private volatile int cachedTotalChunks = 0;
    private volatile int cachedOnlinePlayers = 0;
    private volatile String cachedMainWorld = "world";
    private volatile int cachedWorldCount = 1;
    
    private ServerStatsManager() {
        this.startTime = System.currentTimeMillis();
        updateCachedData(); // Initial update
    }
    
    public static ServerStatsManager getInstance() {
        if (instance == null) {
            instance = new ServerStatsManager();
        }
        return instance;
    }
    
    // Deze methode moet aangeroepen worden op de main thread
    public void updateCachedData() {
        try {
            cachedOnlinePlayers = Bukkit.getOnlinePlayers().size();
            cachedWorldCount = Bukkit.getWorlds().size();
            
            if (!Bukkit.getWorlds().isEmpty()) {
                cachedMainWorld = Bukkit.getWorlds().get(0).getName();
            }
            
            int totalEntities = 0;
            int totalChunks = 0;
            for (World world : Bukkit.getWorlds()) {
                totalEntities += world.getEntities().size();
                totalChunks += world.getLoadedChunks().length;
            }
            cachedTotalEntities = totalEntities;
            cachedTotalChunks = totalChunks;
        } catch (Exception e) {
            // Ignore errors during update
        }
    }
    
    public JsonObject getServerStats() {
        JsonObject stats = new JsonObject();
        
        // Basic server info (these are thread-safe)
        stats.addProperty("serverName", Bukkit.getServer().getName());
        stats.addProperty("version", Bukkit.getVersion());
        stats.addProperty("bukkitVersion", Bukkit.getBukkitVersion());
        stats.addProperty("onlinePlayers", cachedOnlinePlayers);
        stats.addProperty("maxPlayers", Bukkit.getMaxPlayers());
        
        // Performance stats
        stats.addProperty("tps", getTPS());
        stats.addProperty("usedMemory", getUsedMemory());
        stats.addProperty("maxMemory", getMaxMemory());
        stats.addProperty("freeMemory", getFreeMemory());
        stats.addProperty("memoryUsagePercentage", getMemoryUsagePercentage());
        
        // Uptime
        stats.addProperty("uptime", getUptime());
        
        // World info (cached)
        stats.addProperty("worldCount", cachedWorldCount);
        stats.addProperty("mainWorld", cachedMainWorld);
        stats.addProperty("totalEntities", cachedTotalEntities);
        stats.addProperty("totalChunksLoaded", cachedTotalChunks);
        
        return stats;
    }
    
    private double getTPS() {
        try {
            // Dit is een geschatte TPS berekening
            // Voor echte TPS zou je een meer geavanceerde methode moeten gebruiken
            Object server = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
            Object[] recentTps = (Object[]) server.getClass().getField("recentTps").get(server);
            return Math.min(20.0, Double.parseDouble(df.format(recentTps[0])));
        } catch (Exception e) {
            // Fallback methode
            return 20.0; // Assume 20 TPS als we het niet kunnen bepalen
        }
    }
    
    private String getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        return formatBytes(usedMemory);
    }
    
    private String getMaxMemory() {
        return formatBytes(Runtime.getRuntime().maxMemory());
    }
    
    private String getFreeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
        return formatBytes(freeMemory);
    }
    
    private double getMemoryUsagePercentage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        return Double.parseDouble(df.format((double) usedMemory / maxMemory * 100));
    }
    
    private String getUptime() {
        long uptime = System.currentTimeMillis() - startTime;
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        return String.format("%d dagen, %d uur, %d minuten", 
                           days, hours % 24, minutes % 60);
    }
    
    public JsonObject getDetailedPerformance() {
        JsonObject performance = new JsonObject();
        
        // CPU Info (basic)
        performance.addProperty("availableProcessors", Runtime.getRuntime().availableProcessors());
        
        // Memory details
        Runtime runtime = Runtime.getRuntime();
        performance.addProperty("totalMemoryBytes", runtime.totalMemory());
        performance.addProperty("freeMemoryBytes", runtime.freeMemory());
        performance.addProperty("maxMemoryBytes", runtime.maxMemory());
        performance.addProperty("usedMemoryBytes", runtime.totalMemory() - runtime.freeMemory());
        
        // JVM Details
        performance.addProperty("javaVersion", System.getProperty("java.version"));
        performance.addProperty("javaVendor", System.getProperty("java.vendor"));
        performance.addProperty("osName", System.getProperty("os.name"));
        performance.addProperty("osVersion", System.getProperty("os.version"));
        performance.addProperty("osArch", System.getProperty("os.arch"));
        
        // Thread info
        performance.addProperty("activeThreadCount", Thread.activeCount());
        
        return performance;
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();
    }
}