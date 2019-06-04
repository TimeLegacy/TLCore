package net.timelegacy.tlcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

  private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  public static String messagePrefix = "";
  public static String MAIN_COLOR = "&e";
  public static String SECOND_COLOR = "&6";
  public static String SUCCESS_COLOR = "&a";
  public static String ERROR_COLOR = "&c";
  public static String noPermission = "&cYou do not have permission to perform this action!";

  /**
   * No permission error message
   *
   * @param player player
   */
  public static void noPerm(Player player) {
    sendMessage(player, noPermission, false);
  }

  /**
   * Send message
   *
   * @param player player
   * @param message message
   * @param usePrefix to use prefix
   */
  public static void sendMessage(Player player, String message, Boolean usePrefix) {
    if (player == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    player.sendMessage(colorize(message));
  }

  /**
   * Send message
   *
   * @param player player
   * @param message message
   * @param prefix custom prefix
   */
  public static void sendMessage(Player player, String message, String prefix) {
    if (player == null || message == null) {
      return;
    }

    player.sendMessage(colorize(prefix + message));
  }

  /**
   * Send message
   *
   * @param sender commandsender
   * @param message message
   * @param prefix prefix of message
   */
  public static void sendMessage(CommandSender sender, String message, String prefix) {
    if (sender == null || message == null) {
      return;
    }

    sender.sendMessage(colorize(prefix + message));
  }

  /**
   * Send message
   *
   * @param sender commandsender
   * @param message message
   * @param usePrefix to use normal prefix
   */
  public static void sendMessage(CommandSender sender, String message, Boolean usePrefix) {
    if (sender == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    sender.sendMessage(colorize(message));
  }

  /**
   * Colorize text
   *
   * @param input input to be colorized
   */
  public static String colorize(String input) {
    return ChatColor.translateAlternateColorCodes('&', input);
  }

  /**
   * Help Menu
   *
   * @param player player
   * @param command command
   * @param desc description
   */
  public static void helpMenu(Player player, String command, String desc) {
    sendMessage(player, "&f" + command, false);
    sendMessage(player, " &7&o" + desc, false);
  }

  /**
   * Help Menu
   *
   * @param sender commandsender
   * @param command command
   * @param desc description
   */
  public static void helpMenu(CommandSender sender, String command, String desc) {
    sendMessage(sender, "&f" + command, false);
    sendMessage(sender, " &7&o" + desc, false);
  }

  /**
   * Help Menu
   *
   * @param sender commandsender
   * @param command command
   * @param desc description
   * @param prefix prefix for message
   */
  public static void helpMenu(CommandSender sender, String command, String desc, String prefix) {
    sendMessage(sender, "&f" + command, prefix);
    sendMessage(sender, " &7&o" + desc, prefix);
  }

  /**
   * Help Menu
   *
   * @param player player
   * @param command command
   * @param desc description
   * @param prefix prefix for message
   */
  public static void helpMenu(Player player, String command, String desc, String prefix) {
    sendMessage(player, "&f" + command, prefix);
    sendMessage(player, " &7&o" + desc, prefix);
  }

  /**
   * Help Menu
   *
   * @param text friendlyify text
   */
  public static String friendlyify(String text) {
    String n = text;

    n = n.replaceAll("_", "");
    n = n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();

    return n;
  }
}
