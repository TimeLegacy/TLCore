package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.handler.PerkHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatTypeCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player player = (Player) sender;

    if (args.length == 0) {
      MessageUtils.sendMessage(player, "&7Usage: /chat [child/mature]", true);
      return true;
    }

    if (args.length != 1) {
      MessageUtils.sendMessage(player, "&7Usage: /chat [child/mature]", true);
      return true;
    }

    if (args[0].equalsIgnoreCase("child")) {
      if (PerkHandler.hasPerk(player.getUniqueId(), "CHAT.MATURE")) {
        MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "You have set your chat to child friendly.", true);
        PerkHandler.removePerk(player.getUniqueId(), "CHAT.MATURE");
      } else {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Your chat is already child friendly.", true);
      }

      return true;
    }

    if (args[0].equalsIgnoreCase("mature")) {
      if (!PerkHandler.hasPerk(player.getUniqueId(), "CHAT.MATURE")) {
        MessageUtils.sendMessage(player, MessageUtils.SUCCESS_COLOR + "You have set your chat to mature.", true);
        PerkHandler.addPerk(player.getUniqueId(), "CHAT.MATURE");
      } else {
        MessageUtils.sendMessage(player, MessageUtils.ERROR_COLOR + "Your chat is already mature.", true);
      }

      return true;
    }

    return false;
  }
}
