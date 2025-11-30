package me.megamagnum.main;

import me.megamagnum.main.files.commandAFK;
import me.megamagnum.main.files.ColorHelper;
import me.megamagnum.main.files.commandReverse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import static me.megamagnum.main.files.commandAFK.afklist;

public class onMove implements Listener {
    @EventHandler
    public static void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        // Check for reversed movement
        if(commandReverse.isReversed(p)) {
            Location from = e.getFrom();
            Location to = e.getTo();
            
            // Only reverse if player actually moved horizontally
            if(from.getX() != to.getX() || from.getZ() != to.getZ()) {
                // Calculate movement vector
                double deltaX = to.getX() - from.getX();
                double deltaZ = to.getZ() - from.getZ();
                
                // Reverse the movement (invert direction)
                Location reversed = from.clone();
                reversed.setX(from.getX() - deltaX);
                reversed.setZ(from.getZ() - deltaZ);
                reversed.setY(to.getY()); // Keep Y the same (jumping/falling still works)
                reversed.setYaw(to.getYaw());
                reversed.setPitch(to.getPitch());
                
                e.setTo(reversed);
            }
        }

        if(afklist.contains(e.getPlayer().getUniqueId())){
            String displayName = ColorHelper.getDisplayName(p);
            p.setDisplayName(displayName);
            p.setPlayerListName(displayName);
            afklist.remove(p.getUniqueId());
            Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.GRAY + "  is vanaf nu niet meer AFK!");
            p.setSleepingIgnored(false);
        }
    }

}