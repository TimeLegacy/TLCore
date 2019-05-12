package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class RebootCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    Player p = (Player) sender;

    Rank r = core.rankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      for (Player on : Bukkit.getOnlinePlayers()) {
        on.kickPlayer(core.messageUtils.c("&cRebooting server..."));
      }

      core.messageUtils.sendMessage(p, "&cRebooting server...", true);

      Bukkit.shutdown();
    }

    return false;
  }
}
