package net.timelegacy.core.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.timelegacy.core.Core;
import org.bukkit.entity.Player;

/**
 * Created by Batman on 2016-06-26.
 */
public class BungeeUtils {

  private Core core = Core.getInstance();

  /**
   * Kick a player from the bungee network
   *
   * @param player Player who is to be kicked from bungee
   * @param message Kick message
   */
  public void kickPlayer(Player player, String message) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("KickPlayer");
    out.writeUTF(player.getName());
    out.writeUTF(message);

    player.sendPluginMessage(core, "BungeeCord", out.toByteArray());
  }
}
