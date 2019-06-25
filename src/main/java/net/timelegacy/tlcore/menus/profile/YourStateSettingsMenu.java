package net.timelegacy.tlcore.menus.profile;

import java.util.Arrays;
import java.util.Collections;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.datatype.PlayerProfile.Status;
import net.timelegacy.tlcore.utils.ItemUtils;
import net.timelegacy.tlcore.utils.MenuUtils;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YourStateSettingsMenu implements Listener {

  public static String guiName = MenuUtils.centerTitle("&8&lYour State Settings");

  public static void openMenu(Player player) {
    PlayerProfile playerProfile = new PlayerProfile(player.getUniqueId());

    Inventory inv = Bukkit.createInventory(null, 9 * 6, guiName);

    String checkmark = "\u2713";
    String x = "\u274C";

    // Row 1

    // Row 2
    inv.setItem(13, ItemUtils.createItem(Material.WHITE_BANNER, "&eCurrent State",
        Arrays.asList(
            "&7Below is your &b&lSTATE&7. States are different",
            "&7ways of showing your activity. They also effect",
            "&7how you interact with the server.",
            "",
            "&7By default, you are set to",
            "&7the &b&lACTIVE&7 state.")));

    // Row 3

    // Row 4
    ItemStack active =
        ItemUtils.createItem(
            Material.GREEN_BANNER,
            MessageUtils.colorize("&a&lACTIVE"),
            Arrays.asList(
                "&7You will:",
                "&a" + checkmark + " &7Receive Messages",
                "&a" + checkmark + " &7Receive Party Invites",
                "",
                "&7Players will be able to:",
                "&a" + checkmark + " &7Interact with you."));

    ItemStack away =
        ItemUtils.createItem(
            Material.YELLOW_BANNER,
            MessageUtils.colorize("&e&lAWAY"),
            Arrays.asList(
                "&7You will:",
                "&a" + checkmark + " &7Receive Messages",
                "&c" + x + " &7Not Receive Party Invites",
                "",
                "&7Players will be able to:",
                "&c" + x + " &7Not interact with you."));

    ItemStack doNotDisturb =
        ItemUtils.createItem(
            Material.RED_BANNER,
            MessageUtils.colorize("&c&lDO NOT DISTURB"),
            Arrays.asList(
                "&7You will:",
                "&c" + x + " &7Not Receive Messages",
                "&c" + x + " &7Not Receive Party Invites",
                "",
                "&7Players will be able to:",
                "&c" + x + " &7Not interact with you."));

    inv.setItem(29, active);
    inv.setItem(31, away);
    inv.setItem(33, doNotDisturb);

    // Row 5
    switch (playerProfile.getStatus().toString()) {
      case "ACTIVE":
        inv.setItem(38, ItemUtils.createItem(new ItemStack(Material.LIME_DYE, 1), "&a&lACTIVE",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(40, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1), "&e&lAWAY",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(42, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1), "&c&lDO NOT DISTURB",
            Collections.singletonList("&7Click to change state!")));
        break;
      case "AWAY":
        inv.setItem(38, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1), "&a&lACTIVE",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(40, ItemUtils.createItem(new ItemStack(Material.LIME_DYE, 1), "&e&lAWAY",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(42, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1), "&c&lDO NOT DISTURB",
            Collections.singletonList("&7Click to change state!")));
        break;
      case "DND":
        inv.setItem(38, ItemUtils.createItem(new ItemStack(Material.GRAY_DYE, 1), "&a&lACTIVE",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(40, ItemUtils.createItem(new ItemStack(Material.GRAY_DYE, 1), "&e&lAWAY",
            Collections.singletonList("&7Click to change state!")));
        inv.setItem(42, ItemUtils.createItem(new ItemStack(Material.LIME_DYE, 1), "&c&lDO NOT DISTURB",
            Collections.singletonList("&7Click to change state!")));
        break;
    }

    player.openInventory(inv);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player p = (Player) event.getWhoClicked();

    if (event.getCurrentItem() != null) {

      String title = ChatColor.stripColor(event.getInventory().getTitle()).replace(" ", "");

      if (title.startsWith("YourStateSettings")) {
        event.setCancelled(true);

        ItemStack is = event.getCurrentItem();

        if (!is.hasItemMeta()) {
          return;
        }

        String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
        PlayerProfile playerProfile = new PlayerProfile(p.getUniqueId());

        if (name.equals("ACTIVE")) {
          playerProfile.setStatus(Status.ACTIVE);
          return;
        }

        if (name.equals("AWAY")) {
          playerProfile.setStatus(Status.AWAY);
          return;
        }

        if (name.equals("DO NOT DISTURB")) {
          playerProfile.setStatus(Status.DND);
          return;
        }

      }
    }
  }
}
