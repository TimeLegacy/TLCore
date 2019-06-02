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

    if (sender instanceof Player) {

      Player p = (Player) sender;

      if (args.length == 0) {
        MessageUtils.sendMessage(p, "&7Usage: /chat [child/mature]", true);
      } else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("child")) {
          if (PerkHandler.hasPerk(p.getUniqueId(), "CHAT.MATURE")) {
            MessageUtils.sendMessage(
                p,
                MessageUtils.SUCCESS_COLOR + "You have set your chat to child friendly.",
                true);
            PerkHandler.removePerk(p.getUniqueId(), "CHAT.MATURE");
          } else {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "Your chat is already child friendly.", true);
          }
        } else if (args[0].equalsIgnoreCase("mature")) {
          if (!PerkHandler.hasPerk(p.getUniqueId(), "CHAT.MATURE")) {
            MessageUtils.sendMessage(
                p, MessageUtils.SUCCESS_COLOR + "You have set your chat to mature.", true);
            PerkHandler.addPerk(p.getUniqueId(), "CHAT.MATURE");
          } else {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "Your chat is already mature.", true);
          }
        }

      } else {
        MessageUtils.sendMessage(p, "&7Usage: /chat [child/mature]", true);
      }
    }

    return false;
  }
}
