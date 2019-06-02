package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.BanHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class UnBanCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getUniqueId());
      if (r.getPriority() >= 8) {

        if (args.length == 0) {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /unban [player]", true);
        } else if (args.length == 1) {

          if (PlayerHandler.playerExists(args[0])) {
            UUID uuid = PlayerHandler.getUUID(args[0]);
            if (BanHandler.isBanned(uuid)) {

              BanHandler.setBanned(uuid, "false", Punishment.NULL, p.getUniqueId());
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + args[0]
                      + MessageUtils.MAIN_COLOR
                      + " is now unbanned.",
                  true);
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player isn't banned.", true);
            }
          } else {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          }
        } else {
          MessageUtils.sendMessage(
              p, MessageUtils.ERROR_COLOR + "Usage: /unban [player]", true);
        }
      } else {
        MessageUtils.noPerm(p);
      }
    }

    return false;
  }
}
