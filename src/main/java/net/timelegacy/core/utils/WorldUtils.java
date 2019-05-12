package net.timelegacy.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class WorldUtils {

  /**
   * Circle
   */
  public static List<Location> circle(
      Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
    List<Location> circleblocks = new ArrayList<Location>();
    int cx = loc.getBlockX();
    int cy = loc.getBlockY();
    int cz = loc.getBlockZ();
    for (int x = cx - r; x <= cx + r; x++) {
      for (int z = cz - r; z <= cz + r; z++) {
        for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
          double dist =
              (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
          if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
            Location l = new Location(loc.getWorld(), x, y + plus_y, z);
            circleblocks.add(l);
          }
        }
      }
    }

    return circleblocks;
  }

  public Location locationFromConfig(ConfigurationSection config, World world, String key) {
    return new Location(
        world,
        config.getDouble(key + ".x"),
        config.getDouble(key + ".y"),
        config.getDouble(key + ".z"),
        (float) config.getDouble(key + ".yaw", 0),
        (float) config.getDouble(key + ".pitch", 0));
  }

  /**
   * Setup the world
   */
  public void setupWorld(World world, boolean spawnMobs) {
    // world auto save disable
    world.setAutoSave(false);
    // don't Spawn monster and animals
    world.setSpawnFlags(spawnMobs, spawnMobs);
    // No storms
    world.setStorm(false);
    // no thunders
    world.setThundering(false);
    // set day light
    world.setTime(0);
    // forever sunny
    world.setWeatherDuration(Integer.MAX_VALUE);
    // Enables/disables text output of command block commands to console
    world.setGameRuleValue("commandBlockOutput", "false");
    // Enables/disables day/night cycle
    world.setGameRuleValue("doDaylightCycle", "false");
    // Enables/disables fire updates (no fire spread or dissipation)
    world.setGameRuleValue("doFireTick", "false");
    // Enables/disables mob drops
    // Enables/disables creepers, ghasts, and Wither blowing up blocks,
    // endermen picking up blocks and zombies breaking doors
    world.setGameRuleValue("mobGriefing", "false");
    // Enables/disables the spawning of mobs unless you want them to
    // (excl: eggs and mob spawners will still spawn mobs)
    world.setGameRuleValue("doMobSpawning", "false");
    // Enables/disables keeping inventory on death
    world.setGameRuleValue("keepInventory", "false");

    /*world.setGameRuleValue("doMobLoot", "false");
    // Enables/disables blocks dropping items when broken (includes TNT
    // destroying blocks)
    world.setGameRuleValue("doTileDrops", "false");
    // Enables/disables keeping inventory on death
    world.setGameRuleValue("keepInventory", "false");
    // Enables/disables creepers, ghasts, and Wither blowing up blocks,
    // endermen picking up blocks and zombies breaking doors
    world.setGameRuleValue("mobGriefing", "false");
    // Allows/Disallows player to naturally regenerate health,
    // regardless of food level
    world.setGameRuleValue("naturalRegeneration", "false");*/
  }
}
