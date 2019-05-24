package net.timelegacy.tlcore.datatype;

public class Rank {

  private String name;
  private int priority;
  private String chat;
  private String primaryColor;
  private String secondaryColor;
  private String tab;

  public Rank(
      String name, int priority, String chat, String primaryColor, String secondaryColor,
      String tab) {
    this.name = name;
    this.priority = priority;
    this.chat = chat;
    this.primaryColor = primaryColor;
    this.secondaryColor = secondaryColor;
    this.tab = tab;
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

  public String getPrimaryColor() {
    return primaryColor;
  }

  public String getSecondaryColor() {
    return secondaryColor;
  }

  public String getTab() {
    return tab;
  }
}
