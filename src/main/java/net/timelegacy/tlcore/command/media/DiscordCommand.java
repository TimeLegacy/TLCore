package net.timelegacy.tlcore.command.media;

import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;

    MessageUtils.sendMessage(player, "&7&l[&cTAC&7&l] &B&ohttps://discord.gg/wUTvvUg", false);
    return true;
  }
}
