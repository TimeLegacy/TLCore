package net.timelegacy.core.command;

import net.timelegacy.core.Core;
import net.timelegacy.core.handler.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class FlyCommand implements CommandExecutor {

  private Core core = Core.getInstance();

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (sender instanceof Player) {
      final Player p = (Player) sender;
      Rank r = core.rankHandler.getRank(p.getName());
      if (r.getPriority() >= 9) {

        if (core.flySpeed.contains(p.getName())) {
          core.flySpeed.remove(p.getName());
          p.setFlying(false);
          p.setFlySpeed(0.1f);
          p.setAllowFlight(false);
          core.messageUtils.sendMessage(
              p, core.messageUtils.ERROR_COLOR + "Flymode disabled.", true);
        } else {
          if (args.length == 0) {
            core.messageUtils.sendMessage(
                p, core.messageUtils.SUCCESS_COLOR + "Flymode enabled at default speed.", true);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setAllowFlight(true);
            core.flySpeed.add(p.getName());
          } else {
            String posnum = args[0];

            int speed = 1;

            try {
              speed = Integer.parseInt(posnum);

              if (speed < 1 || speed > 10) {
                core.messageUtils.sendMessage(
                    p, core.messageUtils.ERROR_COLOR + "Usage: /fly [speed (1-10)]", true);
              } else {
                if (speed == 1) {
                  p.setFlySpeed(0.1f);
                }
                if (speed == 2) {
                  p.setFlySpeed(0.2f);
                }
                if (speed == 3) {
                  p.setFlySpeed(0.3f);
                }
                if (speed == 4) {
                  p.setFlySpeed(0.4f);
                }
                if (speed == 5) {
                  p.setFlySpeed(0.5f);
                }
                if (speed == 6) {
                  p.setFlySpeed(0.6f);
                }
                if (speed == 7) {
                  p.setFlySpeed(0.7f);
                }
                if (speed == 8) {
                  p.setFlySpeed(0.8f);
                }
                if (speed == 9) {
                  p.setFlySpeed(0.9f);
                }
                if (speed == 10) {
                  p.setFlySpeed(1.0f);
                }

                core.messageUtils.sendMessage(
                    p,
                    core.messageUtils.MAIN_COLOR
                        + "Flymode enabled at speed "
                        + core.messageUtils.SECOND_COLOR
                        + speed
                        + core.messageUtils.MAIN_COLOR
                        + ".",
                    true);
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setAllowFlight(true);
                core.flySpeed.add(p.getName());
              }
            } catch (NumberFormatException ex) {
              core.messageUtils.sendMessage(
                  p, core.messageUtils.ERROR_COLOR + "Usage: /fly [speed (1-10)]", true);
              return true;
            }
          }
        }
      } else {
        core.messageUtils.noPerm(p);
      }
    }

    return false;
  }
}
