package me.megamagnum.main;

import me.megamagnum.main.files.ColorHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;



public class ChatEvent implements Listener {
    @EventHandler (priority = EventPriority.LOW)
    public void onChat(PlayerChatEvent e){
        Player sender = e.getPlayer();
        e.setCancelled(true);

        String displayName = ColorHelper.getDisplayName(sender);

        Bukkit.getConsoleSender().sendMessage("<"+ displayName +"> " + ChatColor.GRAY + e.getMessage());

        for (Player pinged : Bukkit.getOnlinePlayers()) {
            if(e.getMessage().contains(pinged.getName())){
                pinged.sendMessage("<"+ displayName +"> " + ChatColor.RED+e.getMessage());
                pinged.playSound(pinged.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.8F);
            }else{
                pinged.sendMessage("<"+ displayName +"> " + ChatColor.GRAY + e.getMessage());
            }



        }





        }




}