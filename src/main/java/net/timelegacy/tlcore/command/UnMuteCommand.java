package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.MuteHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class UnMuteCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getName());
      if (r.getPriority() >= 8) {

        if (args.length == 0) {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /unmute [player]", true);
        } else if (args.length == 1) {

          if (PlayerHandler.playerExistsName(args[0])) {

            if (MuteHandler.isMuted(args[0])) {

              MuteHandler.setMuted(args[0], "false", Punishment.NULL, sender.getName());
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + args[0]
                      + MessageUtils.MAIN_COLOR
                      + " is now unmuted.",
                  true);
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player isn't muted.", true);
            }
          } else {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          }
        } else {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /unmute [player]", true);
        }
      } else {
        MessageUtils.noPerm(p);
      }
    }

    return false;
  }
}
