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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class ChatEvent implements Listener {
    @EventHandler (priority = EventPriority.LOW)
    public void onChat(PlayerChatEvent e){
        Player sender = e.getPlayer();
        String message = e.getMessage();
        
        // Check voor de 67 meme
        if(message.equals("67")) {
            e.setCancelled(true);
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            
            // Spam 67 8x voor iedereen met EXTREME sound effects en scherm effecten
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(int i = 0; i < 8; i++) {
                    final int index = i;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            // Bericht spam
                            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "67 67 67 67 67 67 67 67");
                            p.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "67", ChatColor.RED + "67 67 67 67 67", 2, 8, 2);
                            
                            // MEER GELUID!
                            p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 2.0F, 2.0F);
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 1.5F);
                            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 2.0F);
                            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.5F, 1.0F);
                            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 0.5F);
                            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 0.5F);
                            p.playSound(p.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1.0F, 0.8F);
                            
                            // Scherm effects - alleen voor degene die 67 zegt
                            if(p.equals(sender)) {
                                // Alle effects inclusief movement voor de sender
                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2, false, false));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, false, false));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 40, 1, false, false));
                                if(index % 2 == 0) {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 1, false, false));
                                }
                            } else {
                                // Voor anderen alleen visuele effects, geen movement
                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2, false, false));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, false, false));
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 40, 1, false, false));
                            }
                        }
                    }, (long) index * 8L);
                }
                
                // MEGA finale voor de kick
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(!p.equals(sender)) {
                            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 2.0F, 1.0F);
                        }
                    }
                    
                    sender.kickPlayer(ChatColor.DARK_RED + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                                     ChatColor.RED + "" + ChatColor.BOLD + "67 67 67 67 67 67 67\n" +
                                     ChatColor.GOLD + "" + ChatColor.BOLD + "DAT ZEG JE NIET ZOMAAR!\n" +
                                     ChatColor.RED + "" + ChatColor.BOLD + "67 67 67 67 67 67 67\n" +
                                     ChatColor.DARK_RED + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                }, 75L);
            });
            return;
        }
        
        // Check voor "Is dit tuff" easter egg
        if(message.toLowerCase().contains("is dit tuff")) {
            e.setCancelled(true);
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            
            // Broadcast het bericht normaal maar met speciale styling
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            // SIGMA TUFF sound effects en title voor iedereen
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    // Dramatische title
                    p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "SIGMA TUFF!", 
                               ChatColor.YELLOW + "Dit is zo tuff!", 10, 40, 10);
                    
                    // EPIC sound effects
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 1.0F);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1.5F);
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 1.2F);
                }
                
                // Extra effecten na korte delay
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 2.0F);
                        p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 1.5F, 1.8F);
                        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "✨ SIGMA TUFF DIT! ✨");
                    }
                }, 10L);
                
                // Nog een wave na 20 ticks
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0F, 1.5F);
                        p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 1.0F);
                    }
                }, 20L);
            });
            return;
        }
        
        // Check voor "sigma" meme easter egg
        if(message.toLowerCase().contains("sigma")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            
            // Broadcast het bericht normaal
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            // SIGMA meme sound effect - de bekende phonk beat
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    // Sigma title
                    p.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "σ", 
                               ChatColor.GRAY + "SIGMA", 5, 20, 5);
                    
                    // Phonk beat simulatie met note blocks en bass
                    // Kick drum (bass note block)
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0F, 0.5F);
                    p.playSound(p.getLocation(), Sound.ENTITY_RAVAGER_STEP, 0.3F, 0.5F);
                }
                
                // Beat continues
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0F, 0.5F);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 1.0F);
                    }
                }, 4L);
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0F, 0.5F);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1.5F, 1.0F);
                    }
                }, 8L);
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.0F, 0.5F);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 1.0F);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, 0.8F);
                    }
                }, 12L);
                
                // Epic finish
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.5F, 0.5F);
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.3F, 1.5F);
                        p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "σ " + ChatColor.GRAY + "Sigma grindset detected");
                    }
                }, 16L);
            });
            return;
        }
        
        // Check voor "skibidi" meme
        if(message.toLowerCase().contains("skibidi")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "SKIBIDI", 
                               ChatColor.YELLOW + "Toilet Rizz!", 5, 25, 5);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 2.0F, 0.6F);
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.5F, 2.0F);
                }
                
                for(int i = 1; i <= 4; i++) {
                    final int beat = i;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.5F, beat % 2 == 0 ? 0.8F : 0.6F);
                        }
                    }, (long) i * 5L);
                }
            });
            return;
        }
        
        // Check voor "rizz" meme
        if(message.toLowerCase().contains("rizz")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "✨ RIZZ ✨", 
                               ChatColor.YELLOW + "Unspoken Rizz!", 5, 20, 5);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 2.0F);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.8F);
                    p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 1.5F);
                }
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "💜 " + ChatColor.BOLD + "W RIZZ DETECTED" + ChatColor.LIGHT_PURPLE + " 💜");
                    }
                }, 10L);
            });
            return;
        }
        
        // Check voor "ohio" meme
        if(message.toLowerCase().contains("ohio")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "⚠ OHIO ⚠", 
                               ChatColor.YELLOW + "Only in Ohio", 5, 30, 5);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 0.5F);
                    p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.5F, 1.0F);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0, false, false));
                }
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0F, 1.5F);
                    }
                }, 15L);
            });
            return;
        }
        
        // Check voor "gyat" meme
        if(message.toLowerCase().contains("gyat")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "GYAT", 
                               ChatColor.GOLD + "💯💯💯", 5, 20, 5);
                    p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, 0.8F);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 2.0F, 1.2F);
                    p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 2.0F, 2.0F);
                }
            });
            return;
        }
        
        // Check voor "sus" / "amogus" meme
        if(message.toLowerCase().contains("sus") || message.toLowerCase().contains("amogus") || message.toLowerCase().contains("among us")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "⛔ SUS ⛔", 
                               ChatColor.WHITE + "Among Us", 5, 25, 5);
                    
                    // Among Us drip sound simulatie
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F, 0.7F);
                }
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F, 0.5F);
                    }
                }, 3L);
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F, 0.6F);
                    }
                }, 6L);
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2.5F, 0.5F);
                        p.sendMessage(ChatColor.RED + "📮 " + ChatColor.BOLD + "THAT'S PRETTY SUS!");
                    }
                }, 9L);
            });
            return;
        }
        
        // Check voor "fanum tax" meme
        if(message.toLowerCase().contains("fanum tax") || message.toLowerCase().contains("fanum")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "FANUM TAX", 
                               ChatColor.YELLOW + "Yoink! 🍟", 5, 25, 5);
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2.0F, 0.8F);
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.5F, 1.2F);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.5F, 1.0F);
                }
                
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.GOLD + "💰 " + ChatColor.BOLD + "Your food has been FANUM TAXED!");
                    }
                }, 10L);
            });
            return;
        }
        
        // Check voor "negers" trigger
        if(message.toLowerCase().contains("negers")) {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ChatEvent.class);
            e.setCancelled(true);
            String displayName = ColorHelper.getDisplayName(sender);
            Bukkit.broadcastMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GADVER DAMME!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 2.0F, 0.5F);
                    p.playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1.5F, 1.0F);
                }
            });
            return;
        }
        
        e.setCancelled(true);

        String displayName = ColorHelper.getDisplayName(sender);

        Bukkit.getConsoleSender().sendMessage("<"+ displayName +"> " + ChatColor.GRAY + message);

        for (Player pinged : Bukkit.getOnlinePlayers()) {
            if(message.contains(pinged.getName())){
                pinged.sendMessage("<"+ displayName +"> " + ChatColor.RED+message);
                pinged.playSound(pinged.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.8F);
            }else{
                pinged.sendMessage("<"+ displayName +"> " + ChatColor.GRAY + message);
            }



        }





        }




}