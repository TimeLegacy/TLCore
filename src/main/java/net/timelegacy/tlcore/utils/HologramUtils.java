package net.timelegacy.tlcore.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class HologramUtils {

  /**
   * Create a hologram
   *
   * @param l    location to create
   * @param text text of hologram
   */
  public static void createHologram(Location l, String text) {
    ArmorStand as = l.getWorld().spawn(l, ArmorStand.class);

    as.setVisible(false);
    as.setCustomName(text);
    as.setCustomNameVisible(true);
    as.setGravity(false);
  }

  /**
   * Remove all holograms in the world
   *
   * @param w
   */
  public static void removeAllHolograms(World w) {
    for (Entity e : w.getEntities()) {
      if (e instanceof ArmorStand) {
        e.remove();
      }
    }
  }
}
