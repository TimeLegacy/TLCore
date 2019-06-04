package net.timelegacy.tlcore.utils;

import org.bukkit.entity.Player;

public class PotionUtils {

  /**
   * Clear all potions that a player has.
   */
  public static void clearPotions(Player player) {
    player.getActivePotionEffects().clear();
  }
}
