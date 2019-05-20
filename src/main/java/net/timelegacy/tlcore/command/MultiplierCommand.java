package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.MultiplierHandler;
import net.timelegacy.tlcore.handler.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class MultiplierCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = RankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length < 1 || args.length > 2) {
        MessageUtils.sendMessage(
            sender, MessageUtils.MAIN_COLOR + "&lMultiplier Management help menu", false);
        MessageUtils.helpMenu(sender, "/mm check", "Check the current multiplier status");
        MessageUtils.helpMenu(sender, "/mm enable", "Enable the multipler");
        MessageUtils.helpMenu(sender, "/mm disable", "Disable the multipler");
        MessageUtils.helpMenu(sender, "/mm set <amount>", "Set the multipler");
      }

      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("check")) {

          String status = null;

          if (MultiplierHandler.isMultiplierEnabled()) {
            status = "Enabled";
          } else {
            status = "Disabled";
          }

          MessageUtils.sendMessage(
              sender,
              MessageUtils.MAIN_COLOR
                  + "Multiplier is currently"
                  + " "
                  + MessageUtils.SECOND_COLOR
                  + status
                  + MessageUtils.MAIN_COLOR
                  + " and the multiplier amount is "
                  + MessageUtils.SECOND_COLOR
                  + MultiplierHandler.getMultiplier(),
              true);

          return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {

          if (MultiplierHandler.isMultiplierEnabled()) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Multiplier is already enabled.", true);
          } else {
            MessageUtils.sendMessage(
                sender, MessageUtils.SUCCESS_COLOR + "Multiplier is now enabled.", true);

            MultiplierHandler.toggleMultiplier(true);
          }

          return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {

          if (MultiplierHandler.isMultiplierEnabled()) {
            MessageUtils.sendMessage(
                sender, MessageUtils.SUCCESS_COLOR + "Multiplier is now disabled.", true);

            MultiplierHandler.toggleMultiplier(false);
          } else {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Multiplier is already disabled.", true);
          }

          return true;
        }

        // other command
      } else if (args.length == 2) {
        if (args[0].equalsIgnoreCase("set")) {

          String amount = args[1];

          int am = 0;

          try {
            am = Integer.parseInt(amount);
          } catch (NumberFormatException ex) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
            return true;
          }

          MultiplierHandler.setMultiplier(am);
          MessageUtils.sendMessage(
              sender,
              MessageUtils.MAIN_COLOR
                  + "Multiplier amount set to "
                  + MessageUtils.SECOND_COLOR
                  + am
                  + "x",
              true);

          return true;
        }
      }

      MessageUtils.sendMessage(
          sender, MessageUtils.ERROR_COLOR + "Type /mm for command help", true);
      return true;
    } else {
      MessageUtils.noPerm((Player) sender);
    }

    return true;
  }
}
