package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TeleportHereCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (args.length == 0) {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Usage: /tphere [player]", true);
          return true;
        }

        if (args.length == 1) {
          Player t = Bukkit.getPlayer(args[0]);

          if (t == null) {
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            t.teleport(p);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR
                    + "Summoning "
                    + core.messageUtils.SECOND_COLOR
                    + t.getName(),
                true);
          }

          return true;
        }

        core.messageUtils.sendMessage(
            p, core.messageUtils.ERROR_COLOR + "Usage: /tphere [player]", true);
      } else {
        core.messageUtils.noPerm(p);
      }

      return true;
    } else {
      return false;
    }
  }
}
