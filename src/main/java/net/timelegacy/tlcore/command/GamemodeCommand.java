package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
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
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = RankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length >= 1) {
        if (args[0].equalsIgnoreCase("0")) {
          p.setGameMode(GameMode.SURVIVAL);
          MessageUtils.sendMessage(
              p, MessageUtils.SUCCESS_COLOR + "Gamemode set to Survival.", true);

        } else if (args[0].equalsIgnoreCase("1")) {
          p.setGameMode(GameMode.CREATIVE);
          MessageUtils.sendMessage(
              p, MessageUtils.SUCCESS_COLOR + "Gamemode set to Creative.", true);

        } else if (args[0].equalsIgnoreCase("2")) {
          p.setGameMode(GameMode.ADVENTURE);
          MessageUtils.sendMessage(
              p, MessageUtils.SUCCESS_COLOR + "Gamemode set to Adventure.", true);

        } else {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2]", true);
        }
      } else {
        MessageUtils.sendMessage(
            p, MessageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2]", true);
      }
    } else {
      MessageUtils.noPerm(p);
    }
    return false;
  }
}
