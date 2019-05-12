package net.timelegacy.core.handler;

import java.util.HashMap;
import java.util.UUID;
import net.timelegacy.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionHandler {

  private Core core = Core.getInstance();

  private HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();

  public void detachPermissions(Player p) {
    p.removeAttachment(perms.get(p.getUniqueId()));
  }

  public void addPermission(Player p, String permission) {
    PermissionAttachment pperms = perms.get(p.getUniqueId());
    pperms.setPermission(permission, true);
  }

  public void removePermission(Player p, String permission) {
    if (p.hasPermission(permission)) {
      perms.get(p.getUniqueId()).unsetPermission(permission);
    }
  }

  public void clearPermissions() {
    perms.clear();
  }

  public void attachPermissions(Player p) {
    PermissionAttachment attachment = p.addAttachment(core);
    perms.put(p.getUniqueId(), attachment);
  }
}
