package net.timelegacy.tlcore.utils.Chat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class ChatUtils {

  public static String colorChat(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static String replaceColors(String message) {
    return ChatColor.stripColor(message).replaceAll("(?i)&([a-f0-9])", "");
  }

  public static List<String> colorList(List<String> lore) {
    List<String> newList = new ArrayList<>();

    for (String s : lore) {
      newList.add(colorChat(s));
    }

    return newList;
  }

}
