package net.timelegacy.tlcore.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PotionUtils {

  /**
   * Clear all potions
   *
   * @param p
   */
  public static void clearPotions(Player p) {
    for (PotionEffectType e : PotionEffectType.values()) {
      if (e != null && p.hasPotionEffect(e)) {
        p.removePotionEffect(e);
      }
    }
  }
}
