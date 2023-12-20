package crvs;

import processing.core.PVector;

import java.util.Objects;

/**
 * The type Window.
 */
public class Window {

    /**
     * The origin point of this window
     */
    public PVector origin;

    /**
     * The Width.
     */
    public int width;

    /**
     * The Height.
     */
    public int height;

    /**
     * The current translation of the window from its origin.
     */
    public PVector translation;

    /**
     * The scale factor of the window, multiplies the width and height.
     */
    public PVector scale;

    /**
     * The rotation of the window in degrees.
     */
    public float rotation;

    /**
     * Instantiates a new Window.
     *
     * @param origin      the origin
     * @param width       the width
     * @param height      the height
     * @param translation the translation
     * @param scale       the scale
     * @param rotation    the rotation
     */
    public Window(PVector origin, int width, int height, PVector translation, PVector scale, float rotation) {
        this.origin = Objects.requireNonNullElseGet(origin, PVector::new);
        this.translation = Objects.requireNonNullElseGet(translation, PVector::new);
        this.scale = Objects.requireNonNullElseGet(scale, () -> new PVector(1, 1));
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    /**
     * Instantiates a new Window.
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height the height
     */
    public Window(int x, int y, int width, int height) {
        this(new PVector(x, y), width, height, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     * @param origin the origin
     * @param width  the width
     * @param height the height
     * @param translation the translation
     */
    public Window(PVector origin, int width, int height, PVector translation) {
        this(origin, width, height, translation, null, 0);
    }

    /**
     * Instantiates a new Window.
     * @param origin the origin
     * @param width  the width
     * @param height the height
     */
    public Window(PVector origin, int width, int height) {
        this(origin, width, height,  null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param width  the width
     * @param height the height
     */
    public Window(int width, int height) {
        this(null, width, height, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param origin the origin
     * @param size   the size
     */
    public Window(PVector origin, int size) {
        this(origin, size, size, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param size the size
     */
    public Window(int size) {
        this(null, size, size, null,  null, 0);
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public float getWidth() {
        return this.width * this.scale.x;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public float getHeight() {
        return this.height * this.scale.y;
    }

    /**
     * Center p vector.
     *
     * @return the p vector
     */
    public PVector center() {
        return this.origin.copy().add(this.width/2f, this.height/2f);
    }

    /**
     * Apply p vector.
     *
     * @param v the v
     * @return the p vector
     */
    public PVector apply(PVector v) {
        v.x *= (this.getWidth() - 1);
        v.y *= (this.getHeight() - 1);
        v = this.transform(v);
        v.add(this.origin);
        return Utils.clipped(
                v, this.origin.x,
                this.origin.x + this.getWidth(),
                this.origin.y,
                this.origin.y + this.getHeight()
        );
    }


    /**
     * Apply this window's transform to a vector
     *
     * @param v
     * @return
     */
    private PVector transform(PVector v) {
        return Utils.transform(v, this.center(), this.scale, this.translation, this.rotation);
    }

}
