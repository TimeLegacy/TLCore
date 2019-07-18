package net.timelegacy.tlcore.datatype;

import java.util.HashMap;

public class Rank {

  private String name;
  private int priority;
  private String chat;
  private String color;
  private String tab;
  private HashMap<String, String> permissions;

  public Rank(
      String name, int priority, String chat, String color,
      String tab, HashMap<String, String> permissions) {
    this.name = name;
    this.priority = priority;
    this.chat = chat;
    this.color = color;
    this.tab = tab;
    this.permissions = permissions;
  }

  public String getName() {
    return name;
  }

  public String getChat() {
    return chat;
  }

  public int getPriority() {
    return priority;
  }

  public String getTab() {
    return tab;
  }

  public String getColor() {
    return color;
  }

  public HashMap<String, String> getPermissions() {
    return permissions;
  }
}
