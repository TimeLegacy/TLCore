package net.timelegacy.tlcore.command;

import java.io.File;
import java.io.IOException;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

  private final TLCore plugin;

  public SetSpawnCommand(TLCore plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("&cNo Perms");
      return true;
    }

    Player player = (Player) sender;

    if (RankHandler.getRank(player.getUniqueId()).getPriority() < 9) {
      MessageUtils.sendMessage(player, "&cNo Perms.", false);
      return true;
    }

    if (args.length == 0) {
      File file = new File(plugin.getDataFolder(), "spawns.yml");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

      String worldName = player.getWorld().getName();

      config.createSection("spawns." + worldName);

      config.set("spawns." + worldName + ".x", player.getLocation().getX());
      config.set("spawns." + worldName + ".y", player.getLocation().getY());
      config.set("spawns." + worldName + ".z", player.getLocation().getZ());
      config.set("spawns." + worldName + ".yaw", player.getLocation().getYaw());
      config.set("spawns." + worldName + ".pitch", player.getLocation().getPitch());
      try {
        config.save(file);
      } catch (IOException e) {
        e.printStackTrace();
      }

      MessageUtils.sendMessage(player, "&aSet world spawn to current location!", false);
    }

    return false;
  }
}
