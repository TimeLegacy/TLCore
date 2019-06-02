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

public class TeleportHereCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getUniqueId());
      if (r.getPriority() >= 9) {

        if (args.length == 0) {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /tphere [player]", true);
          return true;
        }

        if (args.length == 1) {
          Player t = Bukkit.getPlayer(args[0]);

          if (t == null) {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            t.teleport(p);
            MessageUtils.sendMessage(
                p,
                MessageUtils.MAIN_COLOR
                    + "Summoning "
                    + MessageUtils.SECOND_COLOR
                    + t.getName(),
                true);
          }

          return true;
        }

        MessageUtils.sendMessage(
            p, MessageUtils.ERROR_COLOR + "Usage: /tphere [player]", true);
      } else {
        MessageUtils.noPerm(p);
      }

      return true;
    } else {
      return false;
    }
  }
}
