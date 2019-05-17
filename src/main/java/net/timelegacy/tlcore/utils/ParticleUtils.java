package net.timelegacy.tlcore.utils;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleUtils {

    // TODO fix dis shit

    public void drawParticleLine(
        Location from, Location to, Particle effect, int particles, int r, int g, int b) {
        Location location = from.clone();
        Location target = to.clone();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        int step = 0;
        for (int i = 0; i < particles; i++) {
            if (step >= (double) particles) {
                step = 0;
            }
            step++;
            loc.add(v);

            if (effect == Particle.REDSTONE) {
                loc.getWorld()
                    .spawnParticle(
                        Particle.REDSTONE,
                        loc,
                        14,
                        0.1f,
                        0.1f,
                        0.1f,
                        1,
                        new Particle.DustOptions(Color.fromRGB(r, g, b), 1));
            } else {
                display(effect, loc);
            }
        }
    }

    public void playHelix(final Location loc, final float i, final Particle effect) {
        BukkitRunnable runnable =
            new BukkitRunnable() {
                double radius = 0;
                double step;
                double y = loc.getY();
                Location location = loc.clone().add(0, 3, 0);

                @Override
                public void run() {
                    double inc = (2 * Math.PI) / 50;
                    double angle = step * inc + i;
                    Vector v = new Vector();
                    v.setX(Math.cos(angle) * radius);
                    v.setZ(Math.sin(angle) * radius);
                    if (effect == Particle.REDSTONE) {
                        display(0, 0, 255, location);
                    } else {
                        display(effect, location);
                    }
                    location.subtract(v);
                    location.subtract(0, 0.1d, 0);
                    if (location.getY() <= y) {
                        cancel();
                    }
                    step += 4;
                    radius += 1 / 50f;
                }
            };
        runnable.runTaskTimer(TLCore.getInstance(), 0, 1);
    }

  /*

  spawnParticle
            online
              .getWorld()
              .spawnParticle(
                  Particle.REDSTONE,
                  online.getLocation(),
                  14,
                  0.1f,
                  0.1f,
                  0.1f,
                  0,
                  new Particle.DustOptions(Color.WHITE, 1));

  particle - the particle to spawn
  location - the location to spawn at
  count - the number of particles
  offsetX - the maximum random offset on the X axis
  offsetY - the maximum random offset on the Y axis
  offsetZ - the maximum random offset on the Z axis
  extra - the extra data for this particle, depends on the particle used (normally speed)
  data - the data to use for the particle or null, the type of this depends on Particle.getDataType()
   */

    public void display(Particle effect, Location loc, int amount, int speed) {
        loc.getWorld().spawnParticle(effect, loc, amount, 0.1f, 0.1f, 0.1f, speed);
    }

    public void display(Particle effect, Location loc) {
        loc.getWorld().spawnParticle(effect, loc, 10, 0.1f, 0.1f, 0.1f, 1);
    }

    public void display(int red, int green, int blue, Location loc) {
        loc.getWorld()
            .spawnParticle(
                Particle.REDSTONE,
                loc,
                14,
                0.1f,
                0.1f,
                0.1f,
                0,
                new Particle.DustOptions(Color.fromRGB(red, green, blue), 1));
    }
}