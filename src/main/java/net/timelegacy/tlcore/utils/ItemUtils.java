package net.timelegacy.tlcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemUtils {

  /**
   * Create an item
   *
   * @param mat    material of item
   * @param amount amount of item
   * @param name   name of item
   * @param lore   lore...
   * @return
   */
  public static ItemStack createItem(Material mat, int amount, String name, String... lore) {
    ItemStack is = new ItemStack(mat, amount);
    ItemMeta im = is.getItemMeta();

    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

    List<String> newLore = new ArrayList<String>();

    for (String l : lore) {
      newLore.add(ChatColor.translateAlternateColorCodes('&', l));
    }

    im.setLore(newLore);

    is.setItemMeta(im);
    return is;
  }

  /**
   * Create a colored item
   * @param mat material
   * @param amount amount of item
   * @param r red
   * @param g green
   * @param b blue
   * @param name name of item
   * @param lore lore...
   * @return
   */
  public static ItemStack createItem(
          Material mat, int amount, int r, int g, int b, String name, String... lore) {
    ItemStack is = new ItemStack(mat, amount);
    LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();

    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

    List<String> newLore = new ArrayList<String>();

    for (String l : lore) {
      newLore.add(ChatColor.translateAlternateColorCodes('&', l));
    }

    im.setLore(newLore);
    im.setColor(Color.fromRGB(r, g, b));

    is.setItemMeta(im);
    return is;
  }

  /**
   * Create an item from itemstack
   * @param i itemstack
   * @param amount amount of itemstack
   * @param name name of itemstack
   * @param lore lore...
   * @return
   */
  public static ItemStack createItem(ItemStack i, int amount, String name, String... lore) {
    ItemStack is = i;
    is.setAmount(amount);
    ItemMeta im = is.getItemMeta();

    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

    List<String> newLore = new ArrayList<>();

    for (String l : lore) {
      newLore.add(ChatColor.translateAlternateColorCodes('&', l));
    }

    im.setLore(newLore);

    is.setItemMeta(im);
    return is;
  }

  /**
   * Player skull from uuid
   *
   * @param uuid uuid of player
   * @return
   */
  public static ItemStack playerSkull(UUID uuid) {
    ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
    SkullMeta meta = (SkullMeta) item.getItemMeta();
    meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
    item.setItemMeta(meta);

    return item;
  }
}
