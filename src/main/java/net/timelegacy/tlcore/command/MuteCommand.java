package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.MuteHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.Punishment;
import net.timelegacy.tlcore.handler.PunishmentHandler;
import net.timelegacy.tlcore.handler.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getName());
      if (r.getPriority() >= 7) {
        if (args.length == 0) {
          MessageUtils.sendMessage(
              p,
              MessageUtils.ERROR_COLOR
                  + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
              true);
        }

        if (args.length >= 2
            && !PunishmentHandler
            .comparePunishments(args[1])
            .toString()
            .equalsIgnoreCase("null")) {
          Punishment muteReason = PunishmentHandler.comparePunishments(args[1]);
          if (args.length == 2) {

            if (PlayerHandler.playerExistsName(args[0])) {

              if (!MuteHandler.isMuted(args[0])) {

                String expire = "true";

                MuteHandler.setMuted(args[0], expire, muteReason, sender.getName());
                MessageUtils.sendMessage(
                    sender,
                    MessageUtils.SECOND_COLOR
                        + args[0]
                        + MessageUtils.MAIN_COLOR
                        + " is now muted.",
                    true);
              } else {
                MessageUtils.sendMessage(
                    sender, MessageUtils.ERROR_COLOR + "Player is already muted.", true);
              }
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else if (args.length == 3) {

            if (PlayerHandler.playerExistsName(args[0])) {

              if (!MuteHandler.isMuted(args[0])) {

                String expire = args[2];

                MuteHandler.setMuted(args[0], expire, muteReason, sender.getName());
                MessageUtils.sendMessage(
                    sender,
                    MessageUtils.SECOND_COLOR
                        + args[0]
                        + MessageUtils.MAIN_COLOR
                        + " is now muted.",
                    true);
              } else {
                MessageUtils.sendMessage(
                    sender, MessageUtils.ERROR_COLOR + "Player is already muted.", true);
              }
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else {
            MessageUtils.sendMessage(
                p,
                MessageUtils.ERROR_COLOR
                    + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
                true);
          }
        }
      } else {
        MessageUtils.noPerm(p);
      }
    }

    return false;
  }
}
