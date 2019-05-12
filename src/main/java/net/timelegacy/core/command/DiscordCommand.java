package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 * Created by Batman on 2016-06-26.
 */
public class DiscordCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      // link, relink

      if (args.length >= 1) {
        if (args[0].equalsIgnoreCase("link")) {
          if (core.discordHandler.getDiscordID(p).isEmpty()
              && core.discordHandler.getDiscordToken(p).isEmpty()) {
            String token = core.messageUtils.randomAlphaNumeric(8);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.SUCCESS_COLOR + "You have generated your linking token.",
                true);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR + "Token: " + core.messageUtils.SECOND_COLOR + token,
                true);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR
                    + "Please join our Discord and type "
                    + core.messageUtils.SECOND_COLOR
                    + " !link "
                    + token,
                true);

            core.discordHandler.updateDiscordToken(p, token);
          } else if (!core.discordHandler.getDiscordID(p).isEmpty()) {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.ERROR_COLOR
                    + "Your Minecraft account is already linked to a Discord account.",
                true);
          } else if (!core.discordHandler.getDiscordToken(p).isEmpty()
              && core.discordHandler.getDiscordID(p).isEmpty()) {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.ERROR_COLOR
                    + "You have not linked your account, but you have a token generated.",
                true);
            String token = core.discordHandler.getDiscordToken(p);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR + "Token: " + core.messageUtils.SECOND_COLOR + token,
                true);
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.MAIN_COLOR
                    + "Please join our Discord and type "
                    + core.messageUtils.SECOND_COLOR
                    + " !link "
                    + token,
                true);
          } else {
            core.messageUtils.sendMessage(
                p, core.messageUtils.ERROR_COLOR + "Error. Contact an administrator.", true);
          }
        } else if (args[0].equalsIgnoreCase("status")) {
          if (!core.discordHandler.getDiscordID(p).isEmpty()) {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.SUCCESS_COLOR
                    + "Success your Minecraft account is linked to our Discord.",
                true);
          } else {
            core.messageUtils.sendMessage(
                p,
                core.messageUtils.ERROR_COLOR
                    + "Your Minecraft is not linked to Discord. Type /discord for help.",
                true);
          }

        } else if (args[0].equalsIgnoreCase("join")) {
          core.messageUtils.sendMessage(
              p,
              core.messageUtils.MAIN_COLOR
                  + "Click the link to join!"
                  + core.messageUtils.SECOND_COLOR
                  + " https://mineaqua.org",
              true);
        }
      } else {
        core.messageUtils.sendMessage(sender, core.messageUtils.MAIN_COLOR + "&lDiscord", false);
        core.messageUtils.helpMenu(
            sender, "/discord link", "Link your Discord account to your Minecraft.");
        core.messageUtils.helpMenu(
            sender, "/discord status", "Check if your Discord was linked successfully.");
        core.messageUtils.helpMenu(sender, "/discord join", "Get the invite link for our Discord.");
      }
    }

    return false;
  }
}
