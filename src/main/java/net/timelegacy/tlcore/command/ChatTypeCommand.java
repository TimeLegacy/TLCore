package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatTypeCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      if (args.length == 0) {
        core.messageUtils.sendMessage(p, "&7Usage: /chat [child/mature]", true);
      } else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("child")) {
          if (core.perkHandler.hasPerk(p, "CHAT.MATURE")) {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.SUCCESS_COLOR + "You have set your chat to child friendly.",
                true);
            core.perkHandler.removePerk(p, "CHAT.MATURE");
          } else {
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Your chat is already child friendly.", true);
          }
        } else if (args[0].equalsIgnoreCase("mature")) {
          if (!core.perkHandler.hasPerk(p, "CHAT.MATURE")) {
            core.messageUtils.sendMessage(
                p, core.messageUtils.SUCCESS_COLOR + "You have set your chat to mature.", true);
            core.perkHandler.addPerk(p, "CHAT.MATURE");
          } else {
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Your chat is already mature.", true);
          }
        }

      } else {
        core.messageUtils.sendMessage(p, "&7Usage: /chat [child/mature]", true);
      }
    }

    return false;
  }
}
