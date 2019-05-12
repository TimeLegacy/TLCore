package net.timelegacy.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

  private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int CENTER_PX = 154;
  public String messagePrefix = "&b&lMineAqua&8:&r ";
  public String MAIN_COLOR = "&f";
  public String SECOND_COLOR = "&b";
  public String SUCCESS_COLOR = "&a";
  public String noPermission =
      messagePrefix + "&cYou do not have permission to perform this action!";
  public String ERROR_COLOR = "&c";

  /**
   * No Permission
   */
  public void noPerm(Player p) {
    sendMessage(p, noPermission, false);
  }

  /**
   * Send Message
   */
  public void sendMessage(Player p, String message, Boolean usePrefix) {
    if (p == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Send Message
   */
  public void sendMessage(Player p, String message, String prefix) {
    if (p == null || message == null) {
      return;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }

  /**
   * Send Message
   */
  public void sendMessage(CommandSender p, String message, String prefix) {
    if (p == null || message == null) {
      return;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }

  /**
   * Send Message
   */
  public void sendMessage(CommandSender p, String message, Boolean usePrefix) {
    if (p == null || message == null) {
      return;
    }

    if (usePrefix) {
      message = messagePrefix + message;
    }

    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Color
   */
  public String c(String input) {
    return ChatColor.translateAlternateColorCodes('&', input);
  }

  /**
   * MD5 Encrypt
   */
  public String MD5(String md5) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(md5.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100), 1, 3);
      }

      return sb.toString();

    } catch (java.security.NoSuchAlgorithmException e) {
      return md5;
    }
  }

  /**
   * Help Message
   */
  public void helpMenu(Player p, String command, String desc) {
    sendMessage(p, "&f" + command, false);
    sendMessage(p, " &7&o" + desc, false);
  }

  /**
   * Help Message
   */
  public void helpMenu(CommandSender p, String command, String desc) {
    sendMessage(p, "&f" + command, false);
    sendMessage(p, " &7&o" + desc, false);
  }

  /**
   * Help Message
   */
  public void helpMenu(CommandSender p, String command, String desc, String prefix) {
    sendMessage(p, "&f" + command, prefix);
    sendMessage(p, " &7&o" + desc, prefix);
  }

  /**
   * Help Message
   */
  public void helpMenu(Player p, String command, String desc, String prefix) {
    sendMessage(p, "&f" + command, prefix);
    sendMessage(p, " &7&o" + desc, prefix);
  }

  /**
   * Friendlyify
   */
  public String friendlyify(String name) {
    String n = name;

    n = n.replaceAll("_", "");
    n = n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();

    return n;
  }

  /*
   * Center the message in chat
   *
   * @param player
   * @param message


  public void sendCenteredMessage(Player player, String message) {
      if (message == null || message.equals("")) player.sendMessage("");
      message = ChatColor.translateAlternateColorCodes('&', message);

      int messagePxSize = 0;
      boolean previousCode = false;
      boolean isBold = false;

      for (char c : message.toCharArray()) {
          if (c == '§') {
              previousCode = true;
              continue;
          } else if (previousCode == true) {
              previousCode = false;
              if (c == 'l' || c == 'L') {
                  isBold = true;
                  continue;
              } else isBold = false;
          } else {
              DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
              messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
              messagePxSize++;
          }
      }

      int halvedMessageSize = messagePxSize / 2;
      int toCompensate = CENTER_PX - halvedMessageSize;
      int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
      int compensated = 0;
      StringBuilder sb = new StringBuilder();
      while (compensated < toCompensate) {
          sb.append(" ");
          compensated += spaceLength;
      }
      player.sendMessage(sb.toString() + message);
  }*/

  public String randomAlphaNumeric(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }
}
