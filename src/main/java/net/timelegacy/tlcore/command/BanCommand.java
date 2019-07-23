package net.timelegacy.tlcore.command;

import java.util.UUID;
import net.timelegacy.tlcore.datatype.Punishment;
import net.timelegacy.tlcore.datatype.Punishment.Type;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
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

    if (rank.getPriority() < 6) {
      MessageUtils.noPerm(player);
      return true;
    }

    if (args.length == 0) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
          + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
    } else if (args.length == 2) {

      if (rank.getPriority() < 7) {
        MessageUtils.noPerm(player);
        return true;
      }

      String username = args[0];

      if (!PlayerHandler.playerExists(username)) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Player not found. Please check the username. (Case-Sensitive)", true);
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
        return true;
      }

      UUID targetUUID = PlayerHandler.getUUID(username);

      if (!Punishment.validReason(args[1])) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Invalid punishment reason.", true);
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
        return true;
      }

      Punishment.Reason reason = Punishment.Reason.valueOf(args[1]);

      Punishment punishment = new Punishment(targetUUID);

      punishment.punish(Type.BAN, reason, "NEVER", player.getUniqueId());
      MessageUtils.sendMessage(sender,
          MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.", true);

    } else if (args.length == 3) {
      String username = args[0];

      if (!PlayerHandler.playerExists(username)) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Player not found. Please check the username. (Case-Sensitive)", true);
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
        return true;
      }

      UUID targetUUID = PlayerHandler.getUUID(username);

      if (!Punishment.validReason(args[1])) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Invalid punishment reason.", true);
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
        return true;
      }

      Punishment.Reason reason = Punishment.Reason.valueOf(args[1]);

      Punishment punishment = new Punishment(targetUUID);

      String expire = args[2];

      if (Punishment.parseExpire(expire) == 0) {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Invalid expire time.", true);
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
            + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
        return true;
      }

      punishment.punish(Type.BAN, reason, expire, player.getUniqueId());
      MessageUtils.sendMessage(sender,
          MessageUtils.SECOND_COLOR + args[0] + MessageUtils.MAIN_COLOR + " is now banned.", true);

    } else {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR
          + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]", true);
    }

    return false;
  }
}
