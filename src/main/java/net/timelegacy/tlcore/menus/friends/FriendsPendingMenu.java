package net.timelegacy.tlcore.menus.friends;

import de.erethon.headlib.HeadLib;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.handler.FriendHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.utils.ItemUtils;
import net.timelegacy.tlcore.utils.MenuUtils;
import net.timelegacy.tlcore.utils.MessageUtils;
import net.wesjd.anvilgui.AnvilGUI;
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

public class FriendsPendingMenu implements Listener {

  private static TLCore plugin = TLCore.getPlugin();

  public static void openMenu(Player player, int page) {
    Inventory menu = Bukkit
        .createInventory(player, 54, MessageUtils.colorize("&8&lPending Friends >> &8&nPage " + page));

    // Row 5
    menu.setItem(39, ItemUtils.createItem(Material.ARROW, 1, "&aPrevious Page"));
    menu.setItem(40, ItemUtils.createItem(HeadLib.WOODEN_PLUS.toItemStack(), 1, "&bSend Request"));
    menu.setItem(41, ItemUtils.createItem(Material.ARROW, 1, "&aNext Page"));

    // Row 6
    menu.setItem(49, ItemUtils.createItem(Material.ENCHANTING_TABLE, 1, "&eReturn to Friends"));

    player.openInventory(menu);

    List<UUID> pendingFriends = FriendHandler.getPendingFriends(player.getUniqueId());

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

          if (current >= pendingFriends.size()) {
            continue;
          }

          String friendUsername = PlayerHandler.getUsername(pendingFriends.get(current));

          ItemStack itemStack = ItemUtils.createSkullItem(friendUsername, "&b" + friendUsername);
          menu.setItem(i, ItemUtils.createItem(itemStack, 1, "&b" + friendUsername,
              "&7Left click to &a&lACCEPT",
              "&7Right click to &c&lIGNORE",
              "",
              "&7this friend request."));
          player.updateInventory();
        }
      }
    }.runTaskAsynchronously(plugin);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player p = (Player) event.getWhoClicked();

    if (event.getCurrentItem() != null) {

      String title = ChatColor.stripColor(event.getInventory().getTitle()).replace(" ", "");

      if (title.startsWith("PendingFriends>>Page")) {
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
            .equals(MessageUtils.colorize("&bSend Request"))) {

          AnvilGUI anvilGUI =
              new AnvilGUI(
                  plugin,
                  p,
                  "Username",
                  (player, reply) -> {
                    if (PlayerHandler.playerExists(reply)) {
                      UUID request = PlayerHandler.getUUID(reply);
                      PlayerProfile profile = new PlayerProfile(request);

                      PlayerProfile pp = new PlayerProfile(p.getUniqueId());

                      if (FriendHandler.getFriends(p.getUniqueId()).size() < 32) {
                        if (pp.getFriends().contains(request.toString())) {
                          MessageUtils.sendMessage(
                              p,
                              MessageUtils.ERROR_COLOR + "You are already friends with &o" + reply,
                              "");
                        } else {
                          if (!profile.getFriendsPending().contains(p.getUniqueId().toString())) {
                            MessageUtils.sendMessage(
                                p,
                                MessageUtils.SUCCESS_COLOR
                                    + "You have sent a friend request to &o"
                                    + reply,
                                "");
                            FriendHandler.sendRequest(p.getUniqueId(), request);
                          } else {
                            MessageUtils.sendMessage(
                                p,
                                MessageUtils.ERROR_COLOR
                                    + "You already have a pending request to &o"
                                    + reply,
                                "");
                          }
                        }
                      } else {
                        MessageUtils.sendMessage(
                            p,
                            MessageUtils.ERROR_COLOR
                                + "You have reached the maximum amount of friends. (This will be changed in the next update. Yay!)",
                            "");
                      }
                      return null;
                    }
                    MessageUtils.sendMessage(p, MessageUtils.ERROR_COLOR + "Player not found.", "");
                    return "Player not found.";
                  });

          return;
        } else if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&eReturn to Friends"))) {
          p.closeInventory();
          FriendsMenu.openMenu(p, 1);

          return;
        } else {

          if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            String username = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            UUID receiver = PlayerHandler.getUUID(username);

            if (event.getClick().isRightClick()) {
              FriendHandler.denyRequest(p.getUniqueId(), receiver);
              MessageUtils.sendMessage(p,
                  MessageUtils.ERROR_COLOR + "You have ignored the friends request of &o" + username, "");
            } else if (event.getClick().isLeftClick()) {
              FriendHandler.acceptRequest(p.getUniqueId(), receiver);
              MessageUtils.sendMessage(p,
                  MessageUtils.SUCCESS_COLOR + "You are now friends with &o" + username, "");
            }

            p.closeInventory();
            FriendsMenu.openMenu(p, 1);
            return;
          }
        }
      }
    }
  }

}
