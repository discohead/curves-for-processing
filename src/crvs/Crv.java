package crvs;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import processing.data.FloatList;

/**
 * The Crv class represents a curve in a 2D space. This curve is defined by a variety of parameters
 * such as amplitude, rate, phase and bias, which can be either constant values or other curves. 
 * The class uses a functional approach by using a FloatOp interface for processing the curve.
 * 
 * Each Crv instance is bound to a PApplet parent and optionally a separate PGraphics window for rendering.
 * The curve's points are calculated based on the provided FloatOp operation. Transformations like translation, 
 * scaling and rotation can be applied to the curve. The curve also supports different bounding
 * behaviors (clipping, wrapping, and folding).
 *
 * Crv is primarily designed for creating complex and evolving shapes in a Processing environment.
 */
public class Crv {
	
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
	 * The resolution of the curve, i.e. the number of points.
	 */
	public int resolution;
	
	/**
	 * The color to use when drawing
	 */
	public int color;
	
	/**
	 * If true, the area enclosed by the curve is filled with color.
	 */
	public boolean fill;

	/**
	 * The PGraphics window in which the curve is drawn.
	 */
	public PGraphics window;
	
	/**
	 * The original position of the curve.
	 */
	public PVector origin;
	
	/**
	 * The current translation of the curve from its origin.
	 */
	public PVector translation;
	
	/**
	 * The scale of the curve in x and y dimensions.
	 */
	public PVector scale;
	
	/**
	 * The rotation of the curve in degrees.
	 */
	public float rotation;

	/**
	 * The Bounding enum defines the different types of boundary behaviors a Crv object can have when
	 * its points exceed the limits of the rendering window. The four types are:
	 * 
	 * <ul>
	 *     <li>NONE: The points of the curve are not constrained and can exceed the window's limits.</li>
	 *     <li>CLIPPING: The points of the curve exceeding the window's limits are cut off, resulting in a hard edge at the boundary.</li>
	 *     <li>WRAPPING: The points of the curve that exceed the window's limits wrap around to the opposite edge of the window.</li>
	 *     <li>FOLDING: The points of the curve that exceed the window's limits are reflected back into the window, creating a folding effect.</li>
	 * </ul>
	 */
	public enum Bounding {
	    /**
	     * No boundary constraint. The points of the curve can exceed the window's limits.
	     */
	    NONE,

	    /**
	     * Clipping boundary constraint. The points of the curve exceeding the window's limits are cut off.
	     */
	    CLIPPING,

	    /**
	     * Wrapping boundary constraint. The points of the curve that exceed the window's limits wrap around to the opposite edge of the window.
	     */
	    WRAPPING,

	    /**
	     * Folding boundary constraint. The points of the curve that exceed the window's limits are reflected back into the window.
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
	
	public enum Component {
		X,
		Y,
	};
    
    /*
     * CONSTRUCTORS
     */
    
	/**
	 * Primary constructor. Initializes a new instance of the Crv class.
	 *
	 * @param parent The parent PApplet.
	 * @param window The PGraphics window in which the curve will be drawn.
	 * @param op The FloatOp function to be applied to the curve, defaults to linear ramp.
	 */
	public Crv(PApplet parent, PGraphics window, FloatOp op) {
		this.parent = parent;
		if (window == null) {
			this.window = parent.g;
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
	}
	
	/**
	 * Constructor that only takes a parent PApplet, and uses default values for other parameters.
	 *
	 * @param parent The parent PApplet.
	 */
	public Crv(PApplet parent) {
		this(parent, null, null);
	}
	
	/**
	 * Constructor that takes a parent PApplet and a PGraphics window, uses default op.
	 *
	 * @param parent The parent PApplet.
	 */
	public Crv(PApplet parent, PGraphics window) {
		this(parent, window, null);
	}
	
	/**
	 * Constructor that takes a parent PApplet and a FloatOp, and uses default values for other parameters.
	 *
	 * @param parent The parent PApplet.
	 * @param op The FloatOp function to be applied to the curve.
	 */
	public Crv(PApplet parent, FloatOp op) {
		this(parent, null, op);
	}
	
	/**
	 * Constructor that takes a parent PApplet and four Crv objects for amplitude, rate, phase, and bias.
	 *
	 * @param parent The parent PApplet.
	 * @param amp The Crv object representing the amplitude.
	 * @param rate The Crv object representing the rate.
	 * @param phase The Crv object representing the phase.
	 * @param bias The Crv object representing the bias.
	 */
	public Crv(PApplet parent, Crv amp, Crv rate, Crv phase, Crv bias) {
		this(parent);
		this.amp = amp;
		this.rate = rate;
		this.phase = phase;
		this.bias = bias;
	}
	
	/**
	 * Constructor that takes a parent PApplet and four float offsets for amplitude, rate, phase, and bias.
	 *
	 * @param parent The parent PApplet.
	 * @param ampOffset The amplitude offset as a float.
	 * @param rateOffset The rate offset as a float.
	 * @param phaseOffset The phase offset as a float.
	 * @param biasOffset The bias offset as a float.
	 */
	public Crv(PApplet parent, float ampOffset, float rateOffset, float phaseOffset, float biasOffset) {
		this(parent);
		this.ampOffset = ampOffset;
		this.rateOffset = rateOffset;
		this.phaseOffset = phaseOffset;
		this.biasOffset = biasOffset;
	}
	
	/**
	 * Constructor that takes a parent PApplet, four Crv objects for amplitude, rate, phase, and bias, and four float offsets for each.
	 *
	 * @param parent The parent PApplet.
	 * @param amp The Crv object representing the amplitude.
	 * @param rate The Crv object representing the rate.
	 * @param phase The Crv object representing the phase.
	 * @param bias The Crv object representing the bias.
	 * @param ampOffset The amplitude offset as a float.
	 * @param rateOffset The rate offset as a float.
	 * @param phaseOffset The phase offset as a float.
	 * @param biasOffset The bias offset as a float.
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

	/**
	 * Applies the curve function to the given position.
	 *
	 * @param pos The position in the curve, a value between 0 and 1.
	 * @return The output of the curve function at the given position.
	 */
	protected float calculate(float pos) {
		return this.op.apply(pos);
	};

	/**
	 * Applies amplitude and bias transformations to the given value at the given position.
	 *
	 * @param value The value to transform.
	 * @param pos The position in the curve, a value between 0 and 1.
	 * @return The transformed value.
	 */
	protected float ampBias(float value, float pos) {
		if (this.amp != null) {
			value *= this.amp.yAt(pos);
		}
		if (this.bias != null) {
			value += this.bias.yAt(pos);
		}
		return value * this.ampOffset + this.biasOffset;
	}
	
	/**
	 * Calculates a position value based on input rate and phase.
	 * 
	 * @param pos   Original position.
	 * @param rate  Rate operator to adjust the position.
	 * @param phase Phase operator to adjust the position.
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
     * Evaluates the curve at the given position for the specified Component.
     * 
     * The position is first transformed by the curve's rate and phase properties.
     * The curve function is then evaluated at this position.
     * The result is then converted to bipolar form (-1 to 1),
     * amplitude and bias are applied,
     * and finally the result is rectified back to unipolar form (0 to 1).
     * 
     * @param pos The original position, a value between 0 and 1.
     * @param component Component.X or Component.Y
     * @return The value of the curve at the transformed position, a value between 0 and 1.
     */
	public float componentAt(Component component, float pos) {
		float modPos = this.calcPos(pos);
		float value;
		switch(component) {
		case X:
			value = pos;
			break;
		case Y:
		default:
			value = this.calculate(modPos);
			break;
		}
		value = this.bipolarize(value);
		value = this.ampBias(value, modPos);
		return this.rectify(value);
	}
	
    /**
     * Evaluates the curve at the given position for the Y Component.
     * 
     * The position is first transformed by the curve's rate and phase properties.
     * The curve function is then evaluated at this position.
     * The result is then converted to bipolar form (-1 to 1),
     * amplitude and bias are applied,
     * and finally the result is rectified back to unipolar form (0 to 1).
     * 
     * @param pos The original position, a value between 0 and 1.
     * @return The value of the curve at the transformed position, a value between 0 and 1.
     */
	public float yAt(float pos) {
		return this.componentAt(Component.Y, pos);
	}
	
	public float xAt(float pos) {
		return this.componentAt(Component.X, pos);
	}
	
	/**
	 * Wraps a given value into a specified range [min, max).
	 *
	 * @param value The value to wrap.
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
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
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
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
     * 
     * This method samples the curve at uniform intervals and returns
     * an array of the resulting y-values. The number of samples is
     * specified by the numSamples parameter.
     * 
     * @param numSamples The number of samples to evaluate.
     * @param component Component.X or Component.Y
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
	
	public float[] floatArray(int numSamples) {
		return this.floatArray(numSamples, Component.Y);
	}

    /**
     * Returns a float array of y-values evaluated from the curve.
     * 
     * This method samples the curve at uniform intervals and returns
     * an array of the resulting y-values. The number of samples is
     * determined by the resolution property of the curve.
     * 
     * @return An array of y-values evaluated from the curve.
     */
	public float[] floatArray() {
		return this.floatArray(this.resolution);
	}
	
    /**
     * Returns a FloatList of values evaluated from the curve for the specified Component.
     * 
     * This method samples the curve at uniform intervals and returns
     * a FloatList of the resulting component values. The number of samples is
     * specified by the numSamples parameter.
     * 
     * @param numSamples The number of samples to evaluate.
     * @param component Component.X or Component.Y
     * @return A FloatList of component values evaluated from the curve.
     */
	public FloatList floatList(Component component, int numSamples) {
		float[] array = this.floatArray(numSamples, component);
		return new FloatList(array);
	}

    /**
     * Returns a FloatList of y-values evaluated from the curve.
     * 
     * This method samples the curve at uniform intervals and returns
     * a FloatList of the resulting y-values. The number of samples is
     * specified by the numSamples parameter.
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
     * 
     * This method samples the curve at uniform intervals and returns
     * a FloatList of the resulting y-values. The number of samples is
     * determined by the resolution property of the curve.
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
     * @param pos The x-position at which to sample the curve.
     * @param transformed Whether or not to transform the point.
     * @return A PVector representing the point on the curve at the given x-position.
     */
	public PVector uPoint(float pos, boolean transformed) {
		float x = this.componentAt(Component.X, pos);
		float y = this.componentAt(Component.Y, pos);
		PVector p = new PVector(x, y);
		p = this.jitter(p);
		if (transformed) {
			p = this.transform(p);
		}
		return this.bounded(p);
	}

    /**
     * Gets a PVector representing a point on the curve at a given x-position, applying transformations.
     * 
     * @param pos The x-position at which to sample the curve.
     * @return A PVector representing the point on the curve at the given x-position.
     */
	public PVector uPoint(float pos) {
		return this.uPoint(pos, true);
	}

    /**
     * Gets a PVector representing a point on the curve at a given x-position, applying transformations and window scaling.
     * 
     * @param pos The x-position at which to sample the curve.
     * @return A PVector representing the point on the curve at the given x-position.
     */
	public PVector wPoint(float pos) {
		PVector p = this.uPoint(pos);
		return this.windowed(p);
	}
	
    /**
     * Transforms the given PVector according to the curve's scale, rotation, and translation.
     * 
     * @param p The PVector to transform.
     * @return The transformed PVector.
     */
	public PVector transform(PVector p) {
		p.sub(Crv.uCenter);
		p.rotate(PApplet.radians(this.rotation));
		p.add(Crv.uCenter);
		p.x *= this.scale.x;
		p.y *= this.scale.y;
		p.add(this.translation);
		return p;
	}

    /**
     * Transforms an array of PVectors according to the curve's scale, rotation, and translation.
     * 
     * @param pvs The array of PVectors to transform.
     * @return The array of transformed PVectors.
     */
	public PVector[] transform(PVector[] pvs) {
		for (int i = 0; i < pvs.length; i++) {
			PVector p = pvs[i];
			p = this.transform(p);
		}
		return pvs;
	}

    /**
     * Re-scales a PVector to fit within the window's dimensions.
     * 
     * @param p The PVector to re-scale.
     * @return The rescaled PVector.
     */
	public PVector windowed(PVector p) {
		p.x *= this.window.width;
		p.y *= this.window.height;
		return p;
	}
	
    /**
     * Adjusts a PVector based on the curve's bounding mode (clipping, wrapping, folding, or none).
     * 
     * @param p The PVector to adjust.
     * @return The adjusted PVector.
     */
	public PVector bounded(PVector p) {
		switch (this.bounding) {
		    case CLIPPING:
		        return clipped(p);
		    case WRAPPING:
		        return wrapped(p);
		    case FOLDING:
		        return folded(p);
		    default:
		    case NONE:
		        return p;
		}
	}
	
    /**
     * Clamps a PVector's components to the range [0, 1].
     * 
     * @param p The PVector to clip.
     * @return The clipped PVector.
     */
	public PVector clipped(PVector p) {
	    p.x = Math.max(0f, Math.min(1f, p.x));
	    p.y = Math.max(0f, Math.min(1f, p.y));
	    return p;
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
     * 
     * The amount of jitter is determined by the jitterScale property of the curve.
     * The probability of applying jitter is determined by the jitterProbability property of the curve.
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
	 * FLOAT[][] MATRIX UTILITIES
	 */
	
    /**
     * Generates a 2D float array of points along the curve.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return A 2D array of floats representing points along the curve.
     */
	public float[][] pointsMatrix(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = new float[numPoints][2];
		for (int i = 0; i < numPoints; i++) {
			float x = (float) i / numPoints;
			PVector p = this.uPoint(x, transformed);
			if (windowed) {
				p = this.windowed(p);
			}
			points[i] = p.array();
		}
		return points;
	}
	
    /**
     * Generates a 2D array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return A 2D array of floats representing points along the curve.
     */
	public float[][] pointsMatrix(boolean windowed) {
		return this.pointsMatrix(this.resolution, windowed, true);
	}
	
    /**
     * Generates a 2D float array of points along the curve, applying transformations but not window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @return A 2D array of floats representing points along the curve.
     */
	public float[][] pointsMatrix(int numPoints) {
		return this.pointsMatrix(numPoints, false, true);
	}
	
    /**
     * Generates a 2D float array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return A 2D array of floats representing points along the curve.
     */
	public float[][] pointsMatrix(int numPoints, boolean windowed) {
		return this.pointsMatrix(numPoints, windowed, true);
	}
	
    /**
     * Generates a 2D float array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return A 2D array of floats representing points along the curve.
     */
	public float[][] pointsMatrix(boolean windowed, boolean transformed) {
		return this.pointsMatrix(this.resolution, windowed, transformed);
	}
	
	/*
	 * FLOAT[][] MATRIX LIST UTILITIES
	 */

    /**
     * Generates a list of float arrays representing points along the curve.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return An ArrayList of float[] representing points along the curve.
     */
	public ArrayList<float[]> pointsMatrixList(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointsMatrix(numPoints, windowed, transformed);
		return new ArrayList<float[]>(Arrays.asList(points));
	}
	
    /**
     * Generates a list of float arrays representing points along the curve, applying transformations but not window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @return An ArrayList of float[] representing points along the curve.
     */
	public ArrayList<float[]> pointsMatrixList(int numPoints) {
		return this.pointsMatrixList(numPoints, false, true);
	}

    /**
     * Generates a list of float arrays representing points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @returnAn ArrayList of float[] representing points along the curve.
     */
	public ArrayList<float[]> pointsMatrixList(boolean windowed) {
		return this.pointsMatrixList(this.resolution, windowed, true);
	}

    /**
     * Generates a list of float arrays representing points along the curve, applying transformations and possibly window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return ArrayList of float[] representing points along the curve.
     */
	public ArrayList<float[]> pointsMatrixList(int numPoints, boolean windowed) {
		return this.pointsMatrixList(numPoints, windowed, true);
	}


	/*
	 * PVECTOR ARRAY UTILITIES
	 */
	
    /**
     * Generates an array of points along the curve.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return An array of PVectors representing points along the curve.
     */
	public PVector[] pointsArray(int numPoints, boolean windowed, boolean transformed) {
		PVector[] points = new PVector[numPoints];
		for (int i = 0; i < numPoints; i++) {
			float x = (float) i / numPoints;
			PVector p = this.uPoint(x, transformed);
			if (windowed) {
				p = this.windowed(p);
			}
			points[i] = p;
		}
		return points;
	}

    /**
     * Generates an array of points along the curve, applying transformations but not window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @return An array of PVectors representing points along the curve.
     */
	public PVector[] pointsArray(int numPoints) {
		return this.pointsArray(numPoints, false, true);
	}

    /**
     * Generates an array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return An array of PVectors representing points along the curve.
     */
	public PVector[] pointsArray(boolean windowed) {
		return this.pointsArray(this.resolution, windowed, true);
	}

    /**
     * Generates an array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return An array of PVectors representing points along the curve.
     */
	public PVector[] pointsArray(int numPoints, boolean windowed) {
		return this.pointsArray(numPoints, windowed, true);
	}

    /**
     * Generates an array of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return An array of PVectors representing points along the curve.
     */
	public PVector[] pointsArray(boolean windowed, boolean transformed) {
		return this.pointsArray(this.resolution, windowed, transformed);
	}
	
	/*
	 * PVECTOR ARRAY LIST UTILITIES
	 */

    /**
     * Generates a list of points along the curve.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return An ArrayList of PVectors representing points along the curve.
     */
	public ArrayList<PVector> pointsList(int numPoints, boolean windowed, boolean transformed) {
		PVector[] array = this.pointsArray(numPoints, windowed, transformed);
		return new ArrayList<PVector>(Arrays.asList(array));
	}
	
    /**
     * Generates a list of points along the curve, applying transformations but not window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @return An ArrayList of PVectors representing points along the curve.
     */
	public ArrayList<PVector> pointsList(int numPoints) {
		return this.pointsList(numPoints, false, true);
	}

    /**
     * Generates a list of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return An ArrayList of PVectors representing points along the curve.
     */
	public ArrayList<PVector> pointsList(boolean windowed) {
		return this.pointsList(this.resolution, windowed, true);
	}

    /**
     * Generates a list of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param numPoints The number of points to generate.
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @return An ArrayList of PVectors representing points along the curve.
     */
	public ArrayList<PVector> pointsList(int numPoints, boolean windowed) {
		return this.pointsList(numPoints, windowed, true);
	}

    /**
     * Generates a list of points along the curve, applying transformations and possibly window scaling.
     * 
     * @param windowed Whether or not to scale the points to the window's dimensions.
     * @param transformed Whether or not to apply the curve's transformations to the points.
     * @return An ArrayList of PVectors representing points along the curve.
     */
	public ArrayList<PVector> pointsList(boolean windowed, boolean transformed) {
		return this.pointsList(this.resolution, windowed, transformed);
	}
	
	/*
	 * PSHAPE UTILITIES
	 */
	
    /**
     * Constructs a PShape object from the curve.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @param visible Whether to make the shape visible.
     * @return A PShape object representing the curve.
     */
	public PShape shape(boolean close, boolean windowed, boolean visible) {
		PVector[] points = this.pointsArray(windowed);
		PShape s = this.window.createShape();
		s.setVisible(visible);
		s.beginShape();
		for (int i = 0; i < points.length; i++) {
			PVector p = points[i];
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
     * Constructs an invisible PShape object from the curve without closing or scaling it.
     * 
     * @return A PShape object representing the curve.
     */
	public PShape shape() {
		return this.shape(false, false, false);
	}
	
    /**
     * Constructs an invisible PShape object from the curve with optional closing and no scaling.
     * 
     * @param close Whether to close the shape.
     * @return A PShape object representing the curve.
     */
	public PShape shape(boolean close) {
		return this.shape(close, false, false);
	}
	
    /**
     * Constructs an invisible PShape object from the curve with optional closing and scaling.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @return A PShape object representing the curve.
     */
	public PShape shape(boolean close, boolean windowed) {
		return this.shape(close, windowed, false);
	}
	
	/*
	 * PSHAPE VERTEX FLOAT[][] MATRIX UTILITIES
	 */
	
    /**
     * Generates a 2D float array of vertices from the curve.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @return A 2D array of floats representing the vertices of the curve.
     */
	public float[][] vertexMatrix(boolean close, boolean windowed) {
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
	public float[][] vertexMatrix(boolean close) {
		return this.vertexMatrix(close, false);
	}
	
    /**
     * Generates a 2D array of vertices from the curve without closing or scaling it.
     * 
     * @return A 2D array of floats representing the vertices of the curve.
     */
	public float[][] vertexMatrix() {
		return this.vertexMatrix(false, false);
	}
	
	/*
	 * PSHAPE VERTEX ARRAY UTILITIES
	 */
	
    /**
     * Generates an array of vertices from the curve.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @return An array of PVectors representing the vertices of the curve.
     */
	public PVector[] vertexArray(boolean close, boolean windowed) {
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
	public PVector[] vertexArray(boolean close) {
		return this.vertexArray(close, false);
	}
	
    /**
     * Generates an array of vertices from the curve without closing or scaling it.
     * 
     * @return An array of PVectors representing the vertices of the curve.
     */
	public PVector[] vertexArray() {
		return this.vertexArray(false, false);
	}
	
	/*
	 * PSHAPE VERTEX ARRAY LIST UTILITIES
	 */
	
    /**
     * Generates a list of vertices from the curve.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @return An ArrayList of PVectors representing the vertices of the curve.
     */
	public ArrayList<PVector> vertexList(boolean close, boolean windowed) {
		PVector[] array = this.vertexArray(close, windowed);
		return new ArrayList<PVector>(Arrays.asList(array));
	}
	
    /**
     * Generates a list of vertices from the curve without scaling it.
     * 
     * @param close Whether to close the shape.
     * @return An ArrayList of PVectors representing the vertices of the curve.
     */
	public ArrayList<PVector> vertexList(boolean close) {
		return this.vertexList(close, false);
	}
	
    /**
     * Generates a list of vertices from the curve without closing or scaling it.
     * 
     * @return An ArrayList of PVectors representing the vertices of the curve.
     */
	public ArrayList<PVector> vertexList() {
		return this.vertexList(false, false);
	}
	
	/*
	 * VERTEX MATRIX LIST UTILITIES
	 */
	
    /**
     * Generates a list of float arrays representing vertices from the curve.
     * 
     * @param close Whether to close the shape.
     * @param windowed Whether to scale the shape to the window's dimensions.
     * @return An ArrayList of float[] representing the vertices of the curve.
     */
	public ArrayList<float[]> vertexMatrixList(boolean close, boolean windowed) {
		float[][] array = this.vertexMatrix(close, windowed);
		return new ArrayList<float[]>(Arrays.asList(array));
	}
	
    /**
     * Generates a list of float arrays representing vertices from the curve without scaling it.
     * 
     * @param close Whether to close the shape.
     * @return An ArrayList of float[] representing the vertices of the curve.
     */
	public ArrayList<float[]> vertexMatrixList(boolean close) {
		return this.vertexMatrixList(close, false);
	}
	
    /**
     * Generates a list of float arrays representing vertices from the curve without closing or scaling it.
     * 
     * @return An ArrayList of float[] representing the vertices of the curve.
     */
	public ArrayList<float[]> vertexMatrixList() {
		return this.vertexMatrixList(false, false);
	}
	
	/*
	 * MESH UTILITIES
	 */

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public Voronoi voronoi(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointsMatrix(numPoints, windowed, transformed);
		PApplet.println(points.length);
		PApplet.printArray(points);
		return new Voronoi(points);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public float[][] voronoiEdges(int numPoints, boolean windowed, boolean transformed) {
		Voronoi v = this.voronoi(numPoints, windowed, transformed);
		return v.getEdges();
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public MPolygon[] voronoiRegions(int numPoints, boolean windowed, boolean transformed) {
		Voronoi v = this.voronoi(numPoints, windowed, transformed);
		return v.getRegions();
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public Delaunay delaunay(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointsMatrix(numPoints, windowed, transformed);
		return new Delaunay(points);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public float[][] delaunayEdges(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getEdges();
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public int delaunayEdgeCount(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.edgeCount();
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public int[][] delaunayLinks(int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getLinks();
	}

	/**
	 * @param index
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public int[] delaunayGetLinked(int index, int numPoints, boolean windowed, boolean transformed) {
		Delaunay d = this.delaunay(numPoints, windowed, transformed);
		return d.getLinked(index);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public Hull hull(int numPoints, boolean windowed, boolean transformed) {
		float[][] points = this.pointsMatrix(numPoints, windowed, transformed);
		return new Hull(points);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public MPolygon hullRegion(int numPoints, boolean windowed, boolean transformed) {
		Hull h = this.hull(numPoints, windowed, transformed);
		return h.getRegion();
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @return
	 */
	public int[] hullExtrema(int numPoints, boolean windowed, boolean transformed) {
		Hull h = this.hull(numPoints, windowed, transformed);
		return h.getExtrema();
	}
	
	/*
	 * DRAWING UTILITIES
	 */

	/**
	 * Plots the Crv on the parent PApplet instance in the window.
	 * The function is evaluated across the width of the window.
	 * 
	 * @param close Whether to close the shape
	 */
	public void draw(boolean close) {
		this.parent.push();
		this.window.beginDraw();
		if (fill) {
			this.window.noStroke();
			this.window.fill(this.color);
		} else {
			this.window.noFill();
			this.window.stroke(this.color);
		}
		PShape shp = this.shape(close, true, true);
		this.window.shape(shp, 0, 0);
		this.window.endDraw();
		if (this.window != this.parent.g) {
			this.parent.image(this.window, this.origin.x, this.origin.y);
		}
		this.parent.pop();
	}
	
	/**
	 * Plots the Crv on the parent PApplet instance in the window.
	 * The function is evaluated across the width of the window and the shape is not close.
	 */
	public void draw() {
		this.draw(false);
	}
	
	/*
	 * IDEAS: Use the ArrayList<float[]> types to generate subsets of the float matrix to be drawn with the different algorithms
	 * Create an Edge Class and a Point Class that provide their own draw(drawer) method
	 * Create arrays of EdgeDrawers and PointDrawers and randomly or iteratively or geometrically use different drawers for different Edges/Points
	 */
	
//	public void voronoiDraw(int numPoints, boolean windowed, boolean transformed, int depth) {
//		if(depth <= 0) {
//			return;
//		}
//		
//		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
//		for(MPolygon region : regions) {
//			region.draw(this.window);
//			
//			// Create a new set of points within the region
//			// How might we do this? Random sampling? Regular sampling? Points along 
//			// float[][] points = region.getInnerCoords();
//			
//			Delaunay d = new Delaunay(region.getCoords());
//			float points;
//			for (float[] edge : d.getEdges()) {
//				// drawEdge
//				// get points along edge
//			}
//			
//			// Create a new Voronoi diagram for the points within the region
//			Voronoi nestedVoronoi = new Voronoi(points);
//			
//			// Draw the nested Voronoi diagram
//			MPolygon[] nestedRegions = nestedVoronoi.getRegions();
//			for(MPolygon nestedRegion : nestedRegions) {
//				nestedRegion.draw(this.window);
//			}
//			
//			// Call this function recursively to create additional levels of nesting
//			voronoiDraw(points.length, windowed, transformed, depth - 1);
//		}
//	}


	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 */
	public void voronoiDraw(int numPoints, boolean windowed, boolean transformed) {
		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
		for(MPolygon region : regions) {
			region.draw(this.window);
		}
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @param drawer
	 */
	public void delaunayDraw(int numPoints, boolean windowed, boolean transformed, EdgeDrawer drawer) {
		float[][] edges = this.delaunayEdges(numPoints, windowed, transformed);
		this.drawEdges(edges, drawer);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 */
	public void delaunayDraw(int numPoints, boolean windowed, boolean transformed) {
		this.delaunayDraw(numPoints, windowed, transformed, null);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 */
	public void hullDraw(int numPoints, boolean windowed, boolean transformed) {
		MPolygon hull = this.hullRegion(numPoints, windowed, transformed);
		hull.draw(this.window);
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 * @param drawer
	 */
	public void delaunayVoronoiDraw(int numPoints, boolean windowed, boolean transformed, EdgeDrawer drawer) {
		MPolygon[] regions = this.voronoiRegions(numPoints, windowed, transformed);
		Delaunay[] delaunays = new Delaunay[regions.length];
		for (int i = 0; i < regions.length; i++) {
			MPolygon region = regions[i];
			delaunays[i] = new Delaunay(region.getCoords());
		}
		for (Delaunay d : delaunays) {
			this.drawEdges(d.getEdges(), drawer);
		}
	}

	/**
	 * @param numPoints
	 * @param windowed
	 * @param transformed
	 */
	public void delaunayVoronoiDraw(int numPoints, boolean windowed, boolean transformed) {
		this.delaunayVoronoiDraw(numPoints, windowed, transformed, null);
	}
	
	// public void drawPoints(float[][] points, PShape shape, PointDrawer drawer) that calls drawer.draw(point, shape)???
	// public void drawPoints(float[][] points, PGraphics window, PointDrawer drawer) that calls drawer.draw(point, window)???
	// window is a Crv.window and we set its origin to point
	public void drawPoints(float[][] points, PointDrawer drawer) {
		for(int i = 0; i < points.length; i++) {
			float[] point = points[i];
			if (drawer != null) {
				drawer.draw(point);
			} else {
				float x = point[0];
				float y = point[1];
				this.window.point(x, y);
			}
	  }
	}

	/**
	 * @param points
	 */
	public void drawPoints(float[][] points) {
		this.drawPoints(points, null);
	}

	/**
	 * @param edges
	 * @param drawer
	 */
	public void drawEdges(float[][] edges, EdgeDrawer drawer) {
		for(int i = 0; i < edges.length; i++) {
			float[] edge = edges[i];
			if (drawer != null) {
				drawer.draw(edge);
			} else {
				float startX = edge[0];
				float startY = edge[1];
				float endX = edge[2];
				float endY = edge[3];
				this.window.line(startX, startY, endX, endY);
			}
	  }
	}

	/**
	 * @param edges
	 */
	public void drawEdges(float[][] edges) {
		this.drawEdges(edges, null);
	}

}
