package me.megamagnum.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.megamagnum.main.files.commandAFK.afklist;

public class onMove implements Listener {
    @EventHandler
    public static void onMove(PlayerMoveEvent e){

        if(afklist.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().setDisplayName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + e.getPlayer().getName());
            e.getPlayer().setPlayerListName(ChatColor.GREEN + ""  + "◆ " + ChatColor.RESET + e.getPlayer().getName());
            afklist.remove(e.getPlayer().getUniqueId());
            Bukkit.broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.GRAY + "  is vanaf nu niet meer AFK!");
            e.getPlayer().setSleepingIgnored(false);
        }
    }

}