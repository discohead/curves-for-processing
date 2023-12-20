package crvs;

// import com.krab.lazy.*;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;
import processing.data.FloatList;

/**
 * The Crv class represents a curve in a 2D space. This curve is defined by a
 * variety of parameters such as amplitude, rate, phase and bias, which can be
 * either constant values or other curves. The class uses a functional approach
 * by using a FloatOp interface for processing the curve.
 * <p>
 * Each Crv instance is bound to a PApplet parent and optionally a separate
 * PGraphics window for rendering. The curve's points are calculated based on
 * the provided FloatOp operation. Transformations like translation, scaling and
 * rotation can be applied to the curve. The curve also supports different
 * bounding behaviors (clipping, wrapping, and folding).
 * <p>
 * Crv is primarily designed for creating complex and evolving shapes in a
 * Processing environment.
 */
@SuppressWarnings("unused")
public class Crv implements FloatOp {

	/**
	 * Static PVector representing the universal center of all curves.
	 */
	static public PVector uCenter = new PVector(0.5f, 0.5f);

	/**
	 * Parent PApplet, typically represents the main sketch.
	 */
	PApplet parent;

	/**
	 * A function that performs an operation on a float and returns a float.
	 */
	public FloatOp op;

	/**
	 * Optional child curve for amplitude.
	 */
	public Crv amp;

	/**
	 * Optional child curve for rate (or frequency).
	 */
	public Crv rate;

	/**
	 * Optional child curve for phase.
	 */
	public Crv phase;

	/**
	 * Optional child curve for bias (offset).
	 */
	public Crv bias;

	/**
	 * Static offset for amplitude scaling
	 */
	public float ampOffset = 1.0f;

	/**
	 * Static offset for rate or frequency
	 */
	public float rateOffset = 1.0f;

	/**
	 * Static offset for phase
	 */
	public float phaseOffset;

	/**
	 * Static offset for bias or er... offset.
	 */
	public float biasOffset;

	/**
	 * Probability of jitter being applied to a point
	 */
	public float jitterProbability;

	/**
	 * Scale of the jitter when applied
	 */
	public float jitterScale;

	/**
	 * The X resolution of the curve, i.e. the number of points.
	 */
	public int resolution;

	/**
	 * The Y resolution of the curve, i.e. the number of possible "levels"
	 */
	public int quantization;

	/**
	 * The color to use when drawing
	 */
	public int color;

	/**
	 * If true, the area enclosed by the curve is filled with color.
	 */
	public boolean fill;

	/**
	 * The Window which defines where the curve is drawn.
	 */
	public Window window;

	/**
	 * The original position of the curve.
	 */
	public PVector origin;

	/**
	 * The current translation of the curve from its origin.
	 */
	public PVector translation;

	/**
	 * The scaling factor applied to the curve
	 */
	public PVector scale;

	/**
	 * The rotation of the curve in degrees.
	 */
	public float rotation;

	/**
	 * Random value between 0 and 1 associated with this instance of the Crv
	 */
	public float seed;

	/**
	 * Random gaussian value associated with this instance of the Crv
	 */
	public float gaussian;

	/**
	 * The Bounding enum defines the different types of boundary behaviors a Crv
	 * object can have when its points exceed the limits of the rendering window.
	 * The four types are:
	 *
	 * <ul>
	 * <li>NONE: The points of the curve are not constrained and can exceed the
	 * window's limits.</li>
	 * <li>CLIPPING: The points of the curve exceeding the window's limits are cut
	 * off, resulting in a hard edge at the boundary.</li>
	 * <li>WRAPPING: The points of the curve that exceed the window's limits wrap
	 * around to the opposite edge of the window.</li>
	 * <li>FOLDING: The points of the curve that exceed the window's limits are
	 * reflected back into the window, creating a folding effect.</li>
	 * </ul>
	 */
	public enum Bounding {
		/**
		 * No boundary constraint. The points of the curve can exceed the window's
		 * limits.
		 */
		NONE,

		/**
		 * Clipping boundary constraint. The points of the curve exceeding the window's
		 * limits are cut off.
		 */
		CLIPPING,

		/**
		 * Wrapping boundary constraint. The points of the curve that exceed the
		 * window's limits wrap around to the opposite edge of the window.
		 */
		WRAPPING,

		/**
		 * Folding boundary constraint. The points of the curve that exceed the window's
		 * limits are reflected back into the window.
		 */
		FOLDING
	}

	/**
	 * The bounding mode of this curve
	 */
	private Bounding bounding;

	/**
	 * Gets the bounding mode of the curve.
	 *
	 * @return The current bounding mode of the curve.
	 */
	public Bounding getBounding() {
		return this.bounding;
	}

	/**
	 * Sets the bounding mode of the curve.
	 *
	 * @param bounding The new bounding mode to be set for the curve.
	 */
	public void setBounding(Bounding bounding) {
		this.bounding = bounding;
	}

	/**
	 * The enum Component.
	 */
	public enum Component {
		/**
		 * X component.
		 */
		X,
		/**
		 * Y component.
		 */
		Y,
	}

	/*
	 * CONSTRUCTORS
	 */

	/**
	 * Primary constructor. Initializes a new instance of the Crv class.
	 *
	 * @param parent The parent PApplet.
	 * @param window The PGraphics window in which the curve will be drawn.
	 * @param op     The FloatOp function to be applied to the curve, defaults to               linear ramp.
	 */
	public Crv(PApplet parent, Window window, FloatOp op) {
		this.parent = parent;
		if (window == null) {
			this.window = new Window(parent.width, parent.height);
		} else {
			this.window = window;
		}
		this.resolution = this.window.width;
		this.origin = new PVector(0, 0);
		this.translation = new PVector(0, 0);
		this.scale = new PVector(1, 1);
		this.bounding = Bounding.NONE;
		if (op == null) {
			this.op = pos -> pos;
		} else {
			this.op = op;
		}
		this.seed = this.parent.random(1f );
		this.gaussian = this.parent.randomGaussian();
	}

	/**
	 * Constructor that only takes a parent PApplet, and uses default values for
	 * other parameters.
	 *
	 * @param parent The parent PApplet.
	 */
	public Crv(PApplet parent) {
		this(parent, null, null);
	}

	/**
	 * Constructor that takes a parent PApplet and a PGraphics window, uses default
	 * op.
	 *
	 * @param parent The parent PApplet.
	 * @param window the window
	 */
	public Crv(PApplet parent, Window window) {
		this(parent, window, null);
	}

	/**
	 * Constructor that takes a parent PApplet and a FloatOp, and uses default
	 * values for other parameters.
	 *
	 * @param parent The parent PApplet.
	 * @param op     The FloatOp function to be applied to the curve.
	 */
	public Crv(PApplet parent, FloatOp op) {
		this(parent, null, op);
	}

	/**
	 * Constructor that takes a parent PApplet and four Crv objects for amplitude,
	 * rate, phase, and bias.
	 *
	 * @param parent The parent PApplet.
	 * @param amp    The Crv object representing the amplitude.
	 * @param rate   The Crv object representing the rate.
	 * @param phase  The Crv object representing the phase.
	 * @param bias   The Crv object representing the bias.
	 */
	public Crv(PApplet parent, Crv amp, Crv rate, Crv phase, Crv bias) {
		this(parent);
		this.amp = amp;
		this.rate = rate;
		this.phase = phase;
		this.bias = bias;
	}

	/**
	 * Constructor that takes a parent PApplet and four float offsets for amplitude,
	 * rate, phase, and bias.
	 *
	 * @param parent      The parent PApplet.
	 * @param ampOffset   The amplitude offset as a float.
	 * @param rateOffset  The rate offset as a float.
	 * @param phaseOffset The phase offset as a float.
	 * @param biasOffset  The bias offset as a float.
	 */
	public Crv(PApplet parent, float ampOffset, float rateOffset, float phaseOffset, float biasOffset) {
		this(parent);
		this.ampOffset = ampOffset;
		this.rateOffset = rateOffset;
		this.phaseOffset = phaseOffset;
		this.biasOffset = biasOffset;
	}

	/**
	 * Constructor that takes a parent PApplet, four Crv objects for amplitude,
	 * rate, phase, and bias, and four float offsets for each.
	 *
	 * @param parent      The parent PApplet.
	 * @param amp         The Crv object representing the amplitude.
	 * @param rate        The Crv object representing the rate.
	 * @param phase       The Crv object representing the phase.
	 * @param bias        The Crv object representing the bias.
	 * @param ampOffset   The amplitude offset as a float.
	 * @param rateOffset  The rate offset as a float.
	 * @param phaseOffset The phase offset as a float.
	 * @param biasOffset  The bias offset as a float.
	 */
	public Crv(PApplet parent, Crv amp, Crv rate, Crv phase, Crv bias, float ampOffset, float rateOffset,
			float phaseOffset, float biasOffset) {
		this(parent);
		this.amp = amp;
		this.rate = rate;
		this.phase = phase;
		this.bias = bias;
		this.ampOffset = ampOffset;
		this.rateOffset = rateOffset;
		this.phaseOffset = phaseOffset;
		this.biasOffset = biasOffset;
	}

	/*
	 * FLOAT UTILITIES
	 */

	public float apply(float pos) {
		return this.calculate(pos);
	}

	/**
	 * Applies the curve function to the given position.
	 *
	 * @param pos The position in the curve, a value between 0 and 1.
	 * @return The output of the curve function at the given position.
	 */
	protected float calculate(float pos) {
		return this.quantize(this.op.apply(pos));
	}

	/**
	 * Applies amplitude and bias transformations to the given value at the given
	 * position.
	 *
	 * @param value The value to transform.
	 * @param pos   The position in the curve, a value between 0 and 1.
	 * @return The transformed value.
	 */
	protected float ampBias(float value, float pos) {
		float ampFactor = this.ampOffset;
		if (this.amp != null) {
			ampFactor *= this.amp.yAt(pos);
		}
		ampFactor = ampFactor/2f;
		value = value * ampFactor + ampFactor;
		if (this.bias != null) {
			value += this.bias.yAt(pos);
		}
		return value + this.biasOffset;
	}

	/**
	 * Calculates a position value based on input rate and phase.
	 *
	 * @param pos Original position.
	 * @return Calculated position.
	 */
	protected float calcPos(float pos) {
		pos = Math.abs(pos);
		pos = pos * this.rateOffset;
		if (pos > 1.0f)
			pos = pos % 1.0f;
		if (this.rate != null) {
			pos = pos * this.rate.yAt(pos);
		}
		if (this.phase != null) {
			pos = (pos + this.phase.yAt(pos));
		}
		pos = pos + this.phaseOffset;
		if (pos > 1.0f)
			return pos % 1.0f;
		return pos;
	}

	/**
	 * Quantize float.
	 *
	 * @param y the y
	 * @return the float
	 */
	protected float quantize(float y) {
		if (this.quantization > 1) {
			float levelSize = 1.0f / (this.quantization - 1);
			int quantizedLevel = Math.round(y / levelSize);
			return quantizedLevel * levelSize;
		}
		return y;
	}

	/**
	 * Evaluates the curve at the given position for the specified Component.
	 * <p>
	 * The position is first transformed by the curve's rate and phase properties.
	 * The curve function is then evaluated at this position. The result is then
	 * converted to bipolar form (-1 to 1), amplitude and bias are applied, and
	 * finally the result rectified back to unipolar form (0 to 1).
	 *
	 * @param component Component.X or Component.Y
	 * @param pos       The original position, a value between 0 and 1.
	 * @return The value of the curve at the transformed position, a value between 0         and 1.
	 */
	public float componentAt(Component component, float pos) {
		if (component == Component.X) return pos;
		float modPos = this.calcPos(pos);
		float value = this.calculate(modPos);
		value = this.bipolarize(value);
		value = this.ampBias(value, modPos);
		return value;
	}

	/**
	 * Evaluates the curve at the given position for the Y Component.
	 * <p>
	 * The position is first transformed by the curve's rate and phase properties.
	 * The curve function is then evaluated at this position. The result is then
	 * converted to bipolar form (-1 to 1), amplitude and bias are applied, and
	 * finally the result rectified back to unipolar form (0 to 1).
	 *
	 * @param pos The original position, a value between 0 and 1.
	 * @return The value of the curve at the transformed position, a value between 0         and 1.
	 */
	public float yAt(float pos) {
		return this.componentAt(Component.Y, pos);
	}

	/**
	 * X at float.
	 *
	 * @param pos the pos
	 * @return the float
	 */
	public float xAt(float pos) {
		return this.componentAt(Component.X, pos);
	}

	/**
	 * Wraps a given value into a specified range [min, max).
	 *
	 * @param value The value to wrap.
	 * @param min   The minimum value of the range.
	 * @param max   The maximum value of the range.
	 * @return The wrapped value.
	 */
	public float wrap(float value, float min, float max) {
		float range = max - min;
		float wrappedValue = (value - min) % range;
		if (wrappedValue < 0) {
			wrappedValue += range;
		} else if (wrappedValue == 0 && value == max) {
			wrappedValue = range;
		}
		return wrappedValue + min;
	}

	/**
	 * Folds a given value into a specified range [min, max].
	 *
	 * @param value The value to fold.
	 * @param min   The minimum value of the range.
	 * @param max   The maximum value of the range.
	 * @return The folded value.
	 */
	public float fold(float value, float min, float max) {
		float range = max - min;
		float foldedValue = (value - min) % (2 * range);
		if (foldedValue < 0) {
			foldedValue += 2 * range;
		}
		return max - Math.abs(foldedValue - range);
	}

	/**
	 * Converts a unipolar value [0, 1] to a bipolar value [-1, 1].
	 *
	 * @param unipolar The unipolar value to convert.
	 * @return The converted bipolar value.
	 */
	public float bipolarize(float unipolar) {
		return unipolar * 2f - 1f;
	}

	/**
	 * Converts a bipolar value [-1, 1] to a unipolar value [0, 1].
	 *
	 * @param bipolar The bipolar value to convert.
	 * @return The converted unipolar value.
	 */
	public float rectify(float bipolar) {
		return bipolar * 0.5f + 0.5f;
	}

	/**
	 * Clamps the position value between 0 and 1.
	 *
	 * @param pos Position value to be clamped.
	 * @return Clamped position value.
	 */
	public float clamp(float pos) {
		return (float) Math.max(0.0, Math.min(1.0, pos));
	}

	/**
	 * Converts a position from a 0-1 range to radians in a 0-2π range.
	 *
	 * @param pos The position to convert, in the 0-1 range.
	 * @return The corresponding radian value in the 0-2π range.
	 */
	public float pos2Rad(float pos) {
		pos = clamp(pos);
		float degrees = pos * 360;
		return (float) Math.toRadians(degrees);
	}

	/**
	 * Returns a float array of y-values evaluated from the curve.
	 * <p>
	 * This method samples the curve at uniform intervals and returns an array of
	 * the resulting y-values. The number of samples is specified by the numSamples
	 * parameter.
	 *
	 * @param numSamples The number of samples to evaluate.
	 * @param component  Component.X or Component.Y
	 * @return An array of y-values evaluated from the curve.
	 */
	public float[] floatArray(int numSamples, Component component) {
		float step = 1f / numSamples;
		float[] table = new float[numSamples];
		for (int i = 0; i < numSamples; i++) {
			float x = i * step;
			table[i] = this.componentAt(component, x);
		}
		return table;
	}

	/**
	 * Float array float [ ].
	 *
	 * @param numSamples the num samples
	 * @return the float [ ]
	 */
	public float[] floatArray(int numSamples) {
		return this.floatArray(numSamples, Component.Y);
	}

	/**
	 * Returns a float array of y-values evaluated from the curve.
	 * <p>
	 * This method samples the curve at uniform intervals and returns an array of
	 * the resulting y-values. The number of samples is determined by the resolution
	 * property of the curve.
	 *
	 * @return An array of y-values evaluated from the curve.
	 */
	public float[] floatArray() {
		return this.floatArray(this.resolution);
	}

	/**
	 * Returns a FloatList of values evaluated from the curve for the specified
	 * Component.
	 * <p>
	 * This method samples the curve at uniform intervals and returns a FloatList of
	 * the resulting component values. The number of samples is specified by the
	 * numSamples parameter.
	 *
	 * @param component  Component.X or Component.Y
	 * @param numSamples The number of samples to evaluate.
	 * @return A FloatList of component values evaluated from the curve.
	 */
	public FloatList floatList(Component component, int numSamples) {
		float[] array = this.floatArray(numSamples, component);
		return new FloatList(array);
	}

	/**
	 * Returns a FloatList of y-values evaluated from the curve.
	 * <p>
	 * This method samples the curve at uniform intervals and returns a FloatList of
	 * the resulting y-values. The number of samples is specified by the numSamples
	 * parameter.
	 *
	 * @param numSamples The number of samples to evaluate.
	 * @return A FloatList of y-values evaluated from the curve.
	 */
	public FloatList floatList(int numSamples) {
		float[] array = this.floatArray(numSamples);
		return new FloatList(array);
	}

	/**
	 * Returns a FloatList of y-values evaluated from the curve.
	 * <p>
	 * This method samples the curve at uniform intervals and returns a FloatList of
	 * the resulting y-values. The number of samples is determined by the resolution
	 * property of the curve.
	 *
	 * @return A FloatList of y-values evaluated from the curve.
	 */
	public FloatList floatList() {
		float[] array = this.floatArray();
		return new FloatList(array);
	}

	/*
	 * PVECTOR UTILITIES
	 */

	/**
	 * Gets a PVector representing a point on the curve at a given x-position.
	 *
	 * @param pos         The x-position at which to sample the curve.
	 * @param transformed Whether to transform the point.
	 * @return A PVector representing the point on the curve at the given         x-position.
	 */
	public PVector uVector(float pos, boolean transformed) {
		float x = this.componentAt(Component.X, pos);
		float y = this.componentAt(Component.Y, pos);
		PVector p = new PVector(x, y);
		p = this.jitter(p);
		if (transformed) {
			p = this.transform(p);
		}
		p.add(this.origin);
		return this.bounded(p);
	}

	/**
	 * Gets a PVector representing a point on the curve at a given x-position,
	 * applying transformations.
	 *
	 * @param pos The x-position at which to sample the curve.
	 * @return A PVector representing the point on the curve at the given         x-position.
	 */
	public PVector uVector(float pos) {
		return this.uVector(pos, true);
	}

	/**
	 * Gets a PVector representing a point on the curve at a given x-position,
	 * applying transformations and window scaling.
	 *
	 * @param pos         The x-position at which to sample the curve.
	 * @param transformed Whether to apply the curve's transform
	 * @return A PVector representing the point on the curve at the given         x-position.
	 */
	public PVector wVector(float pos, boolean transformed) {
		PVector p = this.uVector(pos, transformed);
		return this.windowed(p);
	}

	/**
	 * Transforms the given PVector according to the curve's scale, rotation, and
	 * translation.
	 *
	 * @param v the v
	 * @return The transformed PVector.
	 */
	public PVector transform(PVector v) {
		return Utils.transform(v, Crv.uCenter, this.scale, this.translation, this.rotation);
	}

	/**
	 * Transforms an array of PVectors according to the curve's scale, rotation, and
	 * translation.
	 *
	 * @param vectors         The array of PVectors to transform.
	 * @param scale           the scale
	 * @param translation     the translation
	 * @param rotationDegrees the rotation degrees
	 * @return The array of transformed PVectors.
	 */
	public PVector[] transform(PVector[] vectors, PVector scale, PVector translation, float rotationDegrees) {
		return Utils.transform(vectors, Crv.uCenter, scale, translation, rotationDegrees);
	}

	/**
	 * Re-scales a PVector to fit within the window's dimensions.
	 *
	 * @param v The PVector to re-scale.
	 * @return The rescaled PVector.
	 */
	public PVector windowed(PVector v) {
		v = this.window.apply(v);
		return v;
	}

	/**
	 * Adjusts a PVector based on the curve's bounding mode (clipping, wrapping,
	 * folding, or none).
	 *
	 * @param v The PVector to adjust.
	 * @return The adjusted PVector.
	 */
	public PVector bounded(PVector v) {
		return switch (this.bounding) {
			case CLIPPING -> clipped(v);
			case WRAPPING -> wrapped(v);
			case FOLDING -> folded(v);
			default -> v;
		};
	}

	/**
	 * Clamps a PVector's components to the range [0, 1].
	 *
	 * @param p   The PVector to clip.
	 * @param min the min
	 * @param max the max
	 * @return The clipped PVector.
	 */
	public PVector clipped(PVector p, float min, float max) {
		p.x = Math.max(min, Math.min(max, p.x));
		p.y = Math.max(min, Math.min(max, p.y));
		return p;
	}

	/**
	 * Clipped p vector [ ].
	 *
	 * @param vectors the vectors
	 * @param min     the min
	 * @param max     the max
	 * @return the p vector [ ]
	 */
	public PVector[] clipped(PVector[] vectors, float min, float max) {
		PVector[] clippedVectors = new PVector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			clippedVectors[i] = this.clipped(vectors[i], min, max);
		}
		return clippedVectors;
	}

	/**
	 * Clipped p vector.
	 *
	 * @param p the p
	 * @return the p vector
	 */
	public PVector clipped(PVector p) {
		return this.clipped(p, 0f, 1f);
	}

	/**
	 * Wraps a PVector's components to the range [0, 1].
	 *
	 * @param p The PVector to wrap.
	 * @return The wrapped PVector.
	 */
	public PVector wrapped(PVector p) {
		p.x = wrap(p.x, 0f, 1f);
		p.y = wrap(p.y, 0f, 1f);
		return p;
	}

	/**
	 * Folds a PVector's components to the range [0, 1].
	 *
	 * @param p The PVector to fold.
	 * @return The folded PVector.
	 */
	public PVector folded(PVector p) {
		p.x = fold(p.x, 0f, 1f);
		p.y = fold(p.y, 0f, 1f);
		return p;
	}

	/**
	 * Jitters a PVector by a random amount.
	 * <p>
	 * The amount of jitter is determined by the jitterScale property of the curve.
	 * The probability of applying jitter is determined by the jitterProbability
	 * property of the curve.
	 *
	 * @param p The PVector to jitter.
	 * @return The jittered PVector.
	 */
	public PVector jitter(PVector p) {
		if (this.jitterScale != 0.0f && this.jitterProbability > 0.0f) {
			if (this.jitterProbability > this.parent.random(1f)) {
				PVector v = PVector.random2D();
				v = PVector.mult(v, jitterScale);
				p.add(v);
			}
		}
		return p;
	}

	/*
	 * POINT ARRAY UTILITIES
	 */

	/**
	 * Generates a 2D float array of points along the curve.
	 *
	 * @param numPoints      The number of points to generate.
	 * @param windowed       Whether to scale the points to the window's dimensions.
	 * @param transformed    Whether to apply the curve's transformations to the points.
	 * @param samplingRateOp the sampling rate op
	 * @return A 2D array of floats representing points along the curve.
	 */
	public float[][] pointArray(int numPoints, boolean windowed, boolean transformed, FloatOp samplingRateOp) {
		PVector[] vectors = this.vectorArray(numPoints, windowed, transformed, samplingRateOp);
		float[][] points = new float[numPoints][2];
		for (int i = 0; i < numPoints; i++) {
			points[i] = vectors[i].array();
		}
		return points;
	}

	/**
	 * Point array float [ ] [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return the float [ ] [ ]
	 */
	public float[][] pointArray(int numPoints, boolean windowed, boolean transformed) {
		return this.pointArray(numPoints, windowed, transformed, null);
	}

	/**
	 * Generates a 2D array of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param windowed Whether to scale the points to the window's               dimensions.
	 * @return A 2D array of floats representing points along the curve.
	 */
	public float[][] pointArray(boolean windowed) {
		return this.pointArray(this.resolution, windowed, true);
	}

	/**
	 * Generates a 2D float array of points along the curve, applying
	 * transformations but not window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @return A 2D array of floats representing points along the curve.
	 */
	public float[][] pointArray(int numPoints) {
		return this.pointArray(numPoints, false, true);
	}

	/**
	 * Generates a 2D float array of points along the curve, applying
	 * transformations and possibly window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @param windowed  Whether to scale the points to the window's dimensions.
	 * @return A 2D array of floats representing points along the curve.
	 */
	public float[][] pointArray(int numPoints, boolean windowed) {
		return this.pointArray(numPoints, windowed, true);
	}

	/**
	 * Generates a 2D float array of points along the curve, applying
	 * transformations and possibly window scaling.
	 *
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return A 2D array of floats representing points along the curve.
	 */
	public float[][] pointArray(boolean windowed, boolean transformed) {
		return this.pointArray(this.resolution, windowed, transformed);
	}

	/*
	 * POINT ARRAY LIST UTILITIES
	 */

	/**
	 * Generates a list of float arrays representing points along the curve.
	 *
	 * @param numPoints      The number of points to generate.
	 * @param windowed       Whether to scale the points to the window's dimensions.
	 * @param transformed    Whether to apply the curve's transformations to the points.
	 * @param samplingRateOp FloatOp to determine variable distribution of points along the curve
	 * @return An ArrayList of float[] representing points along the curve.
	 */
	public ArrayList<float[]> pointList(int numPoints, boolean windowed, boolean transformed, FloatOp samplingRateOp) {
		float[][] points = this.pointArray(numPoints, windowed, transformed, samplingRateOp);
		return new ArrayList<>(Arrays.asList(points));
	}

	/**
	 * Generates a list of float arrays representing points along the curve.
	 *
	 * @param numPoints   The number of points to generate.
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return An ArrayList of float[] representing points along the curve.
	 */
	public ArrayList<float[]> pointList(int numPoints, boolean windowed, boolean transformed) {
		return this.pointList(numPoints, windowed, transformed, null);
	}

	/**
	 * Generates a list of float arrays representing points along the curve,
	 * applying transformations but not window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @return An ArrayList of float[] representing points along the curve.
	 */
	public ArrayList<float[]> pointList(int numPoints) {
		return this.pointList(numPoints, false, true);
	}

	/**
	 * Generates a list of float arrays representing points along the curve,
	 * applying transformations and possibly window scaling.
	 *
	 * @param windowed Whether to scale the points to the window's dimensions.
	 * @return An ArrayList of float[] representing points along the curve.
	 */
	public ArrayList<float[]> pointList(boolean windowed) {
		return this.pointList(this.resolution, windowed, true);
	}

	/**
	 * Generates a list of float arrays representing points along the curve,
	 * applying transformations and possibly window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @param windowed  Whether to scale the points to the window's dimensions.
	 * @return ArrayList of float[] representing points along the curve.
	 */
	public ArrayList<float[]> pointList(int numPoints, boolean windowed) {
		return this.pointList(numPoints, windowed, true);
	}

	/*
	 * PVECTOR ARRAY UTILITIES
	 */

	/**
	 * Generates an array of points along the curve.
	 *
	 * @param numPoints      The number of points to generate.
	 * @param windowed       Whether to scale the points to the window's dimensions.
	 * @param transformed    Whether to apply the curve's transformations to the points.
	 * @param samplingRateOp FloatOp to determine distribution of points across the curve
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(int numPoints, boolean windowed, boolean transformed, FloatOp samplingRateOp) {
		PVector[] points = new PVector[numPoints];
		for (int i = 0; i < numPoints; i++) {
			float x = (float) i / (numPoints - 1);
			if (samplingRateOp != null) {
				x = samplingRateOp.apply(x);
			}
			if (windowed) {
				points[i] = this.wVector(x, transformed);
			} else {
				points[i] = this.uVector(x, transformed);
			}
		}
		return points;
	}

	/**
	 * Generates an array of points along the curve.
	 *
	 * @param numPoints   The number of points to generate.
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(int numPoints, boolean windowed, boolean transformed) {
		return this.vectorArray(numPoints, windowed, transformed, null);
	}

	/**
	 * Generates an array of points along the curve, applying transformations but
	 * not window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(int numPoints) {
		return this.vectorArray(numPoints, false, true);
	}

	/**
	 * Generates an array of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param windowed Whether to scale the points to the window's               dimensions.
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(boolean windowed) {
		return this.vectorArray(this.resolution, windowed, true);
	}

	/**
	 * Generates an array of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @param windowed  Whether to scale the points to the window's dimensions.
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(int numPoints, boolean windowed) {
		return this.vectorArray(numPoints, windowed, true);
	}

	/**
	 * Generates an array of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return An array of PVectors representing points along the curve.
	 */
	public PVector[] vectorArray(boolean windowed, boolean transformed) {
		return this.vectorArray(this.resolution, windowed, transformed);
	}

	/*
	 * PVECTOR ARRAY LIST UTILITIES
	 */

	/**
	 * Generates a list of points along the curve.
	 *
	 * @param numPoints      The number of points to generate.
	 * @param windowed       Whether to scale the points to the window's dimensions.
	 * @param transformed    Whether to apply the curve's transformations to the points.
	 * @param samplingRateOp FloatOp to determine variable distribution of points along curve
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(int numPoints, boolean windowed, boolean transformed, FloatOp samplingRateOp) {
		PVector[] array = this.vectorArray(numPoints, windowed, transformed, samplingRateOp);
		return Utils.va2vl(array);
	}

	/**
	 * Generates a list of points along the curve.
	 *
	 * @param numPoints   The number of points to generate.
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(int numPoints, boolean windowed, boolean transformed) {
		return this.vectorList(numPoints, windowed, transformed, null);
	}

	/**
	 * Generates a list of points along the curve, applying transformations but not
	 * window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(int numPoints) {
		return this.vectorList(numPoints, false, true);
	}

	/**
	 * Generates a list of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param windowed Whether to scale the points to the window's               dimensions.
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(boolean windowed) {
		return this.vectorList(this.resolution, windowed, true);
	}

	/**
	 * Generates a list of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param numPoints The number of points to generate.
	 * @param windowed  Whether to scale the points to the window's dimensions.
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(int numPoints, boolean windowed) {
		return this.vectorList(numPoints, windowed, true);
	}

	/**
	 * Generates a list of points along the curve, applying transformations and
	 * possibly window scaling.
	 *
	 * @param windowed    Whether to scale the points to the window's dimensions.
	 * @param transformed Whether to apply the curve's transformations to the points.
	 * @return An ArrayList of PVectors representing points along the curve.
	 */
	public ArrayList<PVector> vectorList(boolean windowed, boolean transformed) {
		return this.vectorList(this.resolution, windowed, transformed);
	}

	/*
	 * PSHAPE UTILITIES
	 */

	/**
	 * Constructs a PShape object from the curve.
	 *
	 * @param close     Whether to close the shape.
	 * @param numPoints the num points
	 * @param windowed  Whether to scale the shape to the window's dimensions.
	 * @param visible   Whether to make the shape visible.
	 * @return A PShape object representing the curve.
	 */
	public PShape shape(boolean close, int numPoints, boolean windowed, boolean visible) {
		PVector[] points = this.vectorArray(numPoints, windowed);
		PShape s = this.parent.createShape();
		s.setVisible(visible);
		s.beginShape();
		for (PVector p : points) {
			s.vertex(p.x, p.y);
		}
		if (close) {
			s.endShape(PConstants.CLOSE);
		} else {
			s.endShape();
		}
		return s;
	}

	/**
	 * Constructs an invisible PShape object from the curve without closing or
	 * scaling it.
	 *
	 * @return A PShape object representing the curve.
	 */
	public PShape shape() {
		return this.shape(false, this.resolution, false, false);
	}

	/**
	 * Constructs an invisible PShape object from the curve with optional closing
	 * and no scaling.
	 *
	 * @param close Whether to close the shape.
	 * @return A PShape object representing the curve.
	 */
	public PShape shape(boolean close) {
		return this.shape(close, this.resolution, false, false);
	}

	/**
	 * Constructs an invisible PShape object from the curve with optional closing
	 * and scaling.
	 *
	 * @param close    Whether to close the shape.
	 * @param windowed Whether to scale the shape to the window's dimensions.
	 * @return A PShape object representing the curve.
	 */
	public PShape shape(boolean close, boolean windowed) {
		return this.shape(close, this.resolution, windowed, false);
	}

	/*
	 * PSHAPE VERTEX FLOAT[][] MATRIX UTILITIES
	 */

	/**
	 * Generates a 2D float array of vertices from the curve.
	 *
	 * @param close    Whether to close the shape.
	 * @param windowed Whether to scale the shape to the window's dimensions.
	 * @return A 2D array of floats representing the vertices of the curve.
	 */
	public float[][] vertexPointArray(boolean close, boolean windowed) {
		PShape s = this.shape(close, windowed);
		int vCount = s.getVertexCodeCount();
		float[][] points = new float[vCount][2];
		for (int i = 0; i < vCount; i++) {
			PVector p = s.getVertex(i);
			points[i] = p.array();
		}
		return points;
	}

	/**
	 * Generates a 2D float array of vertices from the curve without scaling it.
	 *
	 * @param close Whether to close the shape.
	 * @return An 2D array of floats representing the vertices of the curve.
	 */
	public float[][] vertexPointArray(boolean close) {
		return this.vertexPointArray(close, false);
	}

	/**
	 * Generates a 2D array of vertices from the curve without closing or scaling
	 * it.
	 *
	 * @return A 2D array of floats representing the vertices of the curve.
	 */
	public float[][] vertexPointArray() {
		return this.vertexPointArray(false, false);
	}

	/*
	 * PSHAPE VERTEX ARRAY UTILITIES
	 */

	/**
	 * Generates an array of vertices from the curve.
	 *
	 * @param close    Whether to close the shape.
	 * @param windowed Whether to scale the shape to the window's dimensions.
	 * @return An array of PVectors representing the vertices of the curve.
	 */
	public PVector[] vertexVectorArray(boolean close, boolean windowed) {
		PShape s = this.shape(close, windowed);
		int vCount = s.getVertexCodeCount();
		PVector[] pvs = new PVector[vCount];
		for (int i = 0; i < vCount; i++) {
			pvs[i] = s.getVertex(i);
		}
		return pvs;
	}

	/**
	 * Generates an array of vertices from the curve without scaling it.
	 *
	 * @param close Whether to close the shape.
	 * @return An array of PVectors representing the vertices of the curve.
	 */
	public PVector[] vertexVectorArray(boolean close) {
		return this.vertexVectorArray(close, false);
	}

	/**
	 * Generates an array of vertices from the curve without closing or scaling it.
	 *
	 * @return An array of PVectors representing the vertices of the curve.
	 */
	public PVector[] vertexVectorArray() {
		return this.vertexVectorArray(false, false);
	}

	/*
	 * PSHAPE VERTEX ARRAY LIST UTILITIES
	 */

	/**
	 * Generates a list of vertices from the curve.
	 *
	 * @param close    Whether to close the shape.
	 * @param windowed Whether to scale the shape to the window's dimensions.
	 * @return An ArrayList of PVectors representing the vertices of the curve.
	 */
	public ArrayList<PVector> vertexVectorList(boolean close, boolean windowed) {
		PVector[] array = this.vertexVectorArray(close, windowed);
		return new ArrayList<>(Arrays.asList(array));
	}

	/**
	 * Generates a list of vertices from the curve without scaling it.
	 *
	 * @param close Whether to close the shape.
	 * @return An ArrayList of PVectors representing the vertices of the curve.
	 */
	public ArrayList<PVector> vertexVectorList(boolean close) {
		return this.vertexVectorList(close, false);
	}

	/**
	 * Generates a list of vertices from the curve without closing or scaling it.
	 *
	 * @return An ArrayList of PVectors representing the vertices of the curve.
	 */
	public ArrayList<PVector> vertexVectorList() {
		return this.vertexVectorList(false, false);
	}

	/*
	 * VERTEX MATRIX LIST UTILITIES
	 */

	/**
	 * Generates a list of float arrays representing vertices from the curve.
	 *
	 * @param close    Whether to close the shape.
	 * @param windowed Whether to scale the shape to the window's dimensions.
	 * @return An ArrayList of float[] representing the vertices of the curve.
	 */
	public ArrayList<float[]> vertexPointList(boolean close, boolean windowed) {
		float[][] array = this.vertexPointArray(close, windowed);
		return new ArrayList<>(Arrays.asList(array));
	}

	/**
	 * Generates a list of float arrays representing vertices from the curve without
	 * scaling it.
	 *
	 * @param close Whether to close the shape.
	 * @return An ArrayList of float[] representing the vertices of the curve.
	 */
	public ArrayList<float[]> vertexPointList(boolean close) {
		return this.vertexPointList(close, false);
	}

	/**
	 * Generates a list of float arrays representing vertices from the curve without
	 * closing or scaling it.
	 *
	 * @return An ArrayList of float[] representing the vertices of the curve.
	 */
	public ArrayList<float[]> vertexPointList() {
		return this.vertexPointList(false, false);
	}

	/*
	 * MESH UTILITIES
	 */

	/**
	 * Voronoi voronoi.
	 *
	 * @param numPoints      the num points
	 * @param windowed       the windowed
	 * @param transformed    the transformed
	 * @param samplingRateOp FloatOp to determine variable distribution of points along the curve
	 * @return voronoi voronoi
	 */
	public Voronoi voronoi(int numPoints, boolean windowed, boolean transformed, FloatOp samplingRateOp) {
		PVector[] points = this.vectorArray(numPoints, windowed, transformed, samplingRateOp);
		return new Voronoi(points);
	}

	/**
	 * Voronoi voronoi.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return voronoi voronoi
	 */
	public Voronoi voronoi(int numPoints, boolean windowed, boolean transformed) {
		return this.voronoi(numPoints, windowed, transformed, null);
	}

	/**
	 * Voronoi edges float [ ] [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return float [ ] [ ]
	 */
	public float[][] voronoiEdges(int numPoints, boolean windowed, boolean transformed) {
		Voronoi v = this.voronoi(numPoints, windowed, transformed);
		return v.getEdges();
	}

	/**
	 * Voronoi edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param resolution  the resolution
	 * @return the edg [ ]
	 */
	public Edg[] voronoiEdgs(int numPoints, boolean windowed, boolean transformed, int resolution) {
		Voronoi v = this.voronoi(numPoints, windowed, transformed);
		return v.getEdgs(resolution);
	}

	/**
	 * Voronoi edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return the edg [ ]
	 */
	public Edg[] voronoiEdgs(int numPoints, boolean windowed, boolean transformed) {
		return this.voronoiEdgs(numPoints, windowed, transformed, this.resolution);
	}

	/**
	 * Voronoi regions m polygon [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return m polygon [ ]
	 */
	public MPolygon[] voronoiRegions(int numPoints, boolean windowed, boolean transformed) {
		Voronoi v = this.voronoi(numPoints, windowed, transformed);
		MPolygon[] regions = v.getRegions();
		if (windowed) {
			MPolygon[] clippedRegions = new MPolygon[regions.length];
			for (int i = 0; i < regions.length; i++) {
				PVector[] vertices = regions[i].getVertices();
				MPolygon clippedRegion = new MPolygon(vertices.length);
				for (PVector vec : vertices) {
					PVector clippedVec = Utils.clipped(
							vec,
							this.window.origin.x,
							this.window.origin.x + this.window.getWidth(),
							this.window.origin.y,
							this.window.origin.y + this.window.getHeight()
					);
					clippedRegion.add(clippedVec.x, clippedVec.y);
				}
				clippedRegions[i] = clippedRegion;
			}
			return clippedRegions;
		}
		return regions;
	}

	/**
	 * Delaunay delaunay.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return delaunay delaunay
	 */
	public Delaunay delaunay(int numPoints, boolean windowed, boolean transformed) {
		PVector[] points = this.vectorArray(numPoints, windowed, transformed);
		return new Delaunay(points);
	}

	/**
	 * Delaunay edges float [ ] [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return float [ ] [ ]
	 */
	public float[][] delaunayEdges(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getEdges();
	}

	/**
	 * Delaunay edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param resolution  the resolution
	 * @return the edg [ ]
	 */
	public Edg[] delaunayEdgs(int numPoints, boolean windowed, boolean transformed, int resolution) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getEdgs(resolution);
	}

	/**
	 * Delaunay edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return the edg [ ]
	 */
	public Edg[] delaunayEdgs(int numPoints, boolean windowed, boolean transformed) {
		return this.delaunayEdgs(numPoints, windowed, transformed, this.resolution);
	}

	/**
	 * Delaunay edge count int.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return int int
	 */
	public int delaunayEdgeCount(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.edgeCount();
	}

	/**
	 * Delaunay links int [ ] [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return int [ ] [ ]
	 */
	public int[][] delaunayLinks(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getLinks();
	}

	/**
	 * Delaunay get linked int [ ].
	 *
	 * @param index       the index
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return int [ ]
	 */
	public int[] delaunayGetLinked(int index, int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getLinked(index);
	}

	/**
	 * Hull hull.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return hull hull
	 */
	public Hull hull(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointArray(numPoints, windowed, transformed);
		return new Hull(points);
	}

	/**
	 * Hull region m polygon.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return m polygon
	 */
	public MPolygon hullRegion(int numPoints, boolean windowed, boolean transformed) {
		Hull h = this.hull(numPoints, windowed, transformed);
		return h.getRegion();
	}

	/**
	 * Hull extrema int [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return int [ ]
	 */
	public int[] hullExtrema(int numPoints, boolean windowed, boolean transformed) {
		Hull h = this.hull(numPoints, windowed, transformed);
		return h.getExtrema();
	}

	/*
	 * DRAWING UTILITIES
	 */

	/**
	 * Plots the Crv on the parent PApplet instance in the window. The function is
	 * evaluated across the width of the window.
	 *
	 * @param close     Whether to close the shape
	 * @param numPoints the num points
	 */
	public void draw(boolean close, int numPoints) {
		PShape shp = this.shape(close,  numPoints, true, true);
		this.parent.shape(shp, 0, 0);
	}

	/**
	 * Plots the Crv on the parent PApplet instance in the window. The function is
	 * evaluated across the width of the window and the shape is not close.
	 */
	public void draw() {
		this.draw(false, this.resolution);
	}

	/**
	 * Draw.
	 *
	 * @param drawer the drawer
	 */
	public void draw(CrvDrawer drawer) {
		drawer.draw(this);
	}

	/*
	 * IDEAS: Use the ArrayList<float[]> types to generate subsets of the float
	 * matrix to be drawn with the different algorithms Create an Edge Class and a
	 * Point Class that provide their own draw(drawer) method Create arrays of
	 * EdgeDrawers and PointDrawers and randomly or iteratively or geometrically use
	 * different drawers for different Edges/Points
	 */

	private void voronoiDrawRegions(MPolygon[] regions, int depth) {
		if (depth <= 0) {
			return;
		}
		for (MPolygon region : regions) {
			region.draw(this.parent);

			// Create a new set of points within the region
			// How might we do this? Random sampling? Regular sampling? Points along
			// float[][] points = region.getInnerCoords();
			
			PVector[] points = region.getPointsWithin(this.resolution);
			if (points.length > 3) {
				// Create a new Voronoi diagram for the points within the region
				Voronoi nestedVoronoi = new Voronoi(points);

				// Draw the nested Voronoi diagram
				MPolygon[] nestedRegions = nestedVoronoi.getRegions();
				// Call this function recursively to create additional levels of nesting
				this.voronoiDrawRegions(nestedRegions, depth - 1);
			}
		}
	}

	/**
	 * Voronoi draw nested.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param depth       the depth
	 */
	public void voronoiDrawNested(int numPoints, boolean windowed, boolean transformed, int depth) {
		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
		this.voronoiDrawRegions(regions, depth);
	}

	/**
	 * Voronoi draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void voronoiDraw(int numPoints, boolean windowed, boolean transformed) {
		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
		for (MPolygon region: regions) {
			region.draw(this.parent);
		}
	}

	/**
	 * Delaunay draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param drawer      the drawer
	 */
	public void delaunayDraw(int numPoints, boolean windowed, boolean transformed, EdgDrawer drawer) {
		Edg[] edgs = this.delaunayEdgs(numPoints, windowed, transformed);
		this.drawEdgs(edgs, drawer);
	}

	/**
	 * Delaunay draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void delaunayDraw(int numPoints, boolean windowed, boolean transformed) {
		this.delaunayDraw(numPoints, windowed, transformed, null);
	}

	/**
	 * Hull draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void hullDraw(int numPoints, boolean windowed, boolean transformed) {
		MPolygon hull = this.hullRegion(numPoints, windowed, transformed);
		hull.draw(this.parent);
	}

	/**
	 * Deloronoi draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param drawer      the drawer
	 * @param resolution  the resolution
	 */
	public void deloronoiDraw(int numPoints, boolean windowed, boolean transformed, EdgDrawer drawer, int resolution) {
		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
		Delaunay[] delaunays = new Delaunay[regions.length];
		for (int i = 0; i < regions.length; i++) {
			MPolygon region = regions[i];
			delaunays[i] = new Delaunay(region.getVertices());
		}
		for (Delaunay d : delaunays) {
			this.drawEdgs(d.getEdgs(resolution), drawer);
		}
	}

	/**
	 * Deloronoi draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param drawer      the drawer
	 */
	public void deloronoiDraw(int numPoints, boolean windowed, boolean transformed, EdgDrawer drawer) {
		this.deloronoiDraw(numPoints, windowed, transformed, drawer, this.resolution);
	}

	/**
	 * Deloronoi draw.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void deloronoiDraw(int numPoints, boolean windowed, boolean transformed) {
		this.deloronoiDraw(numPoints, windowed, transformed, null);
	}

	/**
	 * Iter draw.
	 *
	 * @param spacing     the spacing
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void iterDraw(float spacing, int numPoints, boolean windowed, boolean transformed) {
		this.parent.noFill();
		float oldYOrigin = this.origin.copy().y;
		this.origin.y = -1;
		while (this.origin.y < 1) {
			this.draw(false, numPoints);
			this.origin.y += spacing;
		}
		this.origin.y = oldYOrigin;
	}


	/**
	 * Draw vectors.
	 *
	 * @param numPoints      the num points
	 * @param windowed       the windowed
	 * @param transformed    the transformed
	 * @param drawer         the drawer
	 * @param samplingRateOp the sampling rate op
	 */
	public void drawVectors(
			int numPoints,
			boolean windowed,
			boolean transformed,
			VctrDrawer drawer,
			FloatOp samplingRateOp
	) {
		PVector[] vectors = this.vectorArray(numPoints, windowed, transformed, samplingRateOp);
		this.drawVectorArray(vectors, drawer);
	}

	/**
	 * Draw vectors.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param drawer      the drawer
	 */
	public void drawVectors(int numPoints, boolean windowed, boolean transformed, VctrDrawer drawer) {
		this.drawVectors(numPoints, windowed, transformed, drawer, null);
	}

	/**
	 * Draw vectors.
	 *
	 * @param vectors the vectors
	 * @param drawer  the drawer
	 */
// public void drawPoints(float[][] points, PShape shape, VctrDrawer drawer)
	// that calls drawer.draw(point, shape)???
	// public void drawPoints(float[][] points, PGraphics window, VctrDrawer drawer)
	// that calls drawer.draw(point, window)???
	// window is a Crv.window, and we set its origin to point
	public void drawVectorArray(PVector[] vectors, VctrDrawer drawer) {
		this.parent.push();
		for (PVector point : vectors) {
			if (drawer != null) {
				drawer.draw(point);
			} else {
				this.parent.point(point.x, point.y);
			}
		}
		this.parent.pop();
	}

	/**
	 * Draw vectors.
	 *
	 * @param vectors the vectors
	 */
	public void drawVectorArray(PVector[] vectors) {
		this.drawVectorArray(vectors, null);
	}

	/**
	 * Draw crv on edg.
	 *
	 * @param edg    the edg
	 * @param drawer the drawer
	 */
	public void drawCrvOnEdg(Edg edg, EdgDrawer drawer) {
		PVector[] crvPoints = edg.getCrvPoints(this);
		for (int i = 0; i < crvPoints.length - 1; i++) {
			PVector source = crvPoints[i];
			PVector target = crvPoints[i + 1];
			if (drawer != null) {
				Edg e = new Edg(source, target, edg.resolution);
				drawer.draw(e);
			} else {
				this.parent.line(source.x, source.y, target.x, target.y);
			}
		}
	}

	/**
	 * Draw vctrs on edg.
	 *
	 * @param edg    the edg
	 * @param drawer the drawer
	 */
	public void drawVctrsOnEdg(Edg edg, VctrDrawer drawer) {
		PVector[] crvPoints = edg.getCrvPoints(this);
		this.drawVectorArray(crvPoints, drawer);
	}

	/**
	 * Draw edges.
	 *
	 * @param edges  the edges
	 * @param drawer the drawer
	 */
	public void drawEdges(float[][] edges, EdgeDrawer drawer) {
		for (float[] edge : edges) {
			if (drawer != null) {
				drawer.draw(edge);
			} else {
				this.parent.line(edge[0], edge[1], edge[2], edge[3]);
			}
		}
	}

	/**
	 * Draw edgs.
	 *
	 * @param edgs   the edgs
	 * @param drawer the drawer
	 */
	public void drawEdgs(Edg[] edgs, EdgDrawer drawer) {
		for (Edg edge : edgs) {
			if (drawer != null) {
				drawer.draw(edge);
			} else {
				this.parent.line(edge.source.x, edge.source.y, edge.target.x, edge.target.y);
			}
		}
	}

	/**
	 * Draw edgs.
	 *
	 * @param edgs the edgs
	 */
	public void drawEdgs(Edg[] edgs) {
		this.drawEdgs(edgs, null);
	}

	/**
	 * Draw edges.
	 *
	 * @param edges the edges
	 */
	public void drawEdges(float[][] edges) {
		this.drawEdges(edges, null);
	}

	/**
	 * Draw web edges.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param drawer      the drawer
	 */
	public void drawWebEdges(int numPoints, boolean windowed, boolean transformed, EdgeDrawer drawer) {
		float[][] edges = this.getWebEdges(numPoints, windowed, transformed);
		this.drawEdges(edges, drawer);
	}

	/**
	 * Draw web edges.
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 */
	public void drawWebEdges(int numPoints, boolean windowed, boolean transformed) {
		this.drawWebEdges(numPoints, windowed, transformed, null);
	}

	/**
	 * Get web edges float [ ] [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return the float [ ] [ ]
	 */
	public float[][] getWebEdges(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointArray(numPoints, windowed, transformed);
		int size = points.length * (points.length - 1) / 2;
		float[][] edges = new float[size][4];
		int edgesIdx = 0;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < i; j++) {
				float[] edge = new float[4];
				edge[0] = points[i][0];
				edge[1] = points[i][1];
				edge[2] = points[j][0];
				edge[3] = points[j][1];
				edges[edgesIdx] = edge;
				edgesIdx++;
			}
		}
		return edges;
	}

	/**
	 * Get web edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @param resolution  the resolution
	 * @return the edg [ ]
	 */
	public Edg[] getWebEdgs(int numPoints, boolean windowed, boolean transformed, int resolution) {
		float[][] edges = this.getWebEdges(numPoints, windowed, transformed);
		Edg[] edgs = new Edg[edges.length];
		for (int i = 0; i < edges.length; i++) {
			edgs[i] = new Edg(edges[i], resolution);
		}
		return edgs;
	}

	/**
	 * Get web edgs edg [ ].
	 *
	 * @param numPoints   the num points
	 * @param windowed    the windowed
	 * @param transformed the transformed
	 * @return the edg [ ]
	 */
	public Edg[] getWebEdgs(int numPoints, boolean windowed, boolean transformed) {
		return this.getWebEdgs(numPoints, windowed, transformed, this.resolution);
	}

}
