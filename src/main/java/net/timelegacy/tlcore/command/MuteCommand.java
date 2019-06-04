package net.timelegacy.tlcore.command;

import java.util.UUID;
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

public class MuteCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;
    Rank rank = RankHandler.getRank(player.getUniqueId());

    if (rank.getPriority() < 7) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (args.length == 0) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
          + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
      return true;
    }

    if (args.length >= 2 && !PunishmentHandler.comparePunishments(args[1]).toString().equalsIgnoreCase("null")) {
      Punishment muteReason = PunishmentHandler.comparePunishments(args[1]);
      if (args.length == 2) {

        if (!PlayerHandler.playerExists(args[0])) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        UUID uuid = PlayerHandler.getUUID(args[0]);

        if (MuteHandler.isMuted(uuid)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player is already muted.", true);
          return true;
        }

        String expire = "true";

        MuteHandler.setMuted(uuid, expire, muteReason, player.getUniqueId());
        MessageUtils.sendMessage(
            sender,
            MessageUtils.SECOND_COLOR
                + args[0]
                + MessageUtils.MAIN_COLOR
                + " is now muted.",
            true);
        return true;
      }

      if (args.length == 3) {
        if (!PlayerHandler.playerExists(args[0])) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        UUID uuid = PlayerHandler.getUUID(args[0]);
        if (MuteHandler.isMuted(uuid)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player is already muted.", true);
          return true;
        }

        String expire = args[2];

        MuteHandler.setMuted(uuid, expire, muteReason, player.getUniqueId());
        MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
                + args[0]
                + MessageUtils.MAIN_COLOR
                + " is now muted.",
            true);
      } else {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /mute [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
      }
    }

    return false;
  }
}
