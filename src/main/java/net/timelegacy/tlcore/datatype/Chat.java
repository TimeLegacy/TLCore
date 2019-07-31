package net.timelegacy.tlcore.datatype;

import net.timelegacy.tlcore.utils.MessageUtils;

public class Chat {

  private String prefix;
  private String suffix;
  private String username;
  private PlayerData playerData;

  public Chat(PlayerData playerData) {
    this.prefix = "";
    this.suffix = "";

    this.playerData = playerData;
  }

  public String getFormat() {
    PlayerProfile profile = playerData.getPlayerProfile();
    String username =
        profile.getNickname().isEmpty() ? playerData.getUsername() : profile.getNickname();

    String format = MessageUtils.colorize("&r" + playerData.getRank().getChat()
        .replace("%line%", "\u2758 ")
        .replace("%arrows%", "\u00BB")
        + " &r");

    return this.prefix + format.replace("%username%", this.username + this.suffix);
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }
}
