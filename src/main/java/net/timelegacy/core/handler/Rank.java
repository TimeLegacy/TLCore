package net.timelegacy.core.handler;

public class Rank {

  private String name;
  private int priority;
  private String chat;
  private String mainColor;
  private String coColor;
  private String tab;

  public Rank(
      String name, int priority, String chat, String mainColor, String coColor, String tab) {
    this.name = name;
    this.priority = priority;
    this.chat = chat;
    this.mainColor = mainColor;
    this.coColor = coColor;
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

  public String getCoColor() {
    return coColor;
  }

  public String getMainColor() {
    return mainColor;
  }

  public String getTab() {
    return tab;
  }
}
