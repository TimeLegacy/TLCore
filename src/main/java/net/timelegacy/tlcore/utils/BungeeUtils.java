package net.timelegacy.tlcore.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.entity.Player;

public class BungeeUtils {

  private static TLCore plugin = TLCore.getPlugin();

  /**
   * Kick a player from the server from bungeecord
   *
   * @param player player
   * @param message message to send to the player
   */
  public static void kickPlayer(Player player, String message) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("KickPlayer");
    out.writeUTF(player.getName());
    out.writeUTF(message);

    player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
  }

  public static void sendPlayer(Player player, String serverName) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(serverName);

    player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
  }

}
