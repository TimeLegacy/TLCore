package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.datatype.Rank;
import net.timelegacy.tlcore.handler.CrateKeyHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateKeyCommand implements CommandExecutor {

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
      MessageUtils.sendMessage(sender, MessageUtils.MAIN_COLOR + "&lKey Management", false);
      MessageUtils.helpMenu(sender, "/ck get <player>", "View a player's amount of keys");
      MessageUtils.helpMenu(sender, "/ck set <player> <amount>", "Set a player's amount of keys");
      MessageUtils.helpMenu(sender, "/ck add <player> <amount>", "Give keys to a player");
      MessageUtils.helpMenu(sender, "/ck take <player> <amount>", "Take keys from a player");
    }

    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("get")) {
        String target = args[1];

        if (!PlayerHandler.playerExists(target)) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        int coins = CrateKeyHandler.getBalance(PlayerHandler.getUUID(target));

        if (!PlayerHandler.playerExists(PlayerHandler.getUUID(target))) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
          return true;
        }

        MessageUtils.sendMessage(sender,
            MessageUtils.SECOND_COLOR
                + target
                + MessageUtils.MAIN_COLOR
                + " has "
                + MessageUtils.SECOND_COLOR
                + coins
                + MessageUtils.MAIN_COLOR
                + " keys.",
            true);

        return true;
      }

      return true;
    }

    if (args.length == 3) {
      if (args[0].equalsIgnoreCase("set")) {
        String target = args[1];

        if (!PlayerHandler.playerExists(target)) {
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

        CrateKeyHandler.setBalance(PlayerHandler.getUUID(target), am);
        MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
                + target
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
        String target = args[1];
        String amount = args[2];
        int am;

        if (!PlayerHandler.playerExists(target)) {
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
          CrateKeyHandler.addKeys(PlayerHandler.getUUID(target), am);
          MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
                  + target
                  + MessageUtils.MAIN_COLOR
                  + " now has been given "
                  + MessageUtils.SECOND_COLOR
                  + am
                  + MessageUtils.MAIN_COLOR
                  + " keys.",
              true);
        } catch (NumberFormatException ex) {
          MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "That's not a number.", true);
        }

        return true;
      }

      if (args[0].equalsIgnoreCase("take")) {
        String target = args[1];
        String amount = args[2];
        int am;

        if (!PlayerHandler.playerExists(target)) {
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
          CrateKeyHandler.removeKeys(PlayerHandler.getUUID(target), am);
          MessageUtils.sendMessage(sender, MessageUtils.SECOND_COLOR
                  + target
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

        return true;
      }
    }

    MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Type /ck for command help", true);
    return true;
  }
}
