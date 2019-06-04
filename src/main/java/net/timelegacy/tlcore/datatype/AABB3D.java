package net.timelegacy.tlcore.datatype;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Special thanks to this link for figuring this out! http://www.java-gaming.org/index.php?topic=28059.0
 */
public class AABB3D {

  protected Vector pos, size;

  /**
   * @param pos This is the center of the bounding box
   * @param size This is the full size of the box
   */
  public AABB3D(Vector pos, Vector size) {
    this.pos = pos;
    this.size = size.multiply(0.5);
  }

  public static boolean collides(AABB3D a, AABB3D b) {
    return (Math.abs(a.pos.getX() - b.pos.getX()) < a.size.getX() + b.size.getX()
        && Math.abs(a.pos.getY() - b.pos.getY()) < a.size.getY() + b.size.getY()
        && Math.abs(a.pos.getZ() - b.pos.getZ()) < a.size.getZ() + b.size.getZ());
  }

  public static boolean collidesXAxis(AABB3D a, AABB3D b) {
    return Math.abs(a.pos.getX() - b.pos.getX()) < a.size.getX() + b.size.getX();
  }

  public static boolean collidesYAxis(AABB3D a, AABB3D b) {
    return Math.abs(a.pos.getY() - b.pos.getY()) < a.size.getY() + b.size.getY();
  }

  public static boolean collidesZAxis(AABB3D a, AABB3D b) {
    return Math.abs(a.pos.getZ() - b.pos.getZ()) < a.size.getZ() + b.size.getZ();
  }

  public static boolean collidesWithBlocks(AABB3D a, Vector[] arrayOfBlocks) {
    for (Vector block : arrayOfBlocks) {
      AABB3D b = new AABB3D(block, new Vector(1, 1, 1));
      if (collides(a, b)) {
        return true;
      }
    }

    return false;
  }

  public static boolean inside(AABB3D a, Vector b) {
    return (Math.abs(a.pos.getX() - b.getX()) < a.size.getX()
        && Math.abs(a.pos.getY() - b.getY()) < a.size.getY()
        && Math.abs(a.pos.getZ() - b.getZ()) < a.size.getZ());
  }

  public static boolean inside(AABB3D a, AABB3D b) {
    return (Math.abs(a.pos.getX() - b.pos.getX()) < Math.abs(a.size.getX() - b.size.getX())
        && Math.abs(a.pos.getY() - b.pos.getY()) < Math.abs(a.size.getY() - b.size.getY())
        && Math.abs(a.pos.getZ() - b.pos.getZ()) < Math.abs(a.size.getZ() - b.size.getZ()));
  }

  public static boolean insideXAxis(AABB3D a, AABB3D b) {
    return (Math.abs(a.pos.getX() - b.pos.getX()) < Math.abs(a.size.getX() - b.size.getX()));
  }

  public static boolean insideYAxis(AABB3D a, AABB3D b) {
    return (Math.abs(a.pos.getY() - b.pos.getY()) < Math.abs(a.size.getY() - b.size.getY()));
  }

  public static boolean insideZAxis(AABB3D a, AABB3D b) {
    return (Math.abs(a.pos.getZ() - b.pos.getZ()) < Math.abs(a.size.getZ() - b.size.getZ()));
  }

  public static AABB3D getPlayersAABB(Player player) {
    if (player.isSwimming() || player.isGliding()) {
      return new AABB3D(
          new Vector(
              player.getLocation().getX(),
              player.getLocation().getY() + 0.3,
              player.getLocation().getZ()),
          new Vector(0.6, 0.6, 0.6));
    } else if (player.isSneaking()) {
      return new AABB3D(
          new Vector(
              player.getLocation().getX(),
              player.getLocation().getY() + 0.825,
              player.getLocation().getZ()),
          new Vector(0.6, 1.65, 0.6));
    } else {
      return new AABB3D(
          new Vector(
              player.getLocation().getX(),
              player.getLocation().getY() + 0.9,
              player.getLocation().getZ()),
          new Vector(0.6, 1.8, 0.6));
    }
  }

  public static boolean playerCollides(Player player, AABB3D object) {
    return collides(getPlayersAABB(player), object);
  }

  public static AABB3D expand(AABB3D start, AABB3D addon) {
    AABB3D result = new AABB3D(new Vector(), new Vector());
    if (!insideXAxis(start, addon)) {
      double[] x = getAddon(start.pos.getX(), start.size.getX(), addon.pos.getX(), addon.size.getX());
      result.pos.setX(x[0]);
      result.size.setX(x[1]);
    } else {
      result.pos.setX(start.pos.getX());
      result.size.setX(start.size.getX());
    }

    if (!insideYAxis(start, addon)) {
      double[] y = getAddon(start.pos.getY(), start.size.getY(), addon.pos.getY(), addon.size.getY());
      result.pos.setY(y[0]);
      result.size.setY(y[1]);
    } else {
      result.pos.setY(start.pos.getY());
      result.size.setY(start.size.getY());
    }

    if (!insideZAxis(start, addon)) {
      double[] z = getAddon(start.pos.getZ(), start.size.getZ(), addon.pos.getZ(), addon.size.getZ());
      result.pos.setZ(z[0]);
      result.size.setZ(z[1]);
    } else {
      result.pos.setZ(start.pos.getZ());
      result.size.setZ(start.size.getZ());
    }

    return result;
  }

  private static double[] getAddon(double posA, double sizeA, double posB, double sizeB) {
    double[][] possiblePositions = new double[][]{{
        posB + Math.abs(((posA + sizeA) - (posB - sizeB)) / 2),
        Math.abs((posA + sizeA) - (posB + sizeB))


    }, {
        posA + Math.abs(((posA - sizeA) - (posB + sizeB)) / 2),
        Math.abs((posA + sizeA) - (posB - sizeB))
    }};
    double[] result = possiblePositions[0];
    if (possiblePositions[1][1] > result[1]) {
      return possiblePositions[1];
    }

    return result;
  }

  public String toString() {
    return "AxisAlignedBoundingBox{pos=" + pos + ",size:" + size + "}";
  }
}
