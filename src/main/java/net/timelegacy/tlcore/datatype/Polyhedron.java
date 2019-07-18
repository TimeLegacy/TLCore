package net.timelegacy.tlcore.datatype;

import java.util.ArrayList;
import org.bukkit.util.Vector;

public class Polyhedron {

  protected AABB3D edgeShell;
  protected ArrayList<AABB3D> polyParts;

  public Polyhedron(AABB3D poly) {
    edgeShell = poly;
    polyParts = new ArrayList<>();
    polyParts.add(poly);
  }

  public Polyhedron(AABB3D[] poly) {
    if (poly.length > 0) {
      edgeShell = poly[0];
      polyParts = new ArrayList<>();
      for (AABB3D polygonPart : poly) {
        addPartMath(polygonPart);
      }
    }

  }
  public Polyhedron(double xCenter, double yCenter, double zCenter, double xSize, double ySize, double zSize){
    AABB3D poly = new AABB3D(new Vector(xCenter, yCenter, zCenter), new Vector(xSize, ySize, zSize));
    edgeShell = poly;
    polyParts = new ArrayList<>();
    polyParts.add(poly);
  }

  public static boolean isInside(Polyhedron polyhedron, AABB3D object) {
    if (AABB3D.collides(polyhedron.edgeShell, object)) {
      for (AABB3D polyPart : polyhedron.polyParts) {
        if (AABB3D.collides(polyPart, object)) {
          return true;
        }
      }
    }

    return false;
  }

  public void addPart(AABB3D poly) {
    addPartMath(poly);

  }

  public void addPart(AABB3D[] poly) {
    for (AABB3D polygonPart : poly) {
      addPartMath(polygonPart);
    }
  }

  private void addPartMath(AABB3D poly) {
    polyParts.add(poly);
    edgeShell = AABB3D.expand(edgeShell, poly);
  }
}
