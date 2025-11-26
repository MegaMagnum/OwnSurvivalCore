package me.megamagnum.main;

import me.megamagnum.main.files.Storage;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class warp implements Listener {

    public static ArrayList<UUID> tptyping = new ArrayList<>();

    private Main mainplugin = Main.getPlugin(Main.class);


@EventHandler
    public void onAction(PlayerInteractEvent e){
    Player p = e.getPlayer();
    ItemStack mainHand = p.getInventory().getItemInMainHand();
    ItemMeta meta = mainHand.getItemMeta();

    ItemStack eyeender = new ItemStack(Material.ENDER_EYE);
    ItemMeta eyeendermeta = eyeender.getItemMeta();
    ArrayList<String> eyeenderlore = new ArrayList();
    eyeenderlore.add(ChatColor.DARK_PURPLE + "This eye will let the teleport to your enlightened star!");
    eyeendermeta.setLore(eyeenderlore);
    eyeendermeta.setDisplayName( ChatColor.GOLD  + "Warping Eye");
    eyeender.setItemMeta(eyeendermeta);

    ItemStack estar = new ItemStack(Material.NETHER_STAR);
    ItemMeta estarmeta = estar.getItemMeta();
    ArrayList<String> estarlore = new ArrayList();
    estarlore.add(ChatColor.DARK_PURPLE + "This star has been enlightened with glowstone!");
    estarmeta.setLore(estarlore);
    estarmeta.setDisplayName( ChatColor.GOLD  + "Enlightened Star");
    estar.setItemMeta(estarmeta);

    ItemStack tpcompas = new ItemStack(Material.BOOK);
    ItemMeta tpcompasmeta = tpcompas.getItemMeta();
    tpcompasmeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
    ArrayList<String> tpcompaslore = new ArrayList();
    tpcompaslore.add( ChatColor.GOLD  + "This codex will bring you to your friends or foes! ");
    tpcompasmeta.setLore(tpcompaslore);
    tpcompasmeta.setDisplayName(ChatColor.MAGIC + "POW" + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "  Arcane Codex  " + ChatColor.RESET + "" + ChatColor.MAGIC + "POW");
    tpcompas.setItemMeta(tpcompasmeta);


    if(e.getAction() == Action.RIGHT_CLICK_AIR) {
        if (p.getItemInHand().getType() == Material.NETHER_STAR) {
            if (meta.hasLore()) {
                Storage.get().set(p.getUniqueId() + "." + "Homecoords", p.getLocation());
                Storage.save();
                for(Player online : Bukkit.getOnlinePlayers()) {
                    online.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 3, 0.5F);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 75, 300));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 275, 300));
                    online.spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 550, 0.5, 0.5, 0.5, 0.1);
                    online.spawnParticle(Particle.SOUL, p.getEyeLocation(), 550, 0.5, 0.5, 0.5, 0.1);

                    p.getInventory().removeItem(estar);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            online.spawnParticle(Particle.TOTEM, p.getEyeLocation(), 550);
                            online.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 3, 0.8F);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                        }
                    }.runTaskLater(mainplugin, 75);
                }
            }

        } else if (p.getItemInHand().getType() == Material.ENDER_EYE) {
            if(meta.hasLore()){
                Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(1, 3, 255), 1);
                Location home =Storage.get().getLocation(p.getUniqueId() + "." + "Homecoords");


                p.getInventory().removeItem(eyeender);
               p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 75, 300));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 275, 300));




                for(Player online : Bukkit.getOnlinePlayers()) {
                    online.spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 350, 0.5, 0.5, 0.5, 0.1);

                    online.playSound(home, Sound.ENTITY_WARDEN_SONIC_CHARGE, 5, 0.4F);
                    online.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 5, 0.4F);

                    new BukkitRunnable(){@Override public void run(){ online.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1F);        }}.runTaskLater(mainplugin,79);
                    new BukkitRunnable(){@Override public void run(){ online.playSound(home, Sound.ENTITY_WARDEN_SONIC_BOOM, 7, 1F);        }}.runTaskLater(mainplugin,79);
                    new BukkitRunnable(){@Override public void run(){ online.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 7, 1F);        }}.runTaskLater(mainplugin,79);

                }
                Location home1 = home.add(0,1,0);

                p.spawnParticle(Particle.SMOKE_LARGE, home, 550, 0.5, 0.5, 0.5, 0.1);
                new BukkitRunnable(){@Override public void run(){  p.spawnParticle(Particle.SCRAPE, home1, 1550, 0.5, 0.5, 0.5, 0.1);       }}.runTaskLater(mainplugin,40);
                e.setCancelled(true);
                

            new BukkitRunnable() {
                @Override
                public void run(){

                    for(Player online : Bukkit.getOnlinePlayers()) {

                        online.spawnParticle(Particle.SOUL, home, 1550, 0.5, 0.5, 0.5, 0.1);
                        online.spawnParticle(Particle.END_ROD, home, 1550, 0.5, 0.5, 0.5, 0.1);
                        online.spawnParticle(Particle.EXPLOSION_HUGE, home, 10, 0.5, 0.5, 0.5, 0.1);


                        online.spawnParticle(Particle.SOUL, p.getLocation(), 1550, 0.5, 0.5, 0.5, 0.1);
                               p.teleport(home);

                        p.removePotionEffect(PotionEffectType.DARKNESS);
                    }
                }
            }.runTaskLater(mainplugin, 80);

            }

        }else if (p.getItemInHand().getType() == Material.BOOK) {
            if(meta.hasLore()){
                p.getInventory().removeItem(tpcompas);
                p.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Arcane Codex: " + ChatColor.LIGHT_PURPLE + "Type the name of the person you want to go to in chat or type (cancel) to cancel me!");
                tptyping.add(p.getUniqueId());

                for(Player online : Bukkit.getOnlinePlayers()) {
                    online.spawnParticle(Particle.END_ROD, p.getEyeLocation(), 1550, 0.5, 0.5, 0.5, 0.1);

                }


            }

        }

    }


}


}