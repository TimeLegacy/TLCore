package net.timelegacy.tlcore.command;

import java.util.logging.Level;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CoinsCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      int coins = 0;
      coins = core.coinHandler.getBalance(p.getName());

      if (coins == 1) {
        core.messageUtils.sendMessage(
            p,
            core.messageUtils.MAIN_COLOR
                + "You have "
                + core.messageUtils.SECOND_COLOR
                + coins
                + core.messageUtils.MAIN_COLOR
                + " coin.",
            true);
      } else if (coins > 1) {
        core.messageUtils.sendMessage(
            p,
            core.messageUtils.MAIN_COLOR
                + "You have "
                + core.messageUtils.SECOND_COLOR
                + coins
                + core.messageUtils.MAIN_COLOR
                + " coins.",
            true);
      } else if (coins < 1) {
        core.messageUtils.sendMessage(
            p,
            core.messageUtils.MAIN_COLOR
                + "You have "
                + core.messageUtils.SECOND_COLOR
                + coins
                + core.messageUtils.MAIN_COLOR
                + " coins.",
            true);
      }

      return true;
    } else {
      core.log("Console has infinite coins! (Just kidding...)", Level.WARNING);
      return false;
    }
  }
}
