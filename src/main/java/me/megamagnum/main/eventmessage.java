package me.megamagnum.main;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static me.megamagnum.main.warp.tptyping;

public class eventmessage implements Listener {
    Main mainplugin = Main.getPlugin(Main.class);
    @EventHandler (priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event){
        event.setCancelled(true);
        ItemStack tpcompas = new ItemStack(Material.BOOK);
        ItemMeta tpcompasmeta = tpcompas.getItemMeta();
        tpcompasmeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        ArrayList<String> tpcompaslore = new ArrayList();
        tpcompaslore.add( ChatColor.GOLD  + "This codex will bring you to your friends or foes! ");
        tpcompasmeta.setLore(tpcompaslore);
        tpcompasmeta.setDisplayName(ChatColor.MAGIC + "POW" + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "  Arcane Codex  " + ChatColor.RESET + "" + ChatColor.MAGIC + "POW");
        tpcompas.setItemMeta(tpcompasmeta);




        Player player = event.getPlayer();
        if(tptyping.contains(player.getUniqueId())){
            Player target = Bukkit.getPlayerExact(event.getMessage());
            if(target == null){
                player.getInventory().addItem(tpcompas);
                tptyping.remove(player.getUniqueId());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 0.4F);



            }else{
                for(Player online : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getEyeLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 0.2F, 0.8F );
                    online.playSound(target.getEyeLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 0.7F,0.7F);
                    online.spawnParticle(Particle.DRAGON_BREATH, target.getEyeLocation(), 1550);
                    tptyping.remove(player.getUniqueId());
                }


                new BukkitRunnable() {

                public void run() {



                    Location targetloc = target.getEyeLocation();
                    for(Player online : Bukkit.getOnlinePlayers()) {
                        online.spawnParticle(Particle.DRAGON_BREATH, targetloc, 1550);
                        online.spawnParticle(Particle.END_ROD, targetloc, 1550);
                        online.spawnParticle(Particle.EXPLOSION_HUGE, targetloc, 10);
                        online.playSound(targetloc, Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1F);
                        online.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 0.3F, 1F);
                    }

                    player.teleport(targetloc);
                }
                }.runTaskLater(mainplugin, 100);
            }


        }
    }
}