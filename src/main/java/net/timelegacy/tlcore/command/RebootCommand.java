package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class RebootCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    Player p = (Player) sender;

    Rank r = RankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      for (Player on : Bukkit.getOnlinePlayers()) {
        on.kickPlayer(MessageUtils.colorize("&cRebooting server..."));
      }

      MessageUtils.sendMessage(p, "&cRebooting server...", true);

      Bukkit.shutdown();
    }

    return false;
  }
}
