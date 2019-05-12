package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Punishment;
import net.timelegacy.core.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Created by Batman on 2016-06-26.
 */
public class UnBanCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 8) {

        if (args.length == 0) {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Usage: /unban [player]", true);
        } else if (args.length == 1) {

          if (core.playerHandler.playerExistsName(args[0])) {

            if (core.banHandler.isBanned(args[0])) {

              core.banHandler.setBanned(args[0], "false", Punishment.NULL, sender.getName());
              core.messageUtils.sendMessage(
                  sender,
                  core.messageUtils.SECOND_COLOR
                      + args[0]
                      + core.messageUtils.MAIN_COLOR
                      + " is now unbanned.",
                  true);
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player isn't banned.", true);
            }
          } else {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          }
        } else {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Usage: /unban [player]", true);
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    }

    return false;
  }
}
