package net.timelegacy.tlcore.utils;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class ParticleUtils {

    public static void drawParticleLine(Location from, Location to, int particles, int r, int g, int b) {
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
            if (step >= (double) particles)
                step = 0;
            step++;
            loc.add(v);

            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), r, g, b, 1, 0, null);
        }
    }

    public static void drawParticleLine(Location from, Location to, Particle effect, double distance) {
        Location location = from.clone();
        Location target = to.clone();
        Vector link = target.toVector().subtract(location.toVector());
        double length = link.length();
        link.normalize();
        double particles = length * distance;
        double ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        int step = 0;
        for (int i = 0; i < particles; i++) {
            if (step >= particles)
                step = 0;
            step++;
            loc.add(v);

            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 1, 0, null);
        }
    }

    public static void playHelix(final Location loc, final float i, final Particle effect) {
        BukkitRunnable runnable = new BukkitRunnable() {
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

    public static void display(Particle effect, Location loc, int amount, int speed) {
        loc.getWorld().spawnParticle(effect, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, speed, amount, null);
    }

    public static void display(Particle effect, Location loc) {
        loc.getWorld().spawnParticle(effect, loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 1, 0, null);

    }

    public static void display(int red, int green, int blue, Location loc) {
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), red / 255, green / 255, blue / 255, 1, 0, null);
    }

}
