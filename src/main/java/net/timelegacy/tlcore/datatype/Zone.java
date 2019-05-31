package net.timelegacy.tlcore.datatype;

public class Zone {
    protected String shortname;
    protected String formalname;
    protected Polygon boundingBoxes;

    public Zone(String shortname, String formalname, Polygon boundingBoxes) {
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

    public Polygon getBoundingBoxes() {
        return boundingBoxes;
    }

    public void setBoundingBoxes(Polygon p) {
        boundingBoxes = p;

    }
}