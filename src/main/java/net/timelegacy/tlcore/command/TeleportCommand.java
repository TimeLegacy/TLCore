package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TeleportCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;
      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (args.length == 0) {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
          return true;
        }

        if (args.length == 1) {
          Player t = Bukkit.getPlayer(args[0]);

          if (t == null) {
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            p.teleport(t);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR
                    + "Teleporting to "
                    + core.messageUtils.SECOND_COLOR
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
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Invalid coordinates.", true);
          }

          return true;
        }

        core.messageUtils.sendMessage(
            p, core.messageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
      } else {
        core.messageUtils.noPerm(p);
      }

      return true;
    } else {
      return false;
    }
  }
}
