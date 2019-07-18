package net.timelegacy.tlcore.datatype;

public class Zone {

  protected String shortname;
  protected String formalname;
  protected Polyhedron boundingBoxes;

  public Zone(String shortname, String formalname, Polyhedron boundingBoxes) {
    this.shortname = shortname;
    this.formalname = formalname;
    this.boundingBoxes = boundingBoxes;
  }

  public String getShortName() {
    return shortname;
  }

  public String getFormalname() {
    return formalname;
  }

  public Polyhedron getBoundingBoxes() {
    return boundingBoxes;
  }

  public void setBoundingBoxes(Polyhedron p) {
    boundingBoxes = p;

  }
}