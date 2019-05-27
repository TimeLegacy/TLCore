package net.timelegacy.tlcore.datatype;

import java.util.ArrayList;

public class Polygon {

    protected AABB3D edgeShell;
    protected ArrayList<AABB3D> polyParts;

    public Polygon(AABB3D poly) {
        edgeShell = poly;
        polyParts = new ArrayList<>();
        polyParts.add(poly);
    }

    public Polygon(AABB3D[] poly) {
        if (poly.length > 0) {
            edgeShell = poly[0];
            polyParts = new ArrayList<>();
            for (AABB3D polygonPart : poly) {
                addPartMath(polygonPart);
            }
        }

    }

    public static boolean isInside(Polygon polygon, AABB3D object) {
        if (AABB3D.collides(polygon.edgeShell, object)) {
            for (AABB3D polyPart : polygon.polyParts) {
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
        if (AABB3D.collides(poly, edgeShell)) {
            polyParts.add(poly);
        } else {
            polyParts.add(poly);
            AABB3D.expand(edgeShell, poly);
        }

    }
}
