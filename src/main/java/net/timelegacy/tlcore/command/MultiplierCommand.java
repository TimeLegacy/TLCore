package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class MultiplierCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = core.rankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length < 1 || args.length > 2) {
        core.messageUtils.sendMessage(
            sender, core.messageUtils.MAIN_COLOR + "&lMultiplier Management help menu", false);
        core.messageUtils.helpMenu(sender, "/mm check", "Check the current multiplier status");
        core.messageUtils.helpMenu(sender, "/mm enable", "Enable the multipler");
        core.messageUtils.helpMenu(sender, "/mm disable", "Disable the multipler");
        core.messageUtils.helpMenu(sender, "/mm set <amount>", "Set the multipler");
      }

      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("check")) {

          String status = null;

          if (core.multiplierHandler.isMultiplierEnabled()) {
            status = "Enabled";
          } else {
            status = "Disabled";
          }

          core.messageUtils.sendMessage(
              sender,
              core.messageUtils.MAIN_COLOR
                  + "Multiplier is currently"
                  + " "
                  + core.messageUtils.SECOND_COLOR
                  + status
                  + core.messageUtils.MAIN_COLOR
                  + " and the multiplier amount is "
                  + core.messageUtils.SECOND_COLOR
                  + core.multiplierHandler.getMultiplier(),
              true);

          return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {

          if (core.multiplierHandler.isMultiplierEnabled()) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Multiplier is already enabled.", true);
          } else {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.SUCCESS_COLOR + "Multiplier is now enabled.", true);

            core.multiplierHandler.enableMultiplier(true);
          }

          return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {

          if (core.multiplierHandler.isMultiplierEnabled()) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.SUCCESS_COLOR + "Multiplier is now disabled.", true);

            core.multiplierHandler.enableMultiplier(false);
          } else {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Multiplier is already disabled.", true);
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
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Invalid amount.", true);
            return true;
          }

          core.multiplierHandler.setMultiplier(am);
          core.messageUtils.sendMessage(
              sender,
              core.messageUtils.MAIN_COLOR
                  + "Multiplier amount set to "
                  + core.messageUtils.SECOND_COLOR
                  + am
                  + "x",
              true);

          return true;
        }
      }

      core.messageUtils.sendMessage(
          sender, core.messageUtils.ERROR_COLOR + "Type /mm for command help", true);
      return true;
    } else {
      core.messageUtils.noPerm((Player) sender);
    }

    return true;
  }
}
