package net.timelegacy.tlcore.command;

import java.io.File;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

  private final TLCore plugin;

  public SpawnCommand(TLCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {

      Player player = (Player) sender;
      if (!player.hasPermission("tlcore.command.spawn")) {
        MessageUtils.noPerm(player);
        return true;
      }

      if (args.length == 0) {
        try {
          player.teleport(doSpawn(player.getWorld().getName()));
        } catch (Exception e) {
          MessageUtils.sendMessage(
              player,
              MessageUtils.ERROR_COLOR
                  + "Please submit a bug report with a screenshot on our Discord.",
              false);
          return true;
        }
        MessageUtils.sendMessage(
            player, MessageUtils.SUCCESS_COLOR + "Teleported to the spawn!", false);
        return true;
      } else {
        MessageUtils.sendMessage(
            player, MessageUtils.ERROR_COLOR + "Usage: /spawn", false); // TODO cleanup
        return true;
      }
    }

    return false;
  }

  private Location doSpawn(String worldName) {
    File file = new File(plugin.getDataFolder(), "spawns.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    World world =
        Bukkit.getServer()
            .getWorld(config.getConfigurationSection("spawns." + worldName).getName());

    if (world == null) {
      return null;
    }

    double x = config.getDouble("spawns." + worldName + ".x");
    double y = config.getDouble("spawns." + worldName + ".y");
    double z = config.getDouble("spawns." + worldName + ".z");
    float yaw = (float) config.getDouble("spawns." + worldName + ".yaw");
    float pitch = (float) config.getDouble("spawns." + worldName + ".pitch");

    return new Location(world, x, y, z, yaw, pitch);
  }

  private Location spawn(String worldName) {
    File file = new File(plugin.getDataFolder(), "spawns.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    World world = Bukkit.getServer().getWorld(config.getString("spawns." + worldName));
    return getLocationFromConfig(config, world);
  }

  private Location spawn() {
    File file = new File(plugin.getDataFolder(), "spawns.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    World world = Bukkit.getServer().getWorld(config.getString("world"));
    return getLocationFromConfig(config, world);
  }

  private Location getLocationFromConfig(YamlConfiguration config, World world) {
    double x = config.getDouble("x");
    double y = config.getDouble("y");
    double z = config.getDouble("z");
    float yaw = (float) config.getDouble("yaw");
    float pitch = (float) config.getDouble("pitch");

    return new Location(world, x, y, z, yaw, pitch);
  }
}
