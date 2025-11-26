package me.megamagnum.main.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

public class commandTPS implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TPS berekenen
        double tps = getTPS();
        String tpsColor;
        String tpsStatus;
        
        if (tps >= 19.5) {
            tpsColor = ChatColor.GREEN + "";
            tpsStatus = "Uitstekend";
        } else if (tps >= 17.0) {
            tpsColor = ChatColor.YELLOW + "";
            tpsStatus = "Goed";
        } else if (tps >= 14.0) {
            tpsColor = ChatColor.GOLD + "";
            tpsStatus = "Matig";
        } else {
            tpsColor = ChatColor.RED + "";
            tpsStatus = "Slecht";
        }
        
        // Uptime berekenen
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        String uptime = formatUptime(uptimeMillis);
        
        // Online spelers
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();
        
        // Geheugen info
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        // Berichten versturen
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.AQUA + "           Server Prestaties");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "TPS: " + tpsColor + String.format("%.2f", tps) + ChatColor.GRAY + " (" + tpsColor + tpsStatus + ChatColor.GRAY + ")");
        sender.sendMessage(ChatColor.YELLOW + "Uptime: " + ChatColor.WHITE + uptime);
        sender.sendMessage(ChatColor.YELLOW + "Spelers: " + ChatColor.WHITE + onlinePlayers + ChatColor.GRAY + "/" + ChatColor.WHITE + maxPlayers);
        sender.sendMessage(ChatColor.YELLOW + "Geheugen: " + ChatColor.WHITE + usedMemory + "MB" + ChatColor.GRAY + " / " + ChatColor.WHITE + maxMemory + "MB");
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        
        return true;
    }
    
    private double getTPS() {
        try {
            // Probeer TPS te krijgen via Bukkit's getServer reflection
            Object server = Bukkit.getServer();
            
            // Probeer eerst via CraftServer
            try {
                Object minecraftServer = server.getClass().getMethod("getServer").invoke(server);
                Object[] recentTps = (Object[]) minecraftServer.getClass().getField("recentTps").get(minecraftServer);
                return Math.min(20.0, (double) recentTps[0]);
            } catch (Exception e1) {
                // Probeer via direct field access
                try {
                    java.lang.reflect.Field tpsField = server.getClass().getDeclaredField("recentTps");
                    tpsField.setAccessible(true);
                    double[] tpsArray = (double[]) tpsField.get(server);
                    return Math.min(20.0, tpsArray[0]);
                } catch (Exception e2) {
                    // Laatste poging via Paper API
                    try {
                        double tps = (double) server.getClass().getMethod("getTPS").invoke(server);
                        return Math.min(20.0, tps);
                    } catch (Exception e3) {
                        // Geef 20.0 terug als default
                        return 20.0;
                    }
                }
            }
        } catch (Exception e) {
            return 20.0;
        }
    }
    
    private String formatUptime(long uptimeMillis) {
        long seconds = uptimeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        
        if (days > 0) {
            return String.format("%dd %dh %dm", days, hours, minutes);
        } else if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}
