package me.megamagnum.main;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class heartevents implements Listener {
    Main mainplugin = Main.getPlugin(Main.class);
    @EventHandler
    public void onPlace(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        double maxhealth;
        maxhealth = p.getMaxHealth();


        if(mainplugin.getConfig().getBoolean("Lifesteal")) {
            ItemStack heart = new ItemStack(Material.SKELETON_SKULL);
            ItemMeta heartmeta = heart.getItemMeta();
            ArrayList<String> heartlore = new ArrayList();
            heartlore.add(ChatColor.DARK_PURPLE + "This crystal holds 1 heart!");
            heartmeta.setLore(heartlore);
            heartmeta.setDisplayName(ChatColor.MAGIC + "" + ChatColor.RED + "❤");
            heart.setItemMeta(heartmeta);


            ItemStack mainHand = p.getInventory().getItemInMainHand();
            ItemMeta meta = mainHand.getItemMeta();

            if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (p.getItemInHand().getType() == Material.SKELETON_SKULL) {
                    if (meta.hasLore()) {
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxhealth + 2);

                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 10);
                        p.getInventory().removeItem(heart);

                        e.setCancelled(true);
                    }
                }
            }

        }

        }
    @EventHandler
    public void onUse(BlockPlaceEvent e) {
        if(mainplugin.getConfig().getBoolean("Lifesteal")) {
        Player p = e.getPlayer();
        if(e.getBlockPlaced().getType() == Material.SKELETON_SKULL) {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "Not very smart to place a heart.. So i stopped it!");
            e.setCancelled(true);
        }
        }

    }


}

