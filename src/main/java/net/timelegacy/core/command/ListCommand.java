package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ListCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (Bukkit.getOnlinePlayers().size() == 0) {
      core.messageUtils.sendMessage(
          sender, core.messageUtils.ERROR_COLOR + "There are no players online.", true);
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

      core.messageUtils.sendMessage(
          sender,
          core.messageUtils.MAIN_COLOR
              + "Online Players ("
              + core.messageUtils.SECOND_COLOR
              + Bukkit.getOnlinePlayers().size()
              + core.messageUtils.MAIN_COLOR
              + ") "
              + core.messageUtils.SECOND_COLOR
              + sb.toString(),
          true);
    }

    return false;
  }
}
