package net.timelegacy.tlcore.command;

import net.timelegacy.tlcore.menus.profile.ProfileMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ProfileCommand implements CommandExecutor {

  @EventHandler
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player player = (Player) sender;

    ProfileMenu.openMenu(player);
    return false;
  }
}
