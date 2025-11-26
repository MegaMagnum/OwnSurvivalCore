package me.megamagnum.main;

import me.megamagnum.main.files.commandAFK;
import me.megamagnum.main.files.ColorHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.megamagnum.main.files.commandAFK.afklist;

public class onMove implements Listener {
    @EventHandler
    public static void onMove(PlayerMoveEvent e){

        if(afklist.contains(e.getPlayer().getUniqueId())){
            Player p = e.getPlayer();
            String displayName = ColorHelper.getDisplayName(p);
            p.setDisplayName(displayName);
            p.setPlayerListName(displayName);
            afklist.remove(p.getUniqueId());
            Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.GRAY + "  is vanaf nu niet meer AFK!");
            p.setSleepingIgnored(false);
        }
    }

}