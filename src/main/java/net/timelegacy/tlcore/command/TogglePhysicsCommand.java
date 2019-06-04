package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TogglePhysicsCommand implements CommandExecutor {

  private TLCore plugin = TLCore.getPlugin();

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      if (plugin.physics) {
        MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Physics has been disabled.", true);
      } else {
        MessageUtils.sendMessage(sender, MessageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
      }

      plugin.physics = !plugin.physics;
      return true;
    }

    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 9) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (plugin.physics) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Physics has been disabled.", true);
    } else {
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
    }

    plugin.physics = !plugin.physics;
    return true;
  }
}
