package net.timelegacy.tlcore.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class FlyingItemUtils {

  private ArmorStand armorstand;
  private Location location;
  private String text = null;
  private Boolean h = false;
  private ItemStack itemstack;
  private double height = -1.3;

  public FlyingItemUtils() {
  }

  public void remove() {
    this.location = null;
    this.armorstand.remove();
    this.armorstand.getPassenger().remove();
    this.armorstand = null;
    this.h = false;
    this.height = 0;
    this.text = null;
    this.itemstack = null;
  }

  public void teleport(Location location) {
    if (this.location != null) {
      armorstand.teleport(location);
      this.location = location;
    }
  }

  public void spawn() {
    if (!h) {
      this.location.setY(this.location.getY() + this.height);
      h = true;
    }

    armorstand = this.location.getWorld().spawn(this.location, ArmorStand.class);
    armorstand.setGravity(false);
    armorstand.setVisible(false);
    Item i = this.location.getWorld().dropItem(this.getLocation(), this.itemstack);
    i.setPickupDelay(2147483647);
    if (this.text != null) {
      i.setCustomName(this.text);
      i.setCustomNameVisible(true);
    }

    armorstand.setPassenger(i);
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public ItemStack getItemStack() {
    return this.itemstack;
  }

  public void setItemStack(ItemStack itemstack) {
    this.itemstack = itemstack;
  }

  public double getHeight() {
    return this.height;
  }

  public void setHeight(double height) {
    this.height = height - 1.3;
    if (this.location != null) {
      this.location.setY(this.location.getY() + this.height);
      h = true;
    }
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
