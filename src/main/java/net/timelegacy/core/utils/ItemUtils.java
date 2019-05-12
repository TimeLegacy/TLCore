package net.timelegacy.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemUtils {

  /**
   * Create Item
   */
  public ItemStack createItem(Material mat, int amount, String name, String... lore) {
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
   * Create Item
   */
  public ItemStack createItem(Material mat, int amount, Byte b, String name, String... lore) {
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

  /**
   * Create Item
   */
  public ItemStack createItem(Material mat, int amount, Byte b) {
    return new ItemStack(mat, amount, b);
  }

  /**
   * Create Item
   */
  public ItemStack createItem(Material mat, int amount, Short b) {
    return new ItemStack(mat, amount, b);
  }

  /**
   * Create Item
   */
  public ItemStack createItem(
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
   * Create Item
   */
  public ItemStack createItem(ItemStack i, int amount, String name, String... lore) {
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

  /*public ItemStack setSkullOwner(ItemStack item, String id, String textureValue) {

    skullNMS.getDeclaredMethod("ItemStack")
    net.minecraft.server.v1_13_R2.ItemStack nmsStack = skullNMS.getDeclaredMethod("startTiming";

    NBTTagCompound tag = nmsStack.getOrCreateTag();

    NBTTagCompound skullOwner = new NBTTagCompound();
    skullOwner.setString("Id", id);
    NBTTagCompound properties = new NBTTagCompound();
    NBTTagList textures = new NBTTagList();
    NBTTagCompound value = new NBTTagCompound();
    value.setString("Value", textureValue);
    textures.add(value);
    properties.set("textures", textures);
    skullOwner.set("Properties", properties);

    tag.set("SkullOwner", skullOwner);

    return CraftItemStack.asBukkitCopy(nmsStack);
  }*/
}
