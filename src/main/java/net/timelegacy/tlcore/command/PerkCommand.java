package net.timelegacy.tlcore.command;

import java.util.List;
import net.timelegacy.tlcore.handler.PerkHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player) || RankHandler.getRank(((Player) sender).getUniqueId()).getPriority() > 8) {
      mainLogic(sender, cmd, commandLabel, args);
    }
    //TODO return command not found if the player doesn't have perms
    return true;
  }

  private void help(CommandSender sender) {
    MessageUtils.sendMessage(sender, MessageUtils.MAIN_COLOR + "&lPerk Management", false);
    MessageUtils.helpMenu(sender, "/perks get <player>", "View all of said player's perks");
    MessageUtils.helpMenu(sender, "/perks add <player> <perk> <perk>", "Adds perks to player");
    MessageUtils.helpMenu(sender, "/perks remove <player> <perk> <perk>", "Removes perks from player");
    MessageUtils.helpMenu(sender, "/perks test <player> <perk> <perk>", "tests if player has said perk");
  }

  private void mainLogic(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (args.length < 1 || args[0].toLowerCase().equals("help")) {
      help(sender);
      return;
    }

    if (args[0].equals("get")) {
      if (args.length > 2) {
        help(sender);
        return;
      }

      List<String> requestedPlayersPerks = PerkHandler.getPerks(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
      if (requestedPlayersPerks == null || requestedPlayersPerks.size() == 0) {
        MessageUtils.sendMessage(sender, "Player " + args[1] + " does not have any perks", false);
        return;
      }

      MessageUtils.sendMessage(sender, "Player: " + args[1], false);
      for (String requestedPlayersPerk : requestedPlayersPerks) {
        MessageUtils.sendMessage(sender, requestedPlayersPerk, false);
      }

      return;
    }

    if (args[0].equals("add")) {
      if (args.length < 3) {
        help(sender);
        return;
      }

      if (RankHandler.getRank(Bukkit.getOfflinePlayer(args[1]).getUniqueId()) == null) {
        MessageUtils.sendMessage(sender, "Player " + args[1] + "seems to not exist, typo maybe?", false);
        return;
      }

      StringBuilder perksAdded = new StringBuilder();
      for (int i = 2; i < args.length; i++) {
        PerkHandler.addPerk(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), args[i]);
        perksAdded.append(" ").append(args[i]);
      }

      MessageUtils.sendMessage(sender, "removed perks:" + perksAdded, false);
      return;
    }

    if (args[0].equals("remove")) {
      if (args.length < 3) {
        help(sender);
        return;
      }

      if (RankHandler.getRank(Bukkit.getOfflinePlayer(args[1]).getUniqueId()) == null) {
        MessageUtils.sendMessage(sender, "Player " + args[1] + "seems to not exist, typo maybe?", false);
        return;
      }

      StringBuilder perksRemoved = new StringBuilder();
      for (int i = 2; i < args.length; i++) {
        PerkHandler.removePerk(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), args[i]);
        perksRemoved.append(" ").append(args[i]);
      }

      MessageUtils.sendMessage(sender, "removed perks: " + perksRemoved, false);
      return;
    }

    if (args[0].equals("test")) {
      if (args.length < 3) {
        help(sender);
        return;
      }

      List<String> requestedPlayersPerks = PerkHandler.getPerks(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
      if (requestedPlayersPerks == null || requestedPlayersPerks.size() == 0) {
        MessageUtils.sendMessage(sender, "Player " + args[1] + " does not have any perks", false);
        return;
      }

      MessageUtils.sendMessage(sender, "Player: " + args[2], false);
      for (int i = 2; i < args.length; i++) {
        MessageUtils.sendMessage(sender, args[i] + ": " + requestedPlayersPerks.contains(args[i]), false);
      }

      return;
    }

    help(sender);
  }
}