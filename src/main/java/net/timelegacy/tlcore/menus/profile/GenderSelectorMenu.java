package net.timelegacy.tlcore.menus.profile;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import net.timelegacy.tlcore.datatype.PlayerProfile;
import net.timelegacy.tlcore.datatype.PlayerProfile.Gender;
import net.timelegacy.tlcore.handler.CoinHandler;
import net.timelegacy.tlcore.handler.PlayerHandler;
import net.timelegacy.tlcore.utils.ItemUtils;
import net.timelegacy.tlcore.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GenderSelectorMenu implements Listener {

  public static String guiName = MenuUtils.centerTitle("&8&lYour Gender");

  public static void openMenu(Player player) {
    Inventory inv = Bukkit.createInventory(null, 9 * 3, guiName);

    PlayerProfile playerProfile = new PlayerProfile(player.getUniqueId());

    inv.setItem(1, ItemUtils.createItem(Material.EXPERIENCE_BOTTLE, "&eYour Character"));
    inv.setItem(6, ItemUtils.createItem(Material.EXPERIENCE_BOTTLE, "&eYour Gender"));

    Timestamp tsJoin = new Timestamp(PlayerHandler.getDateJoined(player.getUniqueId()));
    Date dateJoin = new Date(tsJoin.getTime());

    inv.setItem(
        10,
        ItemUtils.createSkullItem(
            player.getName(),
            "&e" + player.getName(),
            Arrays.asList(
                "&bJoin Date&7: &a" + dateJoin,
                "&bCoins&7: &a" + CoinHandler.getBalance(player.getUniqueId()),
                "&bNickname&7: &a" + (playerProfile.getNickname().isEmpty() ? "N/A" : playerProfile.getNickname()),
                "&bGender&7: &a" + playerProfile.getGender().toString())));


    inv.setItem(14, ItemUtils.createItem(new ItemStack(Material.LIGHT_BLUE_DYE, 1), "&bMale"));
    inv.setItem(15, ItemUtils.createItem(new ItemStack(Material.PINK_DYE, 1), "&dFemale"));
    inv.setItem(16, ItemUtils.createItem(new ItemStack(Material.PURPLE_DYE, 1), "&7Other"));

    player.openInventory(inv);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player p = (Player) event.getWhoClicked();

    if (event.getCurrentItem() != null) {

      String title = ChatColor.stripColor(event.getInventory().getTitle()).replace(" ", "");

      if (title.startsWith("YourGender")) {
        event.setCancelled(true);

        ItemStack is = event.getCurrentItem();

        if (!is.hasItemMeta()) {
          return;
        }

        String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
        PlayerProfile playerProfile = new PlayerProfile(p.getUniqueId());

        if (name.equals("Male")) {
          playerProfile.setGender(Gender.MALE);
          return;
        }

        if (name.equals("Female")) {
          playerProfile.setGender(Gender.FEMALE);
          return;
        }

        if (name.equals("Other")) {
          playerProfile.setGender(Gender.OTHER);
          return;
        }

      }
    }
  }
}
