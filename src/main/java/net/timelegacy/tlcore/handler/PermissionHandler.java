package net.timelegacy.tlcore.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionHandler {

  private static TLCore plugin = TLCore.getPlugin();

  private static Map<UUID, PermissionAttachment> perms = new HashMap<>();

  /**
   * Detach permissions from a player
   *
   * @param player player
   */
  public static void detachPermissions(Player player) {
    player.removeAttachment(perms.get(player.getUniqueId()));
  }

  /**
   * Remove permission from a player
   *
   * @param player player
   * @param permission normal bukkit permission for plugins
   */
  public static void removePermission(Player player, String permission) {
    if (player.hasPermission(permission)) {
      perms.get(player.getUniqueId()).unsetPermission(permission);
    }
  }

  /**
   * Clear all permissions
   */
  public static void clearPermissions() {
    perms.clear();
  }

  /**
   * Attach the permissions to a player
   *
   * @param player player
   */
  public static void attachPermissions(Player player) {
    PermissionAttachment attachment = player.addAttachment(plugin);
    perms.put(player.getUniqueId(), attachment);
  }

  /**
   * Add a permission from a player
   *
   * @param player player
   * @param permission normal bukkit permission for plugins
   */
  public static void addPermission(Player player, String permission) {
    PermissionAttachment pperms = perms.get(player.getUniqueId());
    pperms.setPermission(permission, true);
  }
}
