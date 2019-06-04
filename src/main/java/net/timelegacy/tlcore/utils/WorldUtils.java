package net.timelegacy.tlcore.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldUtils {

  private static TLCore plugin = TLCore.getPlugin();

  /**
   * Load world
   *
   * @param world world name
   */
  public static void loadWorld(String world) {
    if (Bukkit.getWorld(world) != null) {
      System.out.println("Couldn't load world ''" + world + "'' since it all ready exists");
    }

    Bukkit.createWorld(new WorldCreator(world));
  }

  /**
   * Unload world
   *
   * @param world world name
   */
  public static void unloadWorld(String world) {
    World w = Bukkit.getWorld(world);
    if (w != null) {
      for (Player p : w.getPlayers()) {
        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
      }

      Bukkit.unloadWorld(w, true);
    }
  }

  /**
   * Delete world
   *
   * @param world world name
   */

  public static void deleteWorld(String world) {
    World delete = Bukkit.getWorld(world);
    if (delete != null) {
      unloadWorld(world);

      File deleteFolder = delete.getWorldFolder();
      deleteWorld(deleteFolder);
    }
  }

  /**
   * Delete world by file
   *
   * @param path path to world
   */
  public static boolean deleteWorld(final File path) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin,
        new Runnable() {
          @Override
          public void run() {
            if (path.exists()) {
              File[] files = path.listFiles();
              assert files != null;
              for (File file : files) {
                if (file.isDirectory()) {
                  deleteWorld(file);
                } else {
                  file.delete();
                }
              }
            }
          }
        });
    return (path.delete());
  }

  /**
   * Circle as locations
   *
   * @param loc location
   * @param r radius
   * @param h height
   * @param hollow hollow
   * @param sphere sphere
   * @param plus_y plus y
   */
  public static List<Location> circle(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
    List<Location> circleblocks = new ArrayList<>();
    int cx = loc.getBlockX();
    int cy = loc.getBlockY();
    int cz = loc.getBlockZ();

    for (int x = cx - r; x <= cx + r; x++) {
      for (int z = cz - r; z <= cz + r; z++) {
        for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
          double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
          if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
            Location l = new Location(loc.getWorld(), x, y + plus_y, z);
            circleblocks.add(l);
          }
        }
      }
    }

    return circleblocks;
  }

  /**
   * Load location from config
   *
   * @param config config file
   * @param world bukkit world
   * @param key config key
   */
  public static Location locationFromConfig(ConfigurationSection config, World world, String key) {
    return new Location(world,
        config.getDouble(key + ".x"),
        config.getDouble(key + ".y"),
        config.getDouble(key + ".z"),
        (float) config.getDouble(key + ".yaw", 0),
        (float) config.getDouble(key + ".pitch", 0));
  }

  private static void downloadFile(final String fileURL, final String fileName, final String saveDir)
      throws IOException {
    final URL url = new URL(fileURL);
    Bukkit.getScheduler().runTaskAsynchronously(plugin,
        new Runnable() {
          @Override
          public void run() {
            HttpURLConnection httpConn = null;
            try {
              httpConn = (HttpURLConnection) url.openConnection();
              int responseCode = httpConn.getResponseCode();
              if (responseCode == 200) {
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = saveDir + File.separator + fileName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                byte[] buffer = new byte[4096];

                int bytesRead1;
                while ((bytesRead1 = inputStream.read(buffer)) != -1) {
                  outputStream.write(buffer, 0, bytesRead1);
                }

                outputStream.close();
                inputStream.close();
              } else {
                System.out.println("Failed to download map, returned ERROR: " + responseCode);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
            assert httpConn != null;
            httpConn.disconnect();
          }
        });
  }

  public static void downloadMap(String link, String zip_name, String world_name) {
    if (Bukkit.getWorld(world_name) != null) {
      deleteWorld(world_name);
    }

    String file_name = zip_name + ".zip";
    String file_path = getBaseDirectory().replace(".", "") + file_name;
    String world = world_name.replace(".", "");

    try {
      downloadFile(link, file_name, getBaseDirectory());
      unzip(file_path, getBaseDirectory().replace(".", "") + world.replace(".", ""));
    } catch (IOException var10) {
      var10.printStackTrace();
    }

    loadWorld(world);
    new File(file_path).delete();
    System.out.println("DOWNLOADED map `" + world + "`.");

    for (Entity entity : Bukkit.getWorld(world).getEntities()) {
      entity.remove();
    }
  }

  private static String getBaseDirectory() {
    return Bukkit.getServer().getWorldContainer().getAbsolutePath();
  }

  private static void unzip(String zipFilePath, String destDirectory) throws IOException {
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }

    ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

    for (ZipEntry entry = zipIn.getNextEntry(); entry != null; entry = zipIn.getNextEntry()) {
      String filePath = destDirectory + File.separator + entry.getName();
      if (!entry.isDirectory()) {
        extractFile(zipIn, filePath);
      } else {
        new File(filePath).mkdir();
      }

      zipIn.closeEntry();
    }

    zipIn.close();
  }

  private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[4096];

    int read1;
    while ((read1 = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read1);
    }

    bos.close();
  }
}
