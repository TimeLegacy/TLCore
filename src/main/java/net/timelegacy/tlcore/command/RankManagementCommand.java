package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.Rank;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankManagementCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = RankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (args.length < 1 || args.length > 3) {
          MessageUtils.sendMessage(
              sender, MessageUtils.MAIN_COLOR + "&lRank Management", false);
          MessageUtils.helpMenu(sender, "/rm get <player>", "View a player's rank");
          MessageUtils.helpMenu(sender, "/rm set <player> <rank>", "Set a player's rank");
          MessageUtils.helpMenu(
              sender, "/rm remove <player>", "Set a player's rank to DEFAULT");
          MessageUtils.helpMenu(sender, "/rm list", "List the possible ranks");
        }

        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("list")) {
            MessageUtils.sendMessage(
                sender, MessageUtils.MAIN_COLOR + "&lRanks: ", false);
            for (Rank rank : RankHandler.rankList) {
              MessageUtils.sendMessage(sender, rank.getPrimaryColor() + rank.getName(), false);
            }
          }
        } else if (args.length == 2) {
          if (args[0].equalsIgnoreCase("get")) {
            String player = args[1];

            Rank rank = RankHandler.getRank(p.getName());

            if (!PlayerHandler.playerExistsName(player)) {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            } else {
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + ""
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " is rank "
                      + MessageUtils.SECOND_COLOR
                      + rank
                      + MessageUtils.MAIN_COLOR
                      + ".",
                  true);
            }
          }

          if (args[0].equalsIgnoreCase("remove")) {
            String player = args[1];
            String uuid = PlayerHandler.getUUID(player);

            if (PlayerHandler.playerExistsName(player)) {

              RankHandler.removeRank(uuid);
              MessageUtils.sendMessage(
                  sender,
                  MessageUtils.SECOND_COLOR
                      + player
                      + MessageUtils.MAIN_COLOR
                      + " has been set to rank "
                      + MessageUtils.SECOND_COLOR
                      + "DEFAULT",
                  true);
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
        } else if (args.length == 3) {
          if (args[0].equalsIgnoreCase("set")) {
            String player = args[1];
            if (PlayerHandler.playerExistsName(player)) {

              String rank = args[2].toUpperCase();

              // DEFAULT&f, &d&oVIP&f, &d&oVIPPLUS&f, &d&oPREMIUM&f, &d&oYOUTUBER&f,
              // &d&oARCHITECT&f, &d&oHELPER&f, &d&oMODERATOR&f, &d&oADMIN&f, &d&oFOUNDER

              if (RankHandler.isValidRank(rank)) {

                RankHandler.setRank(player, rank);
                MessageUtils.sendMessage(
                    sender,
                    MessageUtils.SECOND_COLOR
                        + player
                        + MessageUtils.MAIN_COLOR
                        + " has been set to rank "
                        + MessageUtils.SECOND_COLOR
                        + rank,
                    true);

              } else {
                MessageUtils.sendMessage(
                    sender, MessageUtils.ERROR_COLOR + "Invalid rank.", true);
              }
            } else {
              MessageUtils.sendMessage(
                  sender, MessageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
        } else {

          MessageUtils.sendMessage(
              sender, MessageUtils.ERROR_COLOR + "Type /rm for command help", true);
        }
      } else {
        MessageUtils.noPerm(p);
      }
    }

    return false;
  }
}
