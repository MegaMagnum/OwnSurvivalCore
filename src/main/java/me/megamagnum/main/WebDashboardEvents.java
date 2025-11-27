package me.megamagnum.main;

import me.megamagnum.main.files.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class WebDashboardEvents implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Update player data when they join
        PlayerDataManager.getInstance().updatePlayerData(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Mark player as offline when they leave
        PlayerDataManager.getInstance().removePlayer(event.getPlayer().getUniqueId());
    }
}