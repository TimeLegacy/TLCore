package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ListCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (Bukkit.getOnlinePlayers().size() == 0) {
      MessageUtils.sendMessage(
          sender, MessageUtils.ERROR_COLOR + "There are no players online.", true);
    } else {
      StringBuilder sb = new StringBuilder();

      int count = 0;
      for (Player p : Bukkit.getOnlinePlayers()) {
        count = count + 1;

        if (count == Bukkit.getOnlinePlayers().size()) {
          sb.append("&f" + p.getDisplayName() + "&8.");
        } else {
          sb.append("&f" + p.getDisplayName() + "&8, ");
        }
      }

      MessageUtils.sendMessage(
          sender,
          MessageUtils.MAIN_COLOR
              + "Online Players ("
              + MessageUtils.SECOND_COLOR
              + Bukkit.getOnlinePlayers().size()
              + MessageUtils.MAIN_COLOR
              + ") "
              + MessageUtils.SECOND_COLOR
              + sb.toString(),
          true);
    }

    return false;
  }
}
