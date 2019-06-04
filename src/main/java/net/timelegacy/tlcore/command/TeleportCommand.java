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
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 9) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (args.length == 0) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
      return true;
    }

    if (args.length == 1) {
      Player target = Bukkit.getPlayer(args[0]);

      if (target == null) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Player not found.", true);
      } else {
        player.teleport(target);
        MessageUtils.sendMessage(player, MessageUtils.MAIN_COLOR
                + "Teleporting to "
                + MessageUtils.SECOND_COLOR
                + target.getName(),
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

        player.teleport(new Location(player.getWorld(), x, y, z));
      } catch (NumberFormatException ex) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Invalid coordinates.", true);
      }

      return true;
    }

    MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /tp [player|x] <y> <y>", true);
    return true;
  }
}
