package net.timelegacy.tlcore.utils;

import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkUtils {

  /**
   * Spawn a random colored firework
   *
   * @param location location to spawn
   */
  public static Firework spawnFirework(Location location) {
    Random color = new Random();

    Firework fw = location.getWorld().spawn(location, Firework.class);
    FireworkMeta fwMeta = fw.getFireworkMeta();

    Type fwType = Type.BALL_LARGE;

    int c1i = color.nextInt(17) + 1;
    int c2i = color.nextInt(17) + 1;

    Color c1 = getFWColor(c1i);
    Color c2 = getFWColor(c2i);

    FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).withTrail().build();

    fwMeta.addEffect(effect);
    fwMeta.setPower(1);
    fw.setFireworkMeta(fwMeta);

    return fw;
  }

  /**
   * Spawn a firework
   *
   * @param location location to spawn
   * @param power power of firework
   */
  public static Firework spawnFirework(Location location, int power) {
    Random color = new Random();

    Firework fw = location.getWorld().spawn(location, Firework.class);
    FireworkMeta fwMeta = fw.getFireworkMeta();

    Type fwType = Type.BALL_LARGE;

    int c1i = color.nextInt(17) + 1;
    int c2i = color.nextInt(17) + 1;

    Color c1 = getFWColor(c1i);
    Color c2 = getFWColor(c2i);

    FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).withTrail().build();

    fwMeta.addEffect(effect);
    fwMeta.setPower(power);
    fw.setFireworkMeta(fwMeta);

    return fw;
  }

  /**
   * Spawn a firework
   *
   * @param location location to spawn
   * @param color color of firework
   * @param power power of firework
   */
  public static Firework spawnFirework(Location location, Color color, int power) {
    Firework fw = location.getWorld().spawn(location, Firework.class);
    FireworkMeta fwMeta = fw.getFireworkMeta();

    Type fwType = Type.BALL_LARGE;

    FireworkEffect effect = FireworkEffect.builder().withFade(color).withColor(color).with(fwType).withTrail().build();

    fwMeta.addEffect(effect);
    fwMeta.setPower(power);
    fw.setFireworkMeta(fwMeta);

    return fw;
  }

  /**
   * Spawn a firework
   *
   * @param location Location to spawn
   * @param color Color of firework
   */
  public static Firework spawnFirework(Location location, Color color) {
    Firework fw = location.getWorld().spawn(location, Firework.class);
    FireworkMeta fwMeta = fw.getFireworkMeta();

    Type fwType = Type.BALL_LARGE;

    FireworkEffect effect = FireworkEffect.builder().withFade(color).withColor(color).with(fwType).withTrail().build();

    fwMeta.addEffect(effect);
    fwMeta.setPower(1);
    fw.setFireworkMeta(fwMeta);

    return fw;
  }

  /**
   * Get firework color
   */
  public static Color getFWColor(int c) {
    switch (c) {
      case 1:
        return Color.TEAL;
      default:
      case 2:
        return Color.WHITE;
      case 3:
        return Color.YELLOW;
      case 4:
        return Color.AQUA;
      case 5:
        return Color.BLACK;
      case 6:
        return Color.BLUE;
      case 7:
        return Color.FUCHSIA;
      case 8:
        return Color.GRAY;
      case 9:
        return Color.GREEN;
      case 10:
        return Color.LIME;
      case 11:
        return Color.MAROON;
      case 12:
        return Color.NAVY;
      case 13:
        return Color.OLIVE;
      case 14:
        return Color.ORANGE;
      case 15:
        return Color.PURPLE;
      case 16:
        return Color.RED;
      case 17:
        return Color.SILVER;
    }
  }
}
