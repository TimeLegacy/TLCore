package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Punishment;
import net.timelegacy.core.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 7) {
        if (args.length == 0) {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.ERROR_COLOR
                  + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
              true);
        }

        if (args.length >= 2
            && !core.punishmentHandler
            .comparePunishments(args[1])
            .toString()
            .equalsIgnoreCase("null")) {
          Punishment muteReason = core.punishmentHandler.comparePunishments(args[1]);
          if (args.length == 2) {

            if (core.playerHandler.playerExistsName(args[0])) {

              if (!core.muteHandler.isMuted(args[0])) {

                String expire = "true";

                core.muteHandler.setMuted(args[0], expire, muteReason, sender.getName());
                core.messageUtils.sendMessage(
                    sender,
                    core.messageUtils.SECOND_COLOR
                        + args[0]
                        + core.messageUtils.MAIN_COLOR
                        + " is now muted.",
                    true);
              } else {
                core.messageUtils.sendMessage(
                    sender, core.messageUtils.ERROR_COLOR + "Player is already muted.", true);
              }
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else if (args.length == 3) {

            if (core.playerHandler.playerExistsName(args[0])) {

              if (!core.muteHandler.isMuted(args[0])) {

                String expire = args[2];

                core.muteHandler.setMuted(args[0], expire, muteReason, sender.getName());
                core.messageUtils.sendMessage(
                    sender,
                    core.messageUtils.SECOND_COLOR
                        + args[0]
                        + core.messageUtils.MAIN_COLOR
                        + " is now muted.",
                    true);
              } else {
                core.messageUtils.sendMessage(
                    sender, core.messageUtils.ERROR_COLOR + "Player is already muted.", true);
              }
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.ERROR_COLOR
                    + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
                true);
          }
        } else {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.ERROR_COLOR + "You must have the correct punishment type.",
              true);
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    }

    return false;
  }
}
