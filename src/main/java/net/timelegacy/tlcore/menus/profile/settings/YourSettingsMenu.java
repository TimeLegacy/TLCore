package net.timelegacy.tlcore.menus.profile.settings;

import java.util.Arrays;
import java.util.Collections;
import net.timelegacy.tlcore.datatype.PlayerProfile;
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

public class YourSettingsMenu implements Listener {

  public static String guiName = MenuUtils.centerTitle("&8&lYour Settings");

  public static void openMenu(Player player) {
    Inventory inv = Bukkit.createInventory(null, 9 * 4, guiName);

    PlayerProfile playerProfile = new PlayerProfile(player.getUniqueId());

    // Row 2
    inv.setItem(10, ItemUtils.createItem(Material.ENDER_EYE, "&aPlayer Visibility"));
    inv.setItem(11, ItemUtils.createItem(Material.BONE, "&aPet Visibility",
        Collections.singletonList("&b&lComing Soon")));
    inv.setItem(12, ItemUtils.createItem(Material.SPAWNER, "&aAuto Spawn Pet",
        Collections.singletonList("&b&lComing Soon")));

    ItemStack active = ItemUtils.createItem(Material.GREEN_BANNER, MessageUtils.colorize("&aState Settings"));
    ItemStack away = ItemUtils.createItem(Material.YELLOW_BANNER, MessageUtils.colorize("&aState Settings"));
    ItemStack doNotDisturb = ItemUtils.createItem(Material.RED_BANNER, MessageUtils.colorize("&aState Settings"));

    switch (playerProfile.getStatus().toString()) {
      case "ACTIVE":
        inv.setItem(14, active);
        break;
      case "AWAY":
        inv.setItem(14, away);
        break;
      case "DND":
        inv.setItem(14, doNotDisturb);
        break;
    }

    // inv.setItem(14, ItemUtils.createItem(Material.BANNER, "&aState Settings"));
    inv.setItem(15, ItemUtils.createItem(Material.PAPER, "&aChat Settings"));
    inv.setItem(16, ItemUtils.createItem(Material.BARRIER, "&aPrivacy Settings"));

    // Row 3
    inv.setItem(19, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 10), "&aPlayer Visibility",
        Arrays.asList("&7Right-click the Blaze Rod", "&7In your hotbar!")));
    inv.setItem(20, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 8), "&aPet Visibility",
        Collections.singletonList("&b&lComing Soon")));
    inv.setItem(21, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 8), "&aAuto Spawn pet",
        Collections.singletonList("&b&lComing Soon")));

    inv.setItem(23, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 14), "&aState Settings",
        Collections.singletonList("&eClick to edit settings!")));
    inv.setItem(24, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 14), "&aChat Settings",
        Collections.singletonList("&b&lComing Soon")));
    inv.setItem(25, ItemUtils.createItem(new ItemStack(Material.INK_SAC, 1, (byte) 14), "&aPrivacy Settings",
        Collections.singletonList("&eClick to edit settings!")));

    player.openInventory(inv);

    //    ItemStack playerVisibility = new ItemStack(Material.EYE_OF_ENDER, 1);
    //    ItemMeta playerVisibilityMeta = playerVisibility.getItemMeta();
    //
    ////    if (Database.getVisibility(player)) {
    ////      playerVisibilityMeta.setDisplayName(MessageUtils.colorize("&aPlayer Visibility"));
    ////      playerVisibility.setItemMeta(playerVisibilityMeta);
    ////    } else {
    ////      playerVisibilityMeta.setDisplayName(MessageUtils.colorize("&cPlayer Visibility"));
    ////      playerVisibility.setItemMeta(playerVisibilityMeta);
    ////    }
    //
    //    inv.setItem(10, playerVisibility);
    //
    //    ItemStack petVisibility = new ItemStack(Material.BONE, 1);
    //    inv.setItem(11, petVisibility);
    //
    //    ItemStack autoSpawnPet = new ItemStack(Material.MOB_SPAWNER, 1);
    //    inv.setItem(12, autoSpawnPet);
    //
    //
    //    ItemStack currentState = new ItemStack(Material.BANNER, 1);
    //    inv.setItem(14, currentState);
    //
    //    ItemStack chatSettings = new ItemStack(Material.PAPER, 1);
    //    inv.setItem(15, chatSettings);
    //
    //    ItemStack privacySettings = new ItemStack(Material.PAPER, 1);
    //    inv.setItem(16, privacySettings);
    //
    //
    //    ItemStack playerVisibilityEnabled = new ItemStack(Material.INK_SACK, 1, (byte) 10);
    //    inv.setItem(19, playerVisibilityEnabled);
    //
    //    ItemStack petVisibilityEnabled = new ItemStack(Material.INK_SACK, 1, (byte) 8);
    //    inv.setItem(20, petVisibilityEnabled);
    //
    //    ItemStack autoSpawnPetEnabled = new ItemStack(Material.INK_SACK, 1, (byte) 8);
    //    inv.setItem(21, autoSpawnPetEnabled);
    //
    //
    //    ItemStack currentStateSettings = new ItemStack(Material.INK_SACK, 1, (byte) 14);
    //    inv.setItem(23, currentStateSettings);
    //
    //    ItemStack editChatSettings = new ItemStack(Material.INK_SACK, 1, (byte) 14);
    //    inv.setItem(24, editChatSettings);
    //
    //    ItemStack editPrivacySettings = new ItemStack(Material.INK_SACK, 1, (byte) 14);
    //    inv.setItem(25, editPrivacySettings);

  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player p = (Player) event.getWhoClicked();

    if (event.getCurrentItem() != null) {

      String title = ChatColor.stripColor(event.getInventory().getTitle()).replace(" ", "");

      if (title.startsWith("YourSettings")) {
        event.setCancelled(true);

        ItemStack is = event.getCurrentItem();

        if (!is.hasItemMeta()) {
          return;
        }

        String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());

        if (name.equals("State Settings")) {
          //YourStateSettingsMenu.
          return;
        }
      }
    }
  }
}
