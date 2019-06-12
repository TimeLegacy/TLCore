package net.timelegacy.tlcore.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.timelegacy.tlcore.TLCore;
import net.timelegacy.tlcore.handler.FriendHandler;
import net.timelegacy.tlcore.handler.PerkHandler;
import net.timelegacy.tlcore.handler.RankHandler;
import net.timelegacy.tlcore.utils.ItemUtils;
import net.timelegacy.tlcore.utils.MenuUtils;
import net.timelegacy.tlcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FriendsMenu {

  private static TLCore plugin = TLCore.getPlugin();

  public static void openMenu(Player player, int page) {
    Inventory menu = Bukkit.createInventory(player, 54, MessageUtils.colorize("&8&lFriends >> &8&nPage " + page));

    // Row 5
    menu.setItem(39, ItemUtils.createItem(Material.ARROW, 1, "&aPrevious Page"));
    menu.setItem(40, ItemUtils.createItem(Material.RED_STAINED_GLASS, 1, "&cReset Pet"));
    menu.setItem(41, ItemUtils.createItem(Material.ARROW, 1, "&aNext Page"));

    // Row 6
    //menu.setItem(49, ItemUtils.createItem(Material.ENCHANTING_TABLE, 1, "&eReturn to Cosmetics"));

    player.openInventory(menu);

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

          List<UUID> friends = FriendHandler.getFriends(player.getUniqueId());

          int current = ((i - 10) + start) - forgotten;

          if (current >= friends.size()) {
            continue;
          }

          ItemStack itemStack = ItemUtils.createSkullItem(friends.get(current).toString());
          ItemStack is = itemStack.clone();

          if (PerkHandler.hasPerk(player.getUniqueId(), pets.get(current).getPerkPerm())
              || RankHandler.getRank(player.getUniqueId()).getPriority() >= 9) {
            ItemMeta ism = is.getItemMeta();
            List<String> lore = ism.getLore() == null ? new ArrayList<>() : ism.getLore();
            if (CosmeticHandler.getPet(player)
                .equalsIgnoreCase(pets.get(current).getCosmeticIdentifier())) {
              ism.addEnchant(Enchantment.DURABILITY, 1, true);
              lore.add(MessageUtils.colorize("&a&lENABLED!"));
            } else {
              lore.add(MessageUtils.colorize("&a&lUNLOCKED"));
            }
            ism.setLore(lore);
            is.setItemMeta(ism);
          } else {
            is =
                ItemUtils.createItem(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    "&c&lLOCKED",
                    "&fUnlock by opening crates.");
          }

          menu.setItem(i, is);
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

      if (title.startsWith("Pets>>Page")) {
        event.setCancelled(true);

        int i = 0;

        if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&eReturn to Cosmetics"))) {
          //CosmeticMenu.openMenu(p);
          return;
        }

        int pageNumber = Integer.parseInt(title.split("Page")[1]);

        if (event
            .getCurrentItem()
            .getItemMeta()
            .getDisplayName()
            .equals(MessageUtils.colorize("&cReset Pet"))) {
          /*if (CosmeticHandler.hasPet(p)) {
            CosmeticHandler.removePet(p);
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "You have removed your pet cosmetic.", true);
          } else {
            MessageUtils.sendMessage(
                p, MessageUtils.ERROR_COLOR + "You do not have a pet enabled.", true);
          }*/
          return;
        }

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
          double pages = (double) /*CosmeticHandler.getTotals(p).get("pets") / */21;

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
        } else {

          /*for (Cosmetic cosmetic : CosmeticHandler.getCosmetics()) {
            if (cosmetic.getCosmeticType().equalsIgnoreCase("PET")) {
              if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())
                  .equalsIgnoreCase(
                      ChatColor.stripColor(
                          cosmetic.getItemStack().getItemMeta().getDisplayName()))) {
                CosmeticHandler.setPet(p, cosmetic.getCosmeticIdentifier());

                p.closeInventory();

                MessageUtils.sendMessage(
                    p,
                    MessageUtils.MAIN_COLOR
                        + "You have set your pet as "
                        + MessageUtils.SECOND_COLOR
                        + MessageUtils
                        .friendlyify(cosmetic.getCosmeticIdentifier().replace("_", " ")),
                    true);

                break;
              }
            }
          }*/
        }
      }
    }
  }

}
