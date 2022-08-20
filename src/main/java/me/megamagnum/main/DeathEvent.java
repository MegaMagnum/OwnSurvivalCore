package me.megamagnum.main;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;
import java.util.Objects;

public class DeathEvent implements Listener {
    @EventHandler
    public void OnDeath(PlayerDeathEvent e){
        Main mainplugin = Main.getPlugin(Main.class);
        Player p = e.getEntity();
       double maxhealth;
       maxhealth = p.getMaxHealth();
       if(maxhealth <= 2 ){
           BanList ban = Bukkit.getBanList(BanList.Type.NAME);
           Date date = new Date(System.currentTimeMillis()+24*60*60*1000);
           String banreason = ChatColor.RED + "You died completely. You can join back in 24 hours!";
           ban.addBan(p.getName(), ChatColor.RED + banreason, date, null );
           p.kickPlayer(banreason);





       } else if (e.getEntity().getKiller() == null) {
            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxhealth -2);



        }else{
           Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxhealth -2);
           Player killer = e.getEntity().getKiller();
           double killermaxhealth;
           killermaxhealth = killer.getMaxHealth();
           Objects.requireNonNull(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(killermaxhealth +2);

       }

        }

}



