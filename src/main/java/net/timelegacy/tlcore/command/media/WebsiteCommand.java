package net.timelegacy.tlcore.command.media;

import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WebsiteCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;

    MessageUtils.sendMessage(player, "&7&l[&cTAC&7&l] &7&oHere ya go! &b&ohttps://timelegacy.net/", false);
    return true;
  }

}
