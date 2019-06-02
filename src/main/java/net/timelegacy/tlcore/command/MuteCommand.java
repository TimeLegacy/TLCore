package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.MuteHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.PunishmentHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MuteCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getUniqueId());
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

            if (PlayerHandler.playerExists(args[0])) {
              UUID uuid = PlayerHandler.getUUID(args[0]);

              if (!MuteHandler.isMuted(uuid)) {

                String expire = "true";

                MuteHandler.setMuted(uuid, expire, muteReason, p.getUniqueId());
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

            if (PlayerHandler.playerExists(args[0])) {
              UUID uuid = PlayerHandler.getUUID(args[0]);
              if (!MuteHandler.isMuted(uuid)) {

                String expire = args[2];

                MuteHandler.setMuted(uuid, expire, muteReason, p.getUniqueId());
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
