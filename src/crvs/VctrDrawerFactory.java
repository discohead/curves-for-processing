package crvs;

import processing.core.PApplet;

/**
 * The type Vctr drawer factory.
 */
public class VctrDrawerFactory {
    /**
     * The Parent.
     */
    PApplet parent;

    /**
     * Instantiates a new Vctr drawer factory.
     *
     * @param parent the parent
     */
    public VctrDrawerFactory(PApplet parent) {
        this.parent = parent;
    }

    /**
     * Ellipse vctr drawer.
     *
     * @param width  the width
     * @param height the height
     * @param drawer the drawer
     * @return the vctr drawer
     */
    public VctrDrawer ellipse(float width, float height, VctrDrawer drawer) {
        return (v) -> {
            if (drawer != null) drawer.draw(v);
            this.parent.ellipse(v.x, v.y, width, height);
        };
    }

    /**
     * Rect vctr drawer.
     *
     * @param width  the width
     * @param height the height
     * @param drawer the drawer
     * @return the vctr drawer
     */
    public VctrDrawer rect(float width, float height, VctrDrawer drawer) {
        return (v) -> {
            if (drawer != null) drawer.draw(v);
            this.parent.rect(v.x, v.y, width, height);
        };
    }

}
