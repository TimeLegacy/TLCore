package net.timelegacy.tlcore.handler;

import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionHandler {

  private static TLCore plugin = TLCore.getPlugin();

  private static HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

  public static void detachPermissions(Player p) {
    p.removeAttachment(perms.get(p.getUniqueId()));
  }

  public void addPermission(Player p, String permission) {
    PermissionAttachment pperms = perms.get(p.getUniqueId());
    pperms.setPermission(permission, true);
  }

  public static void removePermission(Player p, String permission) {
    if (p.hasPermission(permission)) {
      perms.get(p.getUniqueId()).unsetPermission(permission);
    }
  }

  public static void clearPermissions() {
    perms.clear();
  }

  public static void attachPermissions(Player p) {
    PermissionAttachment attachment = p.addAttachment(plugin);
    perms.put(p.getUniqueId(), attachment);
  }
}
