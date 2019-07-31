package net.timelegacy.tlcore.handler;

import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.tlcore.datatype.PlayerData;

public class CacheHandler {

  public static HashMap<UUID, PlayerData> cacheData = new HashMap<>();

  /**
   * Add a player to the cache
   */

  public static void addPlayer(UUID uuid) {
    if (!cacheData.containsKey(uuid)) {
      cacheData.put(uuid, new PlayerData(uuid));
    }
  }

  /**
   * Remove a player from the cache
   */

  public static void removePlayer(UUID uuid) {
    if (cacheData.containsKey(uuid)) {
      cacheData.get(uuid);
    }
  }

  /**
   * Check if a player is cached
   */

  public static boolean isPlayerCached(UUID uuid) {
    return cacheData.containsKey(uuid);
  }


  /**
   * Get the player data
   */

  public static PlayerData getPlayerData(UUID uuid) {
    if (cacheData.containsKey(uuid)) {
      return cacheData.get(uuid);
    } else {
      return null;
    }
  }
}
