package net.timelegacy.tlcore.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.timelegacy.tlcore.TLCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldDownloaderUtils {

  private static TLCore plugin = TLCore.getPlugin();

  public void loadWorld(final String world) {
    if (Bukkit.getWorld(world) != null) {
      System.out.println("Couldn't load world ''" + world + "'' since it all ready exists");
    }
    Bukkit.createWorld(new WorldCreator(world));
  }

  public void unloadWorld(String world) {
    World w = Bukkit.getWorld(world);
    if (w != null) {
      for (Player p : w.getPlayers()) {
        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
      }
      Bukkit.unloadWorld(w, true);
    }
  }

  public void deleteWorld(String world) {
    World delete = Bukkit.getWorld(world);
    if (delete != null) {
      unloadWorld(world);

      File deleteFolder = delete.getWorldFolder();
      deleteWorld(deleteFolder);
    }
  }

  public void downloadFile(final String fileURL, final String fileName, final String saveDir)
      throws IOException {
    final URL url = new URL(fileURL);
    Bukkit.getScheduler()
        .runTaskAsynchronously(
            plugin,
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

  public void downloadMap(String link, String zip_name, String world_name) {

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
    (new File(file_path)).delete();
    System.out.println("DOWNLOADED map `" + world + "`.");

    for (Entity entity : Bukkit.getWorld(world).getEntities()) {
      entity.remove();
    }
  }

  public String getBaseDirectory() {
    return Bukkit.getServer().getWorldContainer().getAbsolutePath();
  }

  public void unzip(String zipFilePath, String destDirectory) throws IOException {
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

  private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[4096];

    int read1;
    while ((read1 = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read1);
    }
    bos.close();
  }

  public boolean deleteWorld(final File path) {
    Bukkit.getScheduler()
        .runTaskAsynchronously(
            plugin,
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
}
