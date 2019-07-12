package net.timelegacy.tlcore.datatype;

import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.entity.Player;

public class Chat {

  public static HashMap<UUID, Chat> playerChatStorage = new HashMap<>();

  private String prefix;
  private String suffix;
  private String format;
  private String username;

  public Chat(UUID uuid) {
    this.prefix = "";
    this.suffix = "";

    PlayerProfile profile = new PlayerProfile(uuid);
    this.username =
        profile.getNickname().isEmpty() ? PlayerHandler.getUsername(uuid) : profile.getNickname();

    this.format = MessageUtils.colorize("&r" + RankHandler
        .chatColors(uuid)
        .replace("%line%", "\u2758 ")
        .replace("%arrows%", "\u00BB")
        + " &r");
  }

  public static void addPlayer(Player player) {
    Chat chat = new Chat(player.getUniqueId());
    playerChatStorage.put(player.getUniqueId(), chat);
  }

  public static void removePlayer(Player player) {
    if (playerChatStorage.containsKey(player.getUniqueId())) {
      playerChatStorage.remove(player.getUniqueId());
    }
  }

  public static Chat getPlayerChat(Player player) {
    return playerChatStorage.get(player.getUniqueId());
  }

  public String getFormat() {
    return this.prefix + this.format.replace("%username%", this.username + this.suffix);
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }
}
