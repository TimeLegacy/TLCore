package net.timelegacy.tlcore.utils;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.List;
import net.timelegacy.tlcore.TLCore;
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
  private static int CENTER_PX = 160;

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
   * Filter a message through Grawlox
   */
  public static String filterMessage(String message) {
    return TLCore.grawlox.filter(message);
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
   * Centers chat message.
   *
   * @param message The message to center.
   * */
  public static String centerChat(String message) {
    return getCenteredMessage(message);
  }

  /**
   * Removes all color and color codes from the message.
   *
   * @param message The message to strip.
   * */
  public static String replaceColors(String message) {
    return ChatColor.stripColor(message).replaceAll("(?i)&([a-f0-9])", "");
  }

  /**
   * Colors a list of strings.
   *
   * @param lore The list of strings to color.
   * */
  public static List<String> colorList(List<String> lore) {
    List<String> newList = new ArrayList<>();

    for (String s : lore) {
      newList.add(colorize(s));
    }

    return newList;
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

  public static void sendCenteredMessage(Player player, String message) {
    if (message == null || message.equals("")) {
      player.sendMessage("");
    }

    message = MessageUtils.colorize(message);

    int messagePxSize = 0;
    boolean previousCode = false;
    boolean isBold = false;

    for (char c : message.toCharArray()) {
      if (c == 167) {
        previousCode = true;
      } else if (previousCode) {
        previousCode = false;
        isBold = c == 'l' || c == 'L';
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
  }

  public static void sendCenteredMessage(CommandSender sender, String message) {
    if (message == null || message.equals("")) {
      sender.sendMessage("");
    }

    message = MessageUtils.colorize(message);

    int messagePxSize = 0;
    boolean previousCode = false;
    boolean isBold = false;

    for (char c : message.toCharArray()) {
      if (c == 167) {
        previousCode = true;
      } else if (previousCode) {
        previousCode = false;
        isBold = c == 'l' || c == 'L';
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

    sender.sendMessage(sb.toString() + message);
  }

  public static void sendCenteredMessage(CommandSender sender, TextComponent message) {
    if (message == null || message.equals("")) {
      sender.sendMessage("");
    }

    //message = ChatUtils.colorChat(message);

    int messagePxSize = 0;
    boolean previousCode = false;
    boolean isBold = false;

    for (char c : message.getText().toCharArray()) {
      if (c == 167) {
        previousCode = true;
      } else if (previousCode) {
        previousCode = false;
        isBold = c == 'l' || c == 'L';
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

    sender.sendMessage(sb.toString() + message);
  }

  private static String getCenteredMessage(String message) {
    if (message == null || message.equals("")) {
      return "";
    }

    message = MessageUtils.colorize(message);

    int messagePxSize = 0;
    boolean previousCode = false;
    boolean isBold = false;

    for (char c : message.toCharArray()) {
      if (c == 167) {
        previousCode = true;
      } else if (previousCode) {
        previousCode = false;
        isBold = c == 'l' || c == 'L';
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

    return sb.toString() + message;
  }

  public enum DefaultFontInfo {

    A('A', 5),
    a('a', 5),
    B('B', 5),
    b('b', 5),
    C('C', 5),
    c('c', 5),
    D('D', 5),
    d('d', 5),
    E('E', 5),
    e('e', 5),
    F('F', 5),
    f('f', 4),
    G('G', 5),
    g('g', 5),
    H('H', 5),
    h('h', 5),
    I('I', 3),
    i('i', 1),
    J('J', 5),
    j('j', 5),
    K('K', 5),
    k('k', 4),
    L('L', 5),
    l('l', 1),
    M('M', 5),
    m('m', 5),
    N('N', 5),
    n('n', 5),
    O('O', 5),
    o('o', 5),
    P('P', 5),
    p('p', 5),
    Q('Q', 5),
    q('q', 5),
    R('R', 5),
    r('r', 5),
    S('S', 5),
    s('s', 5),
    T('T', 5),
    t('t', 4),
    U('U', 5),
    u('u', 5),
    V('V', 5),
    v('v', 5),
    W('W', 5),
    w('w', 5),
    X('X', 5),
    x('x', 5),
    Y('Y', 5),
    y('y', 5),
    Z('Z', 5),
    z('z', 5),
    NUM_1('1', 5),
    NUM_2('2', 5),
    NUM_3('3', 5),
    NUM_4('4', 5),
    NUM_5('5', 5),
    NUM_6('6', 5),
    NUM_7('7', 5),
    NUM_8('8', 5),
    NUM_9('9', 5),
    NUM_0('0', 5),
    EXCLAMATION_POINT('!', 1),
    AT_SYMBOL('@', 6),
    NUM_SIGN('#', 5),
    DOLLAR_SIGN('$', 5),
    PERCENT('%', 5),
    UP_ARROW('^', 5),
    AMPERSAND('&', 5),
    ASTERISK('*', 5),
    LEFT_PARENTHESIS('(', 4),
    RIGHT_PARENTHESIS(')', 4),
    MINUS('-', 5),
    UNDERSCORE('_', 5),
    PLUS_SIGN('+', 5),
    EQUALS_SIGN('=', 5),
    LEFT_CURL_BRACE('{', 4),
    RIGHT_CURL_BRACE('}', 4),
    LEFT_BRACKET('[', 3),
    RIGHT_BRACKET(']', 3),
    COLON(':', 1),
    SEMI_COLON(';', 1),
    DOUBLE_QUOTE('"', 3),
    SINGLE_QUOTE('\'', 1),
    LEFT_ARROW('<', 4),
    RIGHT_ARROW('>', 4),
    QUESTION_MARK('?', 5),
    SLASH('/', 5),
    BACK_SLASH('\\', 5),
    LINE('|', 1),
    TILDE('~', 5),
    TICK('`', 2),
    PERIOD('.', 1),
    COMMA(',', 1),
    SPACE(' ', 3),
    DEFAULT('a', 4);

    private char character;
    private int length;

    DefaultFontInfo(char character, int length) {
      this.character = character;
      this.length = length;
    }

    public char getCharacter() {
      return this.character;
    }

    public int getLength() {
      return this.length;
    }

    public int getBoldLength() {
      if (this == DefaultFontInfo.SPACE) {
        return this.getLength();
      }
      return this.length + 1;
    }

    public static DefaultFontInfo getDefaultFontInfo(char c) {
      for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
        if (dFI.getCharacter() == c) {
          return dFI;
        }
      }
      return DefaultFontInfo.DEFAULT;
    }
  }

}
