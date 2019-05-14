package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinManagementCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = core.rankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length < 2 || args.length > 3) {
        core.messageUtils.sendMessage(
            sender, core.messageUtils.MAIN_COLOR + "&lCoin Management", false);
        core.messageUtils.helpMenu(sender, "/cm get <player>", "View a player's amount of coins");
        core.messageUtils.helpMenu(
            sender, "/cm set <player> <amount>", "Set a player's amount of coins");
        core.messageUtils.helpMenu(sender, "/cm add <player> <amount>", "Give coins to a player");
        core.messageUtils.helpMenu(
            sender, "/cm take <player> <amount>", "Take coins from a player");
      }

      if (args.length == 2) {
        if (args[0].equalsIgnoreCase("get")) {
          String player = args[1];
          Player uuid = Bukkit.getPlayer(player);
          core.playerHandler.playerExists(uuid);

          int coins = core.coinHandler.getBalance(p.getName());

          if (!core.playerHandler.playerExists(uuid)) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            core.messageUtils.sendMessage(
                sender,
                core.messageUtils.SECOND_COLOR
                    + player
                    + core.messageUtils.MAIN_COLOR
                    + " has "
                    + core.messageUtils.SECOND_COLOR
                    + coins
                    + core.messageUtils.MAIN_COLOR
                    + " coins.",
                true);
          }

          return true;
        }
      } else if (args.length == 3) {
        if (args[0].equalsIgnoreCase("set")) {
          String player = args[1];
          Player uuid = Bukkit.getPlayer(player);
          core.playerHandler.playerExists(uuid);

          String amount = args[2];

          int am;

          try {
            am = Integer.parseInt(amount);
          } catch (NumberFormatException ex) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Invalid amount.", true);
            return true;
          }

          core.coinHandler.setBalance(player, am);
          core.messageUtils.sendMessage(
              sender,
              core.messageUtils.SECOND_COLOR
                  + player
                  + core.messageUtils.MAIN_COLOR
                  + " now has "
                  + core.messageUtils.SECOND_COLOR
                  + am
                  + core.messageUtils.MAIN_COLOR
                  + " coins.",
              true);

          return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
          String player = args[1];

          String amount = args[2];

          int am;

          if (!core.playerHandler.playerExists(Bukkit.getPlayer(player))) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            try {
              am = Integer.parseInt(amount);
            } catch (NumberFormatException ex) {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Invalid amount.", true);
              return true;
            }

            try {
              core.coinHandler.addCoins(player, am);
              core.messageUtils.sendMessage(
                  sender,
                  core.messageUtils.SECOND_COLOR
                      + player
                      + core.messageUtils.MAIN_COLOR
                      + " now has been given "
                      + core.messageUtils.SECOND_COLOR
                      + am
                      + core.messageUtils.MAIN_COLOR
                      + " coins.",
                  true);
            } catch (NumberFormatException ex) {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "That's not a number.", true);
            }
          }

          return true;
        }

        if (args[0].equalsIgnoreCase("take")) {
          String player = args[1];

          String amount = args[2];

          int am;

          if (!core.playerHandler.playerExists(Bukkit.getPlayer(player))) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
          } else {

            try {
              am = Integer.parseInt(amount);
            } catch (NumberFormatException ex) {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Invalid amount.", true);
              return true;
            }

            try {
              core.coinHandler.removeCoins(player, am);
              core.messageUtils.sendMessage(
                  sender,
                  core.messageUtils.SECOND_COLOR
                      + player
                      + core.messageUtils.MAIN_COLOR
                      + " now has lost "
                      + core.messageUtils.SECOND_COLOR
                      + am
                      + core.messageUtils.SECOND_COLOR
                      + " coins.",
                  true);
            } catch (NumberFormatException ex) {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
          return true;
        }
      }

      core.messageUtils.sendMessage(
          sender, core.messageUtils.ERROR_COLOR + "Type /cm for command help", true);
      return true;
    } else {
      core.messageUtils.noPerm((Player) sender);
    }

    return true;
  }
}
