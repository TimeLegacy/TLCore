package net.timelegacy.tlcore.handler;

import java.util.ArrayList;
import net.timelegacy.tlcore.datatype.CoreSystem;

public class CoreSystemHandler {
  private static ArrayList<CoreSystem> systemsDisabled = new ArrayList<>();

  public static boolean isEnabled(CoreSystem system) {
    return !systemsDisabled.contains(system);
  }

  public static void setDisabled(CoreSystem system) {
    if (!systemsDisabled.contains(system)) {
      systemsDisabled.add(system);
    }
  }
}
