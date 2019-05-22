package net.timelegacy.tlcore.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {

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

  public static ItemStack createItem(Material mat, int amount, Byte b, String name,
      String... lore) {
    ItemStack is = new ItemStack(mat, amount, b);
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

  public static ItemStack createItem(Material mat, int amount, Byte b) {
    return new ItemStack(mat, amount, b);
  }

  public static ItemStack createItem(Material mat, int amount, Short b) {
    return new ItemStack(mat, amount, b);
  }

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

  public static ItemStack playerSkull(UUID id) {
    ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
    SkullMeta meta = (SkullMeta) item.getItemMeta();
    meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
    item.setItemMeta(meta);

    return item;
  }
}
