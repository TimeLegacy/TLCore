package net.timelegacy.tlcore.command;

import java.util.UUID;
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
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 8) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (args.length == 0) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
          + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
      return true;
    }

    if (args.length >= 2 && !PunishmentHandler.comparePunishments(args[1]).toString().equalsIgnoreCase("null")) {
      if (args.length == 2) {

        if (!PlayerHandler.playerExists(args[0])) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        UUID target = PlayerHandler.getUUID(args[0]);
        if (BanHandler.isBanned(target)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player is already banned.", true);
          return true;
        }

        String reason = args[1].toUpperCase();

        Punishment banReason = PunishmentHandler.comparePunishments(reason);

        String expire = "true";

        BanHandler.setBanned(target, expire, banReason, player.getUniqueId());
        MessageUtils.sendMessage(sender,
            MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.", true);

        Player pl = Bukkit.getPlayer(args[0]);
        if (pl != null) {
          BungeeUtils.kickPlayer(pl, ChatColor.translateAlternateColorCodes('&',
              MessageUtils.messagePrefix
                  + "&4You have been banned from the server. &cReason: &f&o"
                  + reason));
        }

        return true;
      }

      if (args.length == 3) {
        if (!PlayerHandler.playerExists(args[0])) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        UUID target = PlayerHandler.getUUID(args[0]);

        if (BanHandler.isBanned(target)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player is already banned.", true);
          return true;
        }

        String reason = args[1].toUpperCase();

        Punishment banReason = PunishmentHandler.comparePunishments(reason);

        String expire = args[2];

        BanHandler.setBanned(target, expire, banReason, player.getUniqueId());
        MessageUtils.sendMessage(sender,
            MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.", true);

        Player pl = Bukkit.getPlayer(args[0]);
        if (pl != null) {
          BungeeUtils.kickPlayer(pl, ChatColor.translateAlternateColorCodes('&',
              MessageUtils.messagePrefix
                  + "&4You have been banned from the server. &cReason: &f&o"
                  + reason));
        }

      } else {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
      }
    }

    return false;
  }
}
