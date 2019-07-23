package net.timelegacy.tlcore.command;

import java.util.UUID;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.handler.FriendHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    Player p = (Player) sender;
    if (args.length == 0) {
      MessageUtils.sendMessage(sender, MessageUtils.MAIN_COLOR + "&lFriends", false);
      MessageUtils.helpMenu(sender, "/friends add <player>", "Add a friend as a player");
      MessageUtils.helpMenu(sender, "/friends remove <player>", "Remove one of your friends");

    } else if (args.length == 2) {
      if (args[0].equalsIgnoreCase("add")) {
        String username = args[1];
        if (PlayerHandler.playerExists(username)) {
          UUID request = PlayerHandler.getUUID(username);
          PlayerProfile profile = new PlayerProfile(request);

          PlayerProfile pp = new PlayerProfile(p.getUniqueId());

          if (FriendHandler.getFriends(p.getUniqueId()).size() < 32) {
            if (pp.getFriends().contains(request.toString())) {
              MessageUtils.sendMessage(
                  p, MessageUtils.ERROR_COLOR + "You are already friends with &o" + username, "");
            } else {
              if (pp.getFriendsPending().contains(request.toString())) {
                FriendHandler.acceptRequest(p.getUniqueId(), request);
                MessageUtils.sendMessage(
                    p,
                    MessageUtils.SUCCESS_COLOR
                        + "You already have a request from &o"
                        + username
                        + "&r"
                        + MessageUtils.SUCCESS_COLOR
                        + " and it has been accepted.",
                    "");
              } else if (!profile.getFriendsPending().contains(p.getUniqueId().toString())) {
                MessageUtils.sendMessage(
                    p,
                    MessageUtils.SUCCESS_COLOR + "You have sent a friend request to &o" + username,
                    "");
                FriendHandler.sendRequest(p.getUniqueId(), request);
              } else {
                MessageUtils.sendMessage(
                    p,
                    MessageUtils.ERROR_COLOR
                        + "You already have a pending request to &o"
                        + username,
                    "");
              }
            }
          } else {
            MessageUtils.sendMessage(
                p,
                MessageUtils.ERROR_COLOR
                    + "You have reached the maximum amount of friends. (This will be changed in the next update. Yay!)",
                "");
          }
        } else {
          MessageUtils.sendMessage(p, MessageUtils.ERROR_COLOR + "Player not found.", "");
        }

      } else if (args[0].equalsIgnoreCase("remove")) {
        String username = args[1];
        if (PlayerHandler.playerExists(username)) {
          UUID request = PlayerHandler.getUUID(username);
          PlayerProfile profile = new PlayerProfile(request);

          PlayerProfile pp = new PlayerProfile(p.getUniqueId());
          if (pp.getFriends().contains(request.toString())) {
            MessageUtils.sendMessage(
                p, MessageUtils.SUCCESS_COLOR + "You removed &o" + username + "&r" + MessageUtils.SUCCESS_COLOR
                    + " from your friends list.", "");

            FriendHandler.removeFriend(p.getUniqueId(), request);
          }
        }
      } else {
        MessageUtils.sendMessage(p, MessageUtils.ERROR_COLOR + "Player not found.", "");
      }

    } else {
      MessageUtils.sendMessage(sender, MessageUtils.ERROR_COLOR + "Type /friends for command help", true);
    }

    return false;
  }
}