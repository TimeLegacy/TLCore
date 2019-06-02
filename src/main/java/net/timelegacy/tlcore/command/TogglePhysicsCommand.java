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
  public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getUniqueId());
      if (r.getPriority() >= 9) {
        if (plugin.physics) {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Physics has been disabled.", true);
        } else {
          MessageUtils.sendMessage(
              p, MessageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
        }

        plugin.physics = !plugin.physics;
      } else {
        MessageUtils.noPerm(p);
      }

      return true;
    } else {
      if (plugin.physics) {
        MessageUtils.sendMessage(
            sender, MessageUtils.ERROR_COLOR + "Physics has been disabled.", true);
      } else {
        MessageUtils.sendMessage(
            sender, MessageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
      }

      plugin.physics = !plugin.physics;
      return false;
    }
  }
}
