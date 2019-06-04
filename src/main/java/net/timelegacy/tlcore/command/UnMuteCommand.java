package net.timelegacy.tlcore.command;

import java.util.UUID;
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
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /unmute [player]", true);
      return true;
    }

    if (args.length != 1) {
      MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Usage: /unmute [player]", true);
      return true;
    }

    if (!PlayerHandler.playerExists(args[0])) {
      MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
      return true;
    }

    UUID uuid = PlayerHandler.getUUID(args[0]);

    if (!MuteHandler.isMuted(uuid)) {
      MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player isn't muted.", true);
      return true;
    }

    MuteHandler.setMuted(uuid, "false", Punishment.NULL, player.getUniqueId());
    MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
            + args[0]
            + MessageUtils.MAIN_COLOR
            + " is now unmuted.",
        true);

    return false;
  }
}
