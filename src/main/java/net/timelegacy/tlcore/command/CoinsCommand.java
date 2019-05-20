package net.timelegacy.tlcore.command;

import java.util.logging.Level;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.CoinHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CoinsCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      int coins = 0;
      coins = CoinHandler.getBalance(p.getName());

      if (coins == 1) {
        MessageUtils.sendMessage(
            p,
            MessageUtils.MAIN_COLOR
                + "You have "
                + MessageUtils.SECOND_COLOR
                + coins
                + MessageUtils.MAIN_COLOR
                + " coin.",
            true);
      } else if (coins > 1) {
        MessageUtils.sendMessage(
            p,
            MessageUtils.MAIN_COLOR
                + "You have "
                + MessageUtils.SECOND_COLOR
                + coins
                + MessageUtils.MAIN_COLOR
                + " coins.",
            true);
      } else if (coins < 1) {
        MessageUtils.sendMessage(
            p,
            MessageUtils.MAIN_COLOR
                + "You have "
                + MessageUtils.SECOND_COLOR
                + coins
                + MessageUtils.MAIN_COLOR
                + " coins.",
            true);
      }

      return true;
    } else {
      return false;
    }
  }
}
