package net.timelegacy.tlcore.utils;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class EntityUtils {

  /**
   * Get nearby entities
   *
   * @param location location to have radius around
   * @param radius radius to check
   */
  public static Entity[] getNearbyEntities(Location location, int radius) {
    int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
    HashSet<Entity> radiusEntities = new HashSet<Entity>();

    for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
      for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
        int x = (int) location.getX(), y = (int) location.getY(), z = (int) location.getZ();
        for (Entity e : new Location(location.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
          if (e.getLocation().distance(location) <= radius && e.getLocation().getBlock() != location.getBlock()) {
            radiusEntities.add(e);
          }
        }
      }
    }

    return radiusEntities.toArray(new Entity[radiusEntities.size()]);
  }

  /**
   * Teleport entity and make look at location
   *
   * @param entity entity to teleport
   * @param teleportLocation location to teleport
   * @param lookLocation location to look at
   */
  public static void teleportLook(Entity entity, Location teleportLocation, Location lookLocation) {
    Location teleportLocation1 = teleportLocation;
    Location location = teleportLocation1;
    Vector dirBetweenLocations = lookLocation.toVector().subtract(teleportLocation1.toVector());
    location.setDirection(dirBetweenLocations);

    entity.teleport(location);
  }
}
