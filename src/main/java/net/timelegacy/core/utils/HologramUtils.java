package net.timelegacy.core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class HologramUtils {

  public void createHologram(Location l, String text) {
    ArmorStand as = l.getWorld().spawn(l, ArmorStand.class);

    as.setVisible(false);
    as.setCustomName(text);
    as.setCustomNameVisible(true);
    as.setGravity(false);
  }

  public void removeAllHolograms(World w) {
    for (Entity e : w.getEntities()) {
      if (e instanceof ArmorStand) {
        e.remove();
      }
    }
  }
}
