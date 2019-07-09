package net.timelegacy.tlcore.menus.friends;

import de.erethon.headlib.HeadLib;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.FriendHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
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
import org.bukkit.scheduler.BukkitRunnable;

public class FriendsMenu implements Listener {

  private static TLCore plugin = TLCore.getPlugin();

  public static void openMenu(Player player, int page) {

    Inventory menu = Bukkit.createInventory(player, 54, MessageUtils.colorize("&8&lFriends >> &8&nPage " + page));

    // Row 5
    menu.setItem(39, ItemUtils.createItem(Material.ARROW, 1, "&aPrevious Page"));
    menu.setItem(40, ItemUtils.createItem(HeadLib.WOODEN_EXCLAMATION_MARK.toItemStack(), 1, "&bPending Friends"));
    menu.setItem(41, ItemUtils.createItem(Material.ARROW, 1, "&aNext Page"));

    // Row 6
    //menu.setItem(49, ItemUtils.createItem(Material.ENCHANTING_TABLE, 1, "&eReturn to Cosmetics"));

    player.openInventory(menu);

    List<UUID> friends = FriendHandler.getFriends(player.getUniqueId());

    new BukkitRunnable() {

      @Override
      public void run() {
        int start = (page * 21) - 21;
        int forgotten = 0;

        for (int i = 10; i <= 34; i++) {
          if (i == 17 || i == 18 || i == 26 || i == 27) {
            forgotten++;
            continue;
          }

          int current = ((i - 10) + start) - forgotten;

          if (current >= friends.size()) {
            continue;
          }

          String friendUsername = PlayerHandler.getUsername(friends.get(current));

          ItemStack itemStack = ItemUtils.createSkullItem(friendUsername, "&b" + friendUsername);
          ItemStack is = itemStack.clone();
          menu.setItem(i, is);
          player.updateInventory();
        }
      }
    }.runTaskAsynchronously(plugin);
  }

  public static void displayRemovalConfirm(Inventory inv, int slot, String username, ItemStack originalHead) {
    ItemStack removalConfirm = ItemUtils.createItem(Material.GUNPOWDER, "&b" + username, Arrays.asList(
        "&7Click to &c&lREMOVE",
        "&b" + username + "&7 as a friend.",
        "",
        "&7This will disappear in 10 seconds."));

    inv.setItem(slot, removalConfirm);
    Bukkit.getScheduler().runTaskLater(plugin, () -> inv.setItem(slot, originalHead), 10 * 20);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player p = (Player) event.getWhoClicked();

    if (event.getCurrentItem() != null) {

      String title = ChatColor.stripColor(event.getInventory().getTitle()).replace(" ", "");

      if (title.startsWith("Friends>>Page")) {
        event.setCancelled(true);

        int i = 0;

        int pageNumber = Integer.parseInt(title.split("Page")[1]);

        if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&aPrevious Page"))) {
          if (pageNumber == 1) {
            MenuUtils.displayGUIError(
                event.getInventory(),
                event.getSlot(),
                event.getCurrentItem(),
                ItemUtils.createItem(Material.BARRIER, 1, "&cThis is the first page!"),
                3);
            return;
          } else {
            openMenu(p, pageNumber - 1);
            return;
          }
        }

        if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&aNext Page"))) {
          double pages = (double) FriendHandler.getFriends(p.getUniqueId()).size() / 21;

          if (pageNumber == MenuUtils.roundUp(pages)) {
            MenuUtils.displayGUIError(
                event.getInventory(),
                event.getSlot(),
                event.getCurrentItem(),
                ItemUtils.createItem(Material.BARRIER, 1, "&cThis is the last page!"),
                3);
            return;
          } else {
            openMenu(p, pageNumber + 1);
            return;
          }
        } else if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&bPending Friends"))) {
          p.closeInventory();
          FriendsPendingMenu.openMenu(p, 1);

          return;
        } else {

          if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            ItemStack head = event.getCurrentItem();
            String username = ChatColor.stripColor(head.getItemMeta().getDisplayName());

            displayRemovalConfirm(event.getClickedInventory(), event.getSlot(), username, head);
          } else if (event.getCurrentItem().getType() == Material.GUNPOWDER) {
            UUID uuid = PlayerHandler
                .getUUID(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

            FriendHandler.removeFriend(p.getUniqueId(), uuid);
          }

          openMenu(p, 1);
        }
      }
    }
  }

}
