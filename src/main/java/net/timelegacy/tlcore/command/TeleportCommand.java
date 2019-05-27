package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TeleportCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;
      Rank r = RankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (args.length == 0) {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
          return true;
        }

        if (args.length == 1) {
          Player t = Bukkit.getPlayer(args[0]);

          if (t == null) {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            p.teleport(t);
            MessageUtils.sendMessage(
                p,
                MessageUtils.MAIN_COLOR
                    + "Teleporting to "
                    + MessageUtils.SECOND_COLOR
                    + t.getName(),
                true);
          }

          return true;
        }

        if (args.length == 3) {
          String xS = args[0];
          String yS = args[1];
          String zS = args[2];

          try {
            double x = Double.parseDouble(xS);
            double y = Double.parseDouble(yS);
            double z = Double.parseDouble(zS);

            p.teleport(new Location(p.getWorld(), x, y, z));
          } catch (NumberFormatException ex) {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "Invalid coordinates.", true);
          }

          return true;
        }

        MessageUtils.sendMessage(
            p, MessageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
      } else {
        MessageUtils.noPerm(p);
      }

      return true;
    } else {
      return false;
    }
  }
}
