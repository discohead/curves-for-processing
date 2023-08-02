package crvs;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * The type Shp.
 */
public abstract class Shp {
    /**
     * The Parent.
     */
    public PApplet parent;
    /**
     * The Origin.
     */
    public PVector origin;
    /**
     * The Scale.
     */
    public PVector scale;
    /**
     * The Rotation.
     */
    public float rotation;
    /**
     * The Points.
     */
    public ArrayList<PVector> points;

    /**
     * Instantiates a new Shp.
     *
     * @param parent   the parent
     * @param points   the points
     * @param origin   the origin
     * @param scale    the scale
     * @param rotation the rotation
     */
    Shp(PApplet parent, ArrayList<PVector> points, PVector origin, PVector scale, float rotation) {
        this.parent = parent;
        this.points = points;
        this.origin = origin;
        this.scale = scale;
        this.rotation = rotation;
    }

    /**
     * Draw.
     *
     * @param origin   the origin
     * @param scale    the scale
     * @param rotation the rotation
     */
    abstract void draw(PVector origin, PVector scale, float rotation);

    /**
     * Draw.
     *
     * @param origin the origin
     * @param scale  the scale
     */
    public void draw(PVector origin, PVector scale) {
        this.draw(origin, scale, this.rotation);
    }

    /**
     * Draw.
     *
     * @param origin the origin
     */
    public void draw(PVector origin) {
        this.draw(origin, this.scale, this.rotation);
    }

    /**
     * Draw.
     */
    public void draw() {
        this.draw(this.origin, this.scale, this.rotation);
    }
}
