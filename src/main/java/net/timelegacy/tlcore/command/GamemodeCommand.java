package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class GamemodeCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 9) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (label.equalsIgnoreCase("gms")) {
      player.setGameMode(GameMode.SURVIVAL);
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Survival.", true);
    } else if (label.equalsIgnoreCase("gmc")) {
      player.setGameMode(GameMode.CREATIVE);
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Creative.", true);
    } else if (label.equalsIgnoreCase("gmsp")) {
      player.setGameMode(GameMode.SPECTATOR);
      MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Spectator.", true);
    } else {
      if (args.length >= 1) {
        if (args[0].equalsIgnoreCase("0")) {
          player.setGameMode(GameMode.SURVIVAL);
          MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Survival.", true);

        } else if (args[0].equalsIgnoreCase("1")) {
          player.setGameMode(GameMode.CREATIVE);
          MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Creative.", true);

        } else if (args[0].equalsIgnoreCase("2")) {
          player.setGameMode(GameMode.ADVENTURE);
          MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Adventure.", true);

        } else if (args[0].equalsIgnoreCase("3")) {
          player.setGameMode(GameMode.SPECTATOR);
          MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "Gamemode set to Spectator.", true);

        } else {
          MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2, 3]", true);
        }
      } else {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2, 3]", true);
      }
    }

    return false;
  }
}
