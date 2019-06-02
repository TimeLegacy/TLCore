package net.timelegacy.tlcore.utils;

import net.timelegacy.tlcore.TLCore;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleUtils {

    /**
     * Draw a line of particles
     *
     * @param from      location from
     * @param to        location to
     * @param effect    particle effect
     * @param particles particle amount
     * @param r         red
     * @param g         green
     * @param b         blue
     */
    public static void drawParticleLine(
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
                display(r, g, b, location);
            } else {
                display(effect, loc);
            }
        }
    }

    /**
     * Create particle helix
     *
     * @param loc location
     * @param i angle of helix
     * @param effect particle effect
     */
    public static void playHelix(final Location loc, final float i, final Particle effect) {
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
        runnable.runTaskTimer(TLCore.getPlugin(), 0, 1);
    }

    /**
     * Display particles
     *
     * @param effect particle effect
     * @param loc location
     * @param amount amount
     * @param speed speed
     */
    public static void display(Particle effect, Location loc, int amount, int speed) {
        loc.getWorld().spawnParticle(effect, loc, amount, 0f, 0f, 0f, speed);
    }

    /**
     * Display particles
     *
     * @param effect particle effect
     * @param loc location
     */
    public static void display(Particle effect, Location loc) {
        loc.getWorld().spawnParticle(effect, loc, 1, 0f, 0f, 0f, 0);
    }

    /**
     * Display particles
     * @param red red
     * @param green green
     * @param blue blue
     * @param loc location
     */
    public static void display(int red, int green, int blue, Location loc) {
        loc.getWorld()
                .spawnParticle(
                        Particle.REDSTONE,
                        loc,
                        1,
                        0f,
                        0f,
                        0f,
                        0,
                        new Particle.DustOptions(Color.fromRGB(red, green, blue), 0.8f));
    }
}