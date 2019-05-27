package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.CrateKeyHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateKeyCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player p = (Player) sender;
    Rank r = RankHandler.getRank(p.getName());
    if (r.getPriority() >= 9) {

      if (args.length < 2 || args.length > 3) {
        MessageUtils.sendMessage(
            sender, MessageUtils.MAIN_COLOR + "&lKey Management", false);
        MessageUtils.helpMenu(sender, "/ck get <player>", "View a player's amount of keys");
        MessageUtils.helpMenu(
            sender, "/ck set <player> <amount>", "Set a player's amount of keys");
        MessageUtils.helpMenu(sender, "/ck add <player> <amount>", "Give keys to a player");
        MessageUtils.helpMenu(sender, "/ck take <player> <amount>", "Take keys from a player");
      }

      if (args.length == 2) {
        if (args[0].equalsIgnoreCase("get")) {
          String player = args[1];
          Player uuid = Bukkit.getPlayer(player);
          PlayerHandler.playerExists(uuid);

          int coins = CrateKeyHandler.getBalance(uuid.getName());

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
                    + " keys.",
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

          CrateKeyHandler.setBalance(uuid.getName(), am);
          MessageUtils.sendMessage(
              sender,
              MessageUtils.SECOND_COLOR
                  + player
                  + MessageUtils.MAIN_COLOR
                  + " now has "
                  + MessageUtils.SECOND_COLOR
                  + am
                  + MessageUtils.MAIN_COLOR
                  + " keys.",
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
              CrateKeyHandler.addCrateKeys(Bukkit.getPlayer(player).getName(), am);
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " now has been given "
                      + MessageUtils.SECOND_COLOR
                      + am
                      + MessageUtils.MAIN_COLOR
                      + " keys.",
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
              CrateKeyHandler.removeCrateKeys(Bukkit.getPlayer(player).getName(), am);
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " now has lost "
                      + MessageUtils.SECOND_COLOR
                      + am
                      + MessageUtils.MAIN_COLOR
                      + " keys.",
                  true);
            } catch (NumberFormatException ex) {
              MessageUtils.sendMessage(sender, "&7Player not found.", true);
            }
          }
          return true;
        }
      }

      MessageUtils.sendMessage(
          sender, MessageUtils.ERROR_COLOR + "Type /ck for command help", true);
      return true;
    } else {
      MessageUtils.noPerm((Player) sender);
    }

    return true;
  }
}
