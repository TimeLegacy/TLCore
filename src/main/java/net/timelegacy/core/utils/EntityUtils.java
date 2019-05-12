package net.timelegacy.core.utils;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class EntityUtils {

  /**
   * Nearby Entities
   *
   * @param location Location to have the radius based around
   * @param radius Radius of blocks
   */
  public static Entity[] getNearbyEntities(Location location, int radius) {
    int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
    HashSet<Entity> radiusEntities = new HashSet<Entity>();

    for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
      for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
        int x = (int) location.getX(), y = (int) location.getY(), z = (int) location.getZ();
        for (Entity e :
            new Location(location.getWorld(), x + (chX * 16), y, z + (chZ * 16))
                .getChunk()
                .getEntities()) {
          if (e.getLocation().distance(location) <= radius
              && e.getLocation().getBlock() != location.getBlock()) {
            radiusEntities.add(e);
          }
        }
      }
    }

    return radiusEntities.toArray(new Entity[radiusEntities.size()]);
  }

  /**
   * No AI //todo fix
   *
   * @param en
   *     <p>public void noAI(Entity en) { net.minecraft.server.v1_13_R2.Entity nmsEn =
   *     ((CraftEntity) en).getHandle(); NBTTagCompound compound = new NBTTagCompound();
   *     nmsEn.c(compound); compound.setByte("NoAI", (byte) 1); nmsEn.f(compound); }
   */

  /**
   * Make an entity teleport and look at a specific location
   */
  public void teleportLook(Entity entity, Location teleportLocation, Location lookLocation) {
    Location teleportLocation1 = teleportLocation;
    Location location = teleportLocation1;
    Vector dirBetweenLocations = lookLocation.toVector().subtract(teleportLocation1.toVector());
    location.setDirection(dirBetweenLocations);

    entity.teleport(location);
  }
}
