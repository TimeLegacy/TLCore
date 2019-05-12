package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankManagementCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (args.length < 1 || args.length > 3) {
          core.messageUtils.sendMessage(
              sender, core.messageUtils.MAIN_COLOR + "&lRank Management", false);
          core.messageUtils.helpMenu(sender, "/rm get <player>", "View a player's rank");
          core.messageUtils.helpMenu(sender, "/rm set <player> <rank>", "Set a player's rank");
          core.messageUtils.helpMenu(
              sender, "/rm remove <player>", "Set a player's rank to DEFAULT");
          core.messageUtils.helpMenu(sender, "/rm list", "List the possible ranks");
        }

        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("list")) {
            core.messageUtils.sendMessage(
                sender, core.messageUtils.MAIN_COLOR + "&lRanks: ", false);
            for (Rank rank : core.rankHandler.rankList) {
              core.messageUtils.sendMessage(sender, rank.getMainColor() + rank.getName(), false);
            }
          }
        } else if (args.length == 2) {
          if (args[0].equalsIgnoreCase("get")) {
            String player = args[1];

            Rank rank = core.rankHandler.getRank(p.getName());

            if (!core.playerHandler.playerExistsName(player)) {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            } else {
              core.messageUtils.sendMessage(
                  sender,
                  core.messageUtils.SECOND_COLOR
                      + ""
                      + player
                      + core.messageUtils.MAIN_COLOR
                      + " is rank "
                      + core.messageUtils.SECOND_COLOR
                      + rank
                      + core.messageUtils.MAIN_COLOR
                      + ".",
                  true);
            }
          }

          if (args[0].equalsIgnoreCase("remove")) {
            String player = args[1];
            String uuid = core.playerHandler.getUUID(player);

            if (core.playerHandler.playerExistsName(player)) {

              core.rankHandler.removeRank(uuid);
              core.messageUtils.sendMessage(
                  sender,
                  core.messageUtils.SECOND_COLOR
                      + player
                      + core.messageUtils.MAIN_COLOR
                      + " has been set to rank "
                      + core.messageUtils.SECOND_COLOR
                      + "DEFAULT",
                  true);
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
        } else if (args.length == 3) {
          if (args[0].equalsIgnoreCase("set")) {
            String player = args[1];
            if (core.playerHandler.playerExistsName(player)) {

              String rank = args[2].toUpperCase();

              // DEFAULT&f, &d&oVIP&f, &d&oVIPPLUS&f, &d&oPREMIUM&f, &d&oYOUTUBER&f,
              // &d&oARCHITECT&f, &d&oHELPER&f, &d&oMODERATOR&f, &d&oADMIN&f, &d&oFOUNDER

              if (core.rankHandler.isValidRank(rank)) {

                core.rankHandler.setRank(player, rank);
                core.messageUtils.sendMessage(
                    sender,
                    core.messageUtils.SECOND_COLOR
                        + player
                        + core.messageUtils.MAIN_COLOR
                        + " has been set to rank "
                        + core.messageUtils.SECOND_COLOR
                        + rank,
                    true);

              } else {
                core.messageUtils.sendMessage(
                    sender, core.messageUtils.ERROR_COLOR + "Invalid rank.", true);
              }
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          }
        } else {

          core.messageUtils.sendMessage(
              sender, core.messageUtils.ERROR_COLOR + "Type /rm for command help", true);
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    }

    return false;
  }
}
