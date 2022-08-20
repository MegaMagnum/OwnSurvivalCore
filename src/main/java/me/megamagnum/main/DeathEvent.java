package me.megamagnum.main;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

public class DeathEvent implements Listener {
    @EventHandler
    public void OnDeath(PlayerDeathEvent e){





        Main mainplugin = Main.getPlugin(Main.class);
        Player p = e.getEntity();

        double deathlocx = p.getLocation().getX();
        double deathlocy = p.getLocation().getY();
        double deathlocz = p.getLocation().getZ();




        String x = ChatColor.RED + "" + ChatColor.BOLD + "✗:  ";

                p.sendMessage( x + ChatColor.RESET + "" + ChatColor.RED + "   X:" + Math.round(deathlocx) + "    Y:" + Math.round(deathlocy) + "    Z:" + Math.round(deathlocz));





       double maxhealth;
       maxhealth = p.getMaxHealth();
       if(maxhealth <= 2 ){
           Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
           BanList ban = Bukkit.getBanList(BanList.Type.NAME);
           Date date = new Date(System.currentTimeMillis()+12*60*60*1000);
           String banreason = ChatColor.RED + "You died completely. You can join back in 12 hours!";
           String broadcastmessage = ChatColor.RED +"" + ChatColor.BOLD + p.getName() + ChatColor.RESET + "" + ChatColor.RED + " Is compleet doodgegaan en is voor 12 uur gebanned uit de server!";
           Bukkit.broadcastMessage(broadcastmessage);

           for(Player online : Bukkit.getOnlinePlayers()){
               online.playSound(online.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 3F, 0.8F);

           }

           ban.addBan(p.getName(), ChatColor.RED + banreason, date, null );
           p.kickPlayer(banreason);





       } else if (e.getEntity().getKiller() == null) {
            Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxhealth -2);

           for(Player online : Bukkit.getOnlinePlayers()){
               online.playSound(online.getLocation(), Sound.ENTITY_WITHER_HURT, 0.1F, 0.01F);
               online.sendMessage(p.getDisplayName() + "Heeft nog " + p.getMaxHealth() /2  + "  hartje over!");

           }


        }else{
           Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxhealth -2);
           Player killer = e.getEntity().getKiller();
           double killermaxhealth;
           killermaxhealth = killer.getMaxHealth();
           Objects.requireNonNull(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(killermaxhealth +2);
           for(Player online : Bukkit.getOnlinePlayers()){
               online.playSound(online.getLocation(), Sound.ENTITY_WITHER_HURT, 0.1F, 0.01F);
               online.sendMessage(p.getDisplayName() + "Heeft nog " + p.getMaxHealth() /2  + "  hartje over!");

           }

       }

        }

}