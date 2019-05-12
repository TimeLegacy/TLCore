package net.timelegacy.core.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PotionUtils {

  public static void clearPotions(Player p) {
    for (PotionEffectType e : PotionEffectType.values()) {
      if (e != null && p.hasPotionEffect(e)) {
        p.removePotionEffect(e);
      }
    }
  }
}
