package crvs;

import processing.core.PVector;

import java.util.Objects;

/**
 * The type Window.
 */
public class Window {

    /**
     * The Width.
     */
    public int width;

    /**
     * The Height.
     */
    public int height;

    /**
     * The origin point of this window
     */
    public PVector origin;

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
     * @param width       the width
     * @param height      the height
     * @param origin      the origin
     * @param translation the translation
     * @param scale       the scale
     * @param rotation    the rotation
     */
    public Window(int width, int height, PVector origin, PVector translation, PVector scale, float rotation) {
        this.origin = Objects.requireNonNullElseGet(origin, PVector::new);
        this.translation = Objects.requireNonNullElseGet(translation, PVector::new);
        this.scale = Objects.requireNonNullElseGet(scale, () -> new PVector(1, 1));
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    /**
     * Instantiates a new Window.
     *
     * @param width  the width
     * @param height the height
     * @param x      the x
     * @param y      the y
     */
    public Window(int width, int height, int x, int y) {
        this(width, height, new PVector(x, y), null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param width  the width
     * @param height the height
     * @param origin the origin
     */
    public Window(int width, int height, PVector origin) {
        this(width, height, origin, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param width  the width
     * @param height the height
     */
    public Window(int width, int height) {
        this(width, height, null, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param size   the size
     * @param origin the origin
     */
    public Window(int size, PVector origin) {
        this(size, size, origin, null, null, 0);
    }

    /**
     * Instantiates a new Window.
     *
     * @param size the size
     */
    public Window(int size) {
        this(size, size, null, null,  null, 0);
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
