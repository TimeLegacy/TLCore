package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class FlyCommand implements CommandExecutor {

  private static TLCore plugin = TLCore.getPlugin();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 7) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (plugin.flySpeed.contains(player.getUniqueId())) {
      plugin.flySpeed.remove(player.getUniqueId());
      player.setFlying(false);
      player.setFlySpeed(0.1f);
      player.setAllowFlight(false);
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Flymode disabled.", true);
      return true;
    }

    if (args.length == 0) {
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Flymode enabled at default speed.", true);
      player.setAllowFlight(true);
      player.setFlying(true);
      player.setAllowFlight(true);
      plugin.flySpeed.add(player.getUniqueId());
      return true;
    }
    
    String posnum = args[0];

    int speed;

    try {
      speed = Integer.parseInt(posnum);

      if (speed < 1 || speed > 10) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /fly [speed (1-10)]", true);
      } else {
        if (speed == 1) {
          player.setFlySpeed(0.1f);
        }
        if (speed == 2) {
          player.setFlySpeed(0.2f);
        }
        if (speed == 3) {
          player.setFlySpeed(0.3f);
        }
        if (speed == 4) {
          player.setFlySpeed(0.4f);
        }
        if (speed == 5) {
          player.setFlySpeed(0.5f);
        }
        if (speed == 6) {
          player.setFlySpeed(0.6f);
        }
        if (speed == 7) {
          player.setFlySpeed(0.7f);
        }
        if (speed == 8) {
          player.setFlySpeed(0.8f);
        }
        if (speed == 9) {
          player.setFlySpeed(0.9f);
        }
        if (speed == 10) {
          player.setFlySpeed(1.0f);
        }

        MessageUtils.sendMessage(player, MessageUtils.MAIN_COLOR
                + "Flymode enabled at speed "
                + MessageUtils.SECOND_COLOR
                + speed
                + MessageUtils.MAIN_COLOR
                + ".",
            true);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setAllowFlight(true);
        plugin.flySpeed.add(player.getUniqueId());
      }
    } catch (NumberFormatException ex) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /fly [speed (1-10)]", true);
      return true;
    }

    return false;
  }
}
