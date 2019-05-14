package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Punishment;
import net.timelegacy.tlcore.handler.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BanCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

    if (sender instanceof Player) {

      Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 8) {

        if (args.length == 0) {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.ERROR_COLOR
                  + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
              true);
        }
        if (args.length >= 2
            && !core.punishmentHandler
            .comparePunishments(args[1])
            .toString()
            .equalsIgnoreCase("null")) {
          if (args.length == 2) {

            if (core.playerHandler.playerExistsName(args[0])) {

              if (!core.banHandler.isBanned(args[0])) {
                args[2] = "true";
                doThing(args, sender);
              } else {
                core.messageUtils.sendMessage(
                    sender, core.messageUtils.ERROR_COLOR + "Player is already banned.", true);
              }
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else if (args.length == 3) {

            if (core.playerHandler.playerExistsName(args[0])) {

              if (!core.banHandler.isBanned(args[0])) {
                doThing(args, sender);
              } else {
                core.messageUtils.sendMessage(
                    sender, core.messageUtils.ERROR_COLOR + "Player is already banned.", true);
              }
            } else {
              core.messageUtils.sendMessage(
                  sender, core.messageUtils.ERROR_COLOR + "Player not found.", true);
            }
          } else {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.ERROR_COLOR
                    + "Usage: /ban [player] [HACKING/PROFANITY/OTHER] [time #d/#m/#y (blank for permenent)]",
                true);
          }
        } else {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.ERROR_COLOR + "You must have the correct punishment type.",
              true);
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    }

    return false;
  }

  private void doThing(String[] args, CommandSender sender) {
    String reason = args[1].toUpperCase();

    Punishment banReason = core.punishmentHandler.comparePunishments(reason);

    String expire = args[2];

    core.banHandler.setBanned(args[0], expire, banReason, sender.getName());
    core.messageUtils.sendMessage(
        sender,
        core.messageUtils.SECOND_COLOR + args[0] + core.messageUtils.MAIN_COLOR + " is now banned.",
        true);

    Player pl = Bukkit.getPlayer(args[0]);
    if (pl != null) {
      core.bungeeUtils.kickPlayer(
          pl,
          ChatColor.translateAlternateColorCodes(
              '&',
              core.messageUtils.messagePrefix
                  + "&4You have been banned from the server. &cReason: &f&o"
                  + reason));
    }
  }
}
