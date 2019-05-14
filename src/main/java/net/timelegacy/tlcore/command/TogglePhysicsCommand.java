package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TogglePhysicsCommand implements CommandExecutor {

  private TLCore core = TLCore.getInstance();

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;

      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {
        if (core.physics) {
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Physics has been disabled.", true);
        } else {
          core.messageUtils.sendMessage(
              p, core.messageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
        }

        core.physics = !core.physics;
      } else {
        core.messageUtils.noPerm(p);
      }

      return true;
    } else {
      if (core.physics) {
        core.messageUtils.sendMessage(
            sender, core.messageUtils.ERROR_COLOR + "Physics has been disabled.", true);
      } else {
        core.messageUtils.sendMessage(
            sender, core.messageUtils.SUCCESS_COLOR + "Physics has been enabled.", true);
      }

      core.physics = !core.physics;
      return false;
    }
  }
}
