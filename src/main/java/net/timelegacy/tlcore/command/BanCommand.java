package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.BanHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.PunishmentHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.BungeeUtils;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BanCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getName());
      if (r.getPriority() >= 8) {

        if (args.length == 0) {
          MessageUtils.sendMessage(
              p,
              MessageUtils.ERROR_COLOR
                  + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
              true);
        }
        if (args.length >= 2
            && !PunishmentHandler
            .comparePunishments(args[1])
            .toString()
            .equalsIgnoreCase("null")) {
          if (args.length == 2) {

            if (PlayerHandler.playerExistsName(args[0])) {

              if (!BanHandler.isBanned(args[0])) {
                String reason = args[1].toUpperCase();

                Punishment banReason = PunishmentHandler.comparePunishments(reason);

                String expire = "true";

                BanHandler.setBanned(args[0], expire, banReason, sender.getName());
                MessageUtils.sendMessage(
                        sender,
                        MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.",
                        true);

                Player pl = Bukkit.getPlayer(args[0]);
                if (pl != null) {
                  BungeeUtils.kickPlayer(
                          pl,
                          ChatColor.translateAlternateColorCodes(
                                  '&',
                                  MessageUtils.messagePrefix
                                          + "&4You have been banned from the server. &cReason: &f&o"
                                          + reason));
                }
              } else {
                MessageUtils.sendMessage(
                    sender, MessageUtils.ERROR_COLOR + "Player is already banned.", true);
              }
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else if (args.length == 3) {

            if (PlayerHandler.playerExistsName(args[0])) {

              if (!BanHandler.isBanned(args[0])) {
                String reason = args[1].toUpperCase();

                Punishment banReason = PunishmentHandler.comparePunishments(reason);

                String expire = args[2];

                BanHandler.setBanned(args[0], expire, banReason, sender.getName());
                MessageUtils.sendMessage(
                        sender,
                        MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.",
                        true);

                Player pl = Bukkit.getPlayer(args[0]);
                if (pl != null) {
                  BungeeUtils.kickPlayer(
                          pl,
                          ChatColor.translateAlternateColorCodes(
                                  '&',
                                  MessageUtils.messagePrefix
                                          + "&4You have been banned from the server. &cReason: &f&o"
                                          + reason));
                }
              } else {
                MessageUtils.sendMessage(
                    sender, MessageUtils.ERROR_COLOR + "Player is already banned.", true);
              }
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else {
            MessageUtils.sendMessage(
                p,
                MessageUtils.ERROR_COLOR
                    + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
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
