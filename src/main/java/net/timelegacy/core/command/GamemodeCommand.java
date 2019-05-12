package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Rank;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class GamemodeCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = core.rankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length >= 1) {
        if (args[0].equalsIgnoreCase("0")) {
          p.setGameMode(GameMode.SURVIVAL);
          core.messageUtils.sendMessage(
              p, core.messageUtils.SUCCESS_COLOR + "Gamemode set to Survival.", true);

        } else if (args[0].equalsIgnoreCase("1")) {
          p.setGameMode(GameMode.CREATIVE);
          core.messageUtils.sendMessage(
              p, core.messageUtils.SUCCESS_COLOR + "Gamemode set to Creative.", true);

        } else if (args[0].equalsIgnoreCase("2")) {
          p.setGameMode(GameMode.ADVENTURE);
          core.messageUtils.sendMessage(
              p, core.messageUtils.SUCCESS_COLOR + "Gamemode set to Adventure.", true);

        } else {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2]", true);
        }
      } else {
        core.messageUtils.sendMessage(
            p, core.messageUtils.ERROR_COLOR + "Usage: /gamemode [0, 1, 2]", true);
      }
    } else {
      core.messageUtils.noPerm(p);
    }
    return false;
  }
}
