package net.timelegacy.tlcore.datatype;

import java.util.ArrayList;
import java.util.List;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Portal {

  private static volatile ArrayList<Portal> activePortals = new ArrayList<>();
  private String teleportDestination;
  private String teleportDestinationType;
  private Location locationOfPortal;
  private Player portalOwner;
  private int ticks = 0;
  private AABB3D[] portalBoundingBoxes;

  private Portal(Player portalOwner, String teleportDestinationType, String teleportDestination, Location locationOfPortal) {
    this.teleportDestination = teleportDestination;
    this.teleportDestinationType = teleportDestinationType;
    this.portalOwner = portalOwner;
    this.locationOfPortal = locationOfPortal;
    portalBoundingBoxes =
        getPortalBoundingBoxes(
            locationOfPortal.getYaw(),
            locationOfPortal.getX(),
            locationOfPortal.getY(),
            locationOfPortal.getZ());
    startPortal(locationOfPortal);
  }

  public static boolean createPortal(Player portalOwner, String teleportDestinationType, String teleportDestination) {
    Location tempLocationOfPortal = getSuitablePortalLocation(portalOwner.getLocation());
    if (tempLocationOfPortal.equals(portalOwner.getLocation())) {
      return false;
    } else {
      activePortals.add(
          new Portal(portalOwner, teleportDestinationType, teleportDestination, tempLocationOfPortal));
      portalOwner.sendMessage(portalOwner.getLocation().toString());
      return true;
    }
  }

  /**
   * This creates a the portal ring effect
   *
   * @param particle Particle effect to use, DRIP_WATER and DRIP_LAVA are the best for this
   * @param location The location of the portal, needs Yaw to be rotated
   * @param count This is how many particles to use in the portal
   * @param offset The starting position on the ring, its good to use a tick counter or something like that
   * @see Portal
   */
  public static void createPortalEffect(
      Particle particle, Location location, int count, int offset, double sizeX, double sizeY, double sizeZ) {
    double degrees = 360 / Math.ceil(count / getViewablePortals(location));
    double direction = Math.toRadians(location.getYaw() + 90);
    double directionCos = Math.cos(direction);
    double directionSin = Math.sin(direction);
    for (double degree = offset; degree < 361 + (offset); degree = degree + degrees) {
      double radians = Math.toRadians(degree);
      double x = (Math.cos(radians) * sizeX / 2) * directionSin;
      double y = (Math.sin(radians) * sizeY / 2) + 0.1 + (sizeY / 2);
      double z = (Math.cos(radians) * sizeZ / 2) * -directionCos;
      location.add(x, y, z);
      location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0);
      location.subtract(x, y, z);
    }
  }

  /**
   * Tests if the portal is valid, and returns the best location for it
   *
   * @param location the current location of the player
   * @return Best location of the portal, returns input if no valid portals are found
   */
  public static Location getSuitablePortalLocation(Location location) {
    Location bestLocation = location;

    List<Vector> nonValidAirBlocks = new ArrayList<>();
    Vector boundBoxScore = new Vector(0, 10, 10);

    // Detecting air blocks in an area around the player for fast access while checking corners
    // touching blocks
    for (double x = -4; x < 4; x++) {
      for (double y = -3; y < 6; y++) {
        for (double z = -4; z < 4; z++) {
          location.add(x, y, z);
          if (location.getBlock().getType().isSolid() || location.getBlock().isLiquid()) {
            nonValidAirBlocks.add(
                new Vector(
                    Math.floor(location.getX()) + 0.5,
                    Math.floor(location.getY()) + 0.5,
                    Math.floor(location.getZ()) + 0.5));
            location.subtract(x, y, z);
          } else {
            location.subtract(x, y, z);
          }
        }
      }
    }

    Vector[] nonValidAirBlocksArray = new Vector[nonValidAirBlocks.size()];
    nonValidAirBlocksArray = nonValidAirBlocks.toArray(nonValidAirBlocksArray);
    /* Detect if corners hit a solid block
     * I used a score system to make sure start close to the port and make sure that there is not a wall in between the player and where the portal will spawn
     */
    for (double x = 0.5; x <= 3.5; x = x + 0.2) {
      boolean blockInWay = true;
      // 0.5 blocks off of the X axis is ok, closer is better though
      for (double z = -0.5; z <= 0.5; z = z + 0.2) {

        for (double y = (-1); y < 2; y++) {
          if (Math.abs(boundBoxScore.getZ()) > Math.abs(z)
              || Math.abs(boundBoxScore.getY()) > Math.floor(Math.abs(y))) { // Y axis score
            // Block collision detection
            AABB3D[] tempSides = getPortalBoundingBoxes(location.getYaw(),
                location.getX() + (x * (Math.cos(Math.toRadians(location.getYaw() + 90)))),
                Math.floor(location.getY() + y),
                location.getZ()
                    + ((x + z) * (Math.sin(Math.toRadians(location.getYaw() + 90)))));
            boolean successful = true;
            for (int box = 0; box <= 2; box++) {
              if (successful) {
                if (AABB3D.collidesWithBlocks(tempSides[box], nonValidAirBlocksArray)) {
                  successful = false;
                } else if (box == 2) {
                  blockInWay = false;
                  // Checks if block is under portal
                  Location locUnderPortal = location.clone();
                  locUnderPortal.setX(tempSides[1].pos.getX());
                  locUnderPortal.setY(tempSides[1].pos.getY() - 2.20001);
                  locUnderPortal.setZ(tempSides[1].pos.getZ());

                  if (locUnderPortal.getBlock().getType().isSolid()) {
                    if (x > 1) {
                      boundBoxScore = new Vector(x, y, z);
                      if (x > 1.5) {
                        bestLocation =
                            new Location(
                                location.getWorld(),
                                location.getX()
                                    + (x * (Math.cos(Math.toRadians(location.getYaw() + 90)))),
                                Math.floor(location.getY() + y),
                                location.getZ()
                                    + ((x + z)
                                    * (Math.sin(Math.toRadians(location.getYaw() + 90)))),
                                location.getYaw(),
                                location.getPitch());
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      if (blockInWay) {
        return bestLocation;
      }

      boundBoxScore.setY(Math.abs(boundBoxScore.getY()) + 0.2);
      boundBoxScore.setZ(Math.abs(boundBoxScore.getZ()) + 0.2);
    }

    return bestLocation;
  }

  private static AABB3D[] getPortalBoundingBoxes(float yaw, double x, double y, double z) {
    // portals will have 3 bounding boxes 0.4 blocks wide, and 2.4 blocks high, 0.65 blocks apart
    double direction = Math.toRadians(yaw + 90);
    double directionCos = Math.cos(direction);
    double directionSin = Math.sin(direction);
    AABB3D[] result =
        new AABB3D[]{
            new AABB3D(
                new Vector(x - (directionSin / 1.3), y + 1.2, z - (directionCos / 1.3)),
                new Vector(0.5d, 2.3d, 0.5d)),
            new AABB3D(new Vector(x, y + 1.20001, z), new Vector(0.5d, 2.4d, 0.5d)),
            new AABB3D(
                new Vector(x + (directionSin / 1.3), y + 1.2, z + (directionCos / 1.3)),
                new Vector(0.5d, 2.3d, 0.5d))
        };
    return result;
  }

  public static void destroyPortal(Portal portal) {
    activePortals.remove(portal);
  }

  /**
   * @param portal portal to check how many portals are active nearby
   * @return Amount of portals within a 32 block radius of the portal
   */
  public static int getViewablePortals(Portal portal) {
    return getViewablePortals(portal.locationOfPortal);
  }

  /**
   * @param location give location of where to check how many portals are active nearby
   * @return Amount of portals within a 32 block radius of the location
   */
  public static int getViewablePortals(Location location) {
    int total = 1;
    for (Portal i : activePortals) {
      if (location != i.locationOfPortal) {
        if (i.locationOfPortal.distance(location) < 32) {
          total++;
        }
      }
    }
    return total;
  }

  private void startPortal(Location location) {
    Particle particle = Particle.DRIP_WATER;
    Portal portal = this;
    new BukkitRunnable() {

      @Override
      public void run() {
        if (ticks == 200) {
          createPortalEffect(particle, location, 80, ticks * 11, 1.4, 2.25, 1.4);
        } else if (ticks == 240) {
          destroyPortal(portal);
          cancel();
        } else if (ticks < 200) {
          createPortalEffect(particle, location, 8, ticks * 11, 1.4, 2.25, 1.4);
        }
        if (checkInPortal()) {
          teleportPlayer();
          destroyPortal(portal);
          if (ticks < 200) {
            createPortalEffect(particle, location, 80, ticks * 11, 1.4, 2.25, 1.4);
          }
          cancel();
        }
        ticks++;
      }
    }.runTaskTimerAsynchronously(TLCore.getPlugin(), 0, 1);
  }

  private boolean checkInPortal() {
    for (int box = 0; box < 2; box++) {
      if (AABB3D.playerCollides(portalOwner, portalBoundingBoxes[box])) {
        return true;
      }
    }

    return false;
  }

  private void teleportPlayer() {
    switch (teleportDestinationType) {
      case "player":
        portalOwner.sendMessage("You jumped into the portal");
        // TODO ADD
        //  portalOwner.teleport(Bukkit.getPlayer(teleportDestination));
        break;
      case "location":
        // TODO
        break;
    }
  }
}
