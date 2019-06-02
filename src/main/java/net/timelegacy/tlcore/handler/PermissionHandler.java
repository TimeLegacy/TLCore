package net.timelegacy.tlcore.handler;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class PermissionHandler {

  private static TLCore plugin = TLCore.getPlugin();

  private static HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

  /**
   * Detach permissions from a player
   *
   * @param p player
   */
  public static void detachPermissions(Player p) {
    p.removeAttachment(perms.get(p.getUniqueId()));
  }

  /**
   * Remove permission from a player
   *
   * @param p player
   * @param permission normal bukkit permission for plugins
   */
  public static void removePermission(Player p, String permission) {
    if (p.hasPermission(permission)) {
      perms.get(p.getUniqueId()).unsetPermission(permission);
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
   * @param p player
   */
  public static void attachPermissions(Player p) {
    PermissionAttachment attachment = p.addAttachment(plugin);
    perms.put(p.getUniqueId(), attachment);
  }

  /**
   * Add a permission from a player
   *
   * @param p          player
   * @param permission normal bukkit permission for plugins
   */
  public void addPermission(Player p, String permission) {
    PermissionAttachment pperms = perms.get(p.getUniqueId());
    pperms.setPermission(permission, true);
  }
}
