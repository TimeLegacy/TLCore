package net.timelegacy.tlcore.command;

import java.util.UUID;
import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.CoinHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinManagementCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {
      Player p = (Player) sender;
      Rank rank = RankHandler.getRank(p.getUniqueId());

      if (rank.getPriority() < 9) {
        MessageUtils.noPerm((Player) sender);
        return true;
      }
    }

    if (args.length < 2 || args.length > 3) {
      MessageUtils.sendMessage(sender, MessageUtils.MAIN_COLOR + "&lCoin Management", false);
      MessageUtils.helpMenu(sender, "/cm get <player>", "View a player's amount of coins");
      MessageUtils.helpMenu(sender, "/cm set <player> <amount>", "Set a player's amount of coins");
      MessageUtils.helpMenu(sender, "/cm add <player> <amount>", "Give coins to a player");
      MessageUtils.helpMenu(sender, "/cm take <player> <amount>", "Take coins from a player");
    }

    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("get")) {
        String player = args[1];

        if (!PlayerHandler.playerExists(player)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        int coins = CoinHandler.getBalance(PlayerHandler.getUUID(player));

        if (!PlayerHandler.playerExists(player)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
                + player
                + MessageUtils.MAIN_COLOR
                + " has "
                + MessageUtils.SECOND_COLOR
                + coins
                + MessageUtils.MAIN_COLOR
                + " coins.",
            true);

        return true;
      }

      return true;
    }

    if (args.length == 3) {
      if (args[0].equalsIgnoreCase("set")) {
        String player = args[1];

        if (!PlayerHandler.playerExists(player)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        String amount = args[2];
        int am;

        try {
          am = Integer.parseInt(amount);
        } catch (NumberFormatException ex) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
          return true;
        }

        UUID uuid = PlayerHandler.getUUID(player);

        CoinHandler.setBalance(uuid, am);
        MessageUtils.sendMessage(sender,
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

        if (!PlayerHandler.playerExists(player)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        try {
          am = Integer.parseInt(amount);
        } catch (NumberFormatException ex) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
          return true;
        }

        UUID uuid = PlayerHandler.getUUID(player);
        try {
          CoinHandler.addCoins(uuid, am);
          MessageUtils.sendMessage(sender,
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
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "That's not a number.", true);
        }

        return true;
      }

      if (args[0].equalsIgnoreCase("take")) {
        String player = args[1];
        String amount = args[2];
        int am;

        if (!PlayerHandler.playerExists(player)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        try {
          am = Integer.parseInt(amount);
        } catch (NumberFormatException ex) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Invalid amount.", true);
          return true;
        }

        try {
          UUID uuid = PlayerHandler.getUUID(player);
          CoinHandler.removeCoins(uuid, am);
          MessageUtils.sendMessage(sender,
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
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
        }

        return true;
      }
    }

    MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Type /cm for command help", true);
    return true;
  }
}
