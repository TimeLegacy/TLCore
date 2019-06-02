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
   * @param p player
   */
  public static void noPerm(Player p) {
    sendMessage(p, noPermission, false);
  }

  /**
   * Send message
   *
   * @param p player
   * @param message message
   * @param usePrefix to use prefix
   */
  public static void sendMessage(Player p, String message, Boolean usePrefix) {
    if (p == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Send message
   *
   * @param p player
   * @param message message
   * @param prefix custom prefix
   */
  public static void sendMessage(Player p, String message, String prefix) {
    if (p == null || message == null) {
      return;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }

  /**
   * Send message
   *
   * @param p commandsender
   * @param message message
   * @param prefix prefix of message
   */
  public static void sendMessage(CommandSender p, String message, String prefix) {
    if (p == null || message == null) {
      return;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }

  /**
   * Send message
   *
   * @param p commandsender
   * @param message message
   * @param usePrefix to use normal prefix
   */
  public static void sendMessage(CommandSender p, String message, Boolean usePrefix) {
    if (p == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Colorize text
   *
   * @param input input to be colorized
   * @return
   */
  public static String colorize(String input) {
    return ChatColor.translateAlternateColorCodes('&', input);
  }

  /**
   * Help Menu
   *
   * @param p player
   * @param command command
   * @param desc description
   */
  public static void helpMenu(Player p, String command, String desc) {
    sendMessage(p, "&f" + command, false);
    sendMessage(p, " &7&o" + desc, false);
  }

  /**
   * Help Menu
   *
   * @param p commandsender
   * @param command command
   * @param desc description
   */
  public static void helpMenu(CommandSender p, String command, String desc) {
    sendMessage(p, "&f" + command, false);
    sendMessage(p, " &7&o" + desc, false);
  }

  /**
   * Help Menu
   *
   * @param p commandsender
   * @param command command
   * @param desc description
   * @param prefix prefix for message
   */
  public static void helpMenu(CommandSender p, String command, String desc, String prefix) {
    sendMessage(p, "&f" + command, prefix);
    sendMessage(p, " &7&o" + desc, prefix);
  }

  /**
   * Help Menu
   *
   * @param p player
   * @param command command
   * @param desc description
   * @param prefix prefix for message
   */
  public static void helpMenu(Player p, String command, String desc, String prefix) {
    sendMessage(p, "&f" + command, prefix);
    sendMessage(p, " &7&o" + desc, prefix);
  }

  /**
   * Help Menu
   *
   * @param text friendlyify text
   * @return
   */
  public static String friendlyify(String text) {
    String n = text;

    n = n.replaceAll("_", "");
    n = n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();

    return n;
  }
}
