package crvs;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * The type Mdl.
 */
public abstract class Mdl extends Shp {
    /**
     * The Shps.
     */
    public ArrayList<Shp> shps;

    /**
     * Instantiates a new Mdl.
     *
     * @param parent   the parent
     * @param points   the points
     * @param shps     the shps
     * @param origin   the origin
     * @param scale    the scale
     * @param rotation the rotation
     */
    Mdl(PApplet parent, ArrayList<PVector> points, ArrayList<Shp> shps, PVector origin, PVector scale, float rotation) {
        super(parent, points, origin, scale, rotation);
        this.shps = shps;
    }
}
