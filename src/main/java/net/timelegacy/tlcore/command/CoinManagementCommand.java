package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.CoinHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinManagementCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = RankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length < 2 || args.length > 3) {
        MessageUtils.sendMessage(
            sender, MessageUtils.MAIN_COLOR + "&lCoin Management", false);
        MessageUtils.helpMenu(sender, "/cm get <player>", "View a player's amount of coins");
        MessageUtils.helpMenu(
            sender, "/cm set <player> <amount>", "Set a player's amount of coins");
        MessageUtils.helpMenu(sender, "/cm add <player> <amount>", "Give coins to a player");
        MessageUtils.helpMenu(
            sender, "/cm take <player> <amount>", "Take coins from a player");
      }

      if (args.length == 2) {
        if (args[0].equalsIgnoreCase("get")) {
          String player = args[1];
          Player uuid = Bukkit.getPlayer(player);
          PlayerHandler.playerExists(uuid);

          int coins = CoinHandler.getBalance(p.getName());

          if (!PlayerHandler.playerExists(uuid)) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            MessageUtils.sendMessage(
                sender,
                MessageUtils.SECOND_COLOR
                    + player
                    + MessageUtils.MAIN_COLOR
                    + " has "
                    + MessageUtils.SECOND_COLOR
                    + coins
                    + MessageUtils.MAIN_COLOR
                    + " coins.",
                true);
          }

          return true;
        }
      } else if (args.length == 3) {
        if (args[0].equalsIgnoreCase("set")) {
          String player = args[1];
          Player uuid = Bukkit.getPlayer(player);
          PlayerHandler.playerExists(uuid);

          String amount = args[2];

          int am;

          try {
            am = Integer.parseInt(amount);
          } catch (NumberFormatException ex) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
            return true;
          }

          CoinHandler.setBalance(player, am);
          MessageUtils.sendMessage(
              sender,
              MessageUtils.SECOND_COLOR
                  + player
                  + MessageUtils.MAIN_COLOR
                  + " now has "
                  + MessageUtils.SECOND_COLOR
                  + am
                  + MessageUtils.MAIN_COLOR
                  + " coins.",
              true);

          return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
          String player = args[1];

          String amount = args[2];

          int am;

          if (!PlayerHandler.playerExists(Bukkit.getPlayer(player))) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          } else {
            try {
              am = Integer.parseInt(amount);
            } catch (NumberFormatException ex) {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
              return true;
            }

            try {
              CoinHandler.addCoins(player, am);
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " now has been given "
                      + MessageUtils.SECOND_COLOR
                      + am
                      + MessageUtils.MAIN_COLOR
                      + " coins.",
                  true);
            } catch (NumberFormatException ex) {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "That's not a number.", true);
            }
          }

          return true;
        }

        if (args[0].equalsIgnoreCase("take")) {
          String player = args[1];

          String amount = args[2];

          int am;

          if (!PlayerHandler.playerExists(Bukkit.getPlayer(player))) {
            MessageUtils.sendMessage(
                sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          } else {

            try {
              am = Integer.parseInt(amount);
            } catch (NumberFormatException ex) {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
              return true;
            }

            try {
              CoinHandler.removeCoins(player, am);
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " now has lost "
                      + MessageUtils.SECOND_COLOR
                      + am
                      + MessageUtils.SECOND_COLOR
                      + " coins.",
                  true);
            } catch (NumberFormatException ex) {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
          return true;
        }
      }

      MessageUtils.sendMessage(
          sender, MessageUtils.ERROR_COLOR + "Type /cm for command help", true);
      return true;
    } else {
      MessageUtils.noPerm((Player) sender);
    }

    return true;
  }
}
