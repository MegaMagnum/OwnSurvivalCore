package me.megamagnum.main;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomChallengeEvent implements Listener {
    
    private String activeChallenge = null;
    private String targetWord = null;
    private String reward = null;
    private JavaPlugin plugin;
    private static RandomChallengeEvent instance;
    
    public RandomChallengeEvent(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        startRandomChallenges();
    }
    
    public static RandomChallengeEvent getInstance() {
        return instance;
    }
    
    public void triggerChallenge() {
        startNewChallenge();
    }
    
    private void startRandomChallenges() {
        // Start random challenge elke 10-30 minuten
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(Bukkit.getOnlinePlayers().size() > 0) {
                startNewChallenge();
            }
        }, 20L * 60 * 10, 20L * 60 * getRandomTime(10, 30));
    }
    
    private int getRandomTime(int min, int max) {
        return min + new Random().nextInt(max - min + 1);
    }
    
    private void startNewChallenge() {
        Random random = new Random();
        
        // Lijst met woorden
        List<String> words = Arrays.asList(
            "appel", "banaan", "pizza", "minecraft", "diamant", 
            "creeper", "enderman", "nether", "dragon", "zombie",
            "skelet", "broodje", "kip", "melk", "taart"
        );
        
        // Lijst met rewards
        List<ChallengeReward> rewards = Arrays.asList(
            new ChallengeReward("speed demon", ChatColor.AQUA, Sound.ENTITY_PLAYER_LEVELUP, 
                new PotionEffect(PotionEffectType.SPEED, 20 * 60, 2)),
            new ChallengeReward("sterke held", ChatColor.RED, Sound.ENTITY_ENDER_DRAGON_GROWL,
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 1)),
            new ChallengeReward("vliegende mug", ChatColor.LIGHT_PURPLE, Sound.ENTITY_BAT_TAKEOFF,
                new PotionEffect(PotionEffectType.JUMP, 20 * 60, 3)),
            new ChallengeReward("onzichtbare ninja", ChatColor.GRAY, Sound.ENTITY_ENDERMAN_TELEPORT,
                new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 45, 0)),
            new ChallengeReward("nachtvisie pro", ChatColor.YELLOW, Sound.BLOCK_BEACON_ACTIVATE,
                new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 120, 0)),
            new ChallengeReward("vuurresistent", ChatColor.GOLD, Sound.ITEM_FIRECHARGE_USE,
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 90, 0)),
            new ChallengeReward("regeneratie god", ChatColor.LIGHT_PURPLE, Sound.BLOCK_BEACON_POWER_SELECT,
                new PotionEffect(PotionEffectType.REGENERATION, 20 * 30, 2)),
            new ChallengeReward("glowende ster", ChatColor.AQUA, Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                new PotionEffect(PotionEffectType.GLOWING, 20 * 60, 0))
        );
        
        targetWord = words.get(random.nextInt(words.size()));
        ChallengeReward selectedReward = rewards.get(random.nextInt(rewards.size()));
        
        reward = selectedReward.name;
        activeChallenge = targetWord;
        
        // Announce challenge
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "⚡ RANDOM CHALLENGE ⚡");
            p.sendMessage(ChatColor.YELLOW + "Wie als eerst " + ChatColor.WHITE + "" + ChatColor.BOLD + targetWord + ChatColor.YELLOW + " zegt");
            p.sendMessage(ChatColor.YELLOW + "wordt " + selectedReward.color + "" + ChatColor.BOLD + reward + ChatColor.YELLOW + "!");
            p.sendMessage("");
            
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5F, 1.0F);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.5F, 1.5F);
        }
        
        // Auto-cancel na 2 minuten
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(activeChallenge != null && activeChallenge.equals(targetWord)) {
                activeChallenge = null;
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(ChatColor.RED + "⏰ Challenge verlopen! Niemand won.");
                }
            }
        }, 20L * 120);
    }
    
    @EventHandler
    public void onChat(PlayerChatEvent e) {
        if(activeChallenge == null) return;
        
        Player winner = e.getPlayer();
        String message = e.getMessage().toLowerCase();
        
        if(message.contains(activeChallenge.toLowerCase())) {
            // Winner!
            activeChallenge = null;
            
            // Find the reward effect
            ChallengeReward selectedReward = null;
            List<ChallengeReward> rewards = Arrays.asList(
                new ChallengeReward("speed demon", ChatColor.AQUA, Sound.ENTITY_PLAYER_LEVELUP, 
                    new PotionEffect(PotionEffectType.SPEED, 20 * 60, 2)),
                new ChallengeReward("sterke held", ChatColor.RED, Sound.ENTITY_ENDER_DRAGON_GROWL,
                    new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 1)),
                new ChallengeReward("vliegende mug", ChatColor.LIGHT_PURPLE, Sound.ENTITY_BAT_TAKEOFF,
                    new PotionEffect(PotionEffectType.JUMP, 20 * 60, 3)),
                new ChallengeReward("onzichtbare ninja", ChatColor.GRAY, Sound.ENTITY_ENDERMAN_TELEPORT,
                    new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 45, 0)),
                new ChallengeReward("nachtvisie pro", ChatColor.YELLOW, Sound.BLOCK_BEACON_ACTIVATE,
                    new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 120, 0)),
                new ChallengeReward("vuurresistent", ChatColor.GOLD, Sound.ITEM_FIRECHARGE_USE,
                    new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 90, 0)),
                new ChallengeReward("regeneratie god", ChatColor.LIGHT_PURPLE, Sound.BLOCK_BEACON_POWER_SELECT,
                    new PotionEffect(PotionEffectType.REGENERATION, 20 * 30, 2)),
                new ChallengeReward("glowende ster", ChatColor.AQUA, Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                    new PotionEffect(PotionEffectType.GLOWING, 20 * 60, 0))
            );
            
            for(ChallengeReward r : rewards) {
                if(r.name.equals(reward)) {
                    selectedReward = r;
                    break;
                }
            }
            
            if(selectedReward != null) {
                // Announce winner
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage("");
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "🎉 CHALLENGE GEWONNEN! 🎉");
                    p.sendMessage(ChatColor.YELLOW + winner.getName() + " is nu " + 
                                 selectedReward.color + "" + ChatColor.BOLD + reward + ChatColor.YELLOW + "!");
                    p.sendMessage("");
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0F, 1.2F);
                }
                
                // Give winner effects
                winner.sendTitle(selectedReward.color + "" + ChatColor.BOLD + "JIJ WINT!", 
                               ChatColor.YELLOW + "Je bent nu " + reward + "!", 10, 40, 10);
                winner.playSound(winner.getLocation(), selectedReward.sound, 2.0F, 1.0F);
                winner.playSound(winner.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.5F, 1.2F);
                winner.playSound(winner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 2.0F);
                
                // Apply potion effect
                winner.addPotionEffect(selectedReward.effect);
                
                // Particle effect
                winner.getWorld().spawnParticle(Particle.TOTEM, winner.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);
                winner.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, winner.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }
    
    private static class ChallengeReward {
        String name;
        ChatColor color;
        Sound sound;
        PotionEffect effect;
        
        ChallengeReward(String name, ChatColor color, Sound sound, PotionEffect effect) {
            this.name = name;
            this.color = color;
            this.sound = sound;
            this.effect = effect;
        }
    }
}
