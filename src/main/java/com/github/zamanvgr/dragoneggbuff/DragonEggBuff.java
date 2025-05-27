package com.github.zamanvgr.dragoneggbuff;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class DragonEggBuff extends JavaPlugin implements Listener {
   private final Map<UUID, PotionEffect> playerEffects = new HashMap<UUID, PotionEffect>();

   public void onEnable() {
      this.getLogger().info("Plugin has enabled!");
      this.getServer().getPluginManager().registerEvents(this, this);
      Bukkit.getScheduler().runTaskTimer(this, this::checkPlayerInventories, 20L, 100L);
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      this.handleEggStatus(event.getPlayer());
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      UUID playerId = event.getPlayer().getUniqueId();
      if (this.playerEffects.containsKey(playerId)) {
         event.getPlayer().removePotionEffect(((PotionEffect)this.playerEffects.get(playerId)).getType());
         this.playerEffects.remove(playerId);
      }

   }

   private void checkPlayerInventories() {
      Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();

      while(playerIterator.hasNext()) {
         Player player = (Player)playerIterator.next();
         this.handleEggStatus(player);
      }

   }

   private void handleEggStatus(Player player) {
      boolean hasDragonEgg = this.hasDragonEgg(player);
      if (hasDragonEgg) {
         this.applySpecialEffects(player);
      } else {
         this.removeSpecialEffects(player);
      }

   }

   private boolean hasDragonEgg(Player player) {
      return player.getInventory().contains(Material.DRAGON_EGG);
   }

   private void applySpecialEffects(Player player) {
      this.removeSpecialEffects(player);
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
      player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 2));
      this.playerEffects.put(player.getUniqueId(), new PotionEffect(PotionEffectType.SPEED, 400, 1));
      this.playerEffects.put(player.getUniqueId(), new PotionEffect(PotionEffectType.STRENGTH, 400, 2));
   }

   private void removeSpecialEffects(Player player) {
      UUID playerId = player.getUniqueId();
      if (this.playerEffects.containsKey(playerId)) {
         player.removePotionEffect(((PotionEffect)this.playerEffects.get(playerId)).getType());
         this.playerEffects.remove(playerId);
      }

   }
}
