package crvs;


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import java.util.Arrays;
import java.lang.Math;

/**
 * This class provides a set of operations to create and manipulate curves.
 */
@SuppressWarnings("unused")
public class Ops {

    /**
     * The Parent.
     */
    PApplet parent;
    /**
     * The Table.
     */
    float[] table;

    /**
     * Initializes a new instance of the Ops class with a reference to the parent PApplet.
     * This is necessary for the Ops class to be able to use the PApplet's random functions.
     *
     * @param parent A reference to the parent PApplet instance. This is typically passed in from a sketch                using the 'this' keyword.
     * @param table  An array of floats to be used as a wavetable by the table() method.
     */
    public Ops(PApplet parent, float[] table) {
        this.parent = parent;
        if (table != null) {
        	this.table = table;
        } else {
        	this.table = new float[1];
        	this.table[0] = 0.0f;
        }
    }

    /**
     * Initializes a new instance of the Ops class with a reference to the parent PApplet.
     * This is necessary for the Ops class to be able to use the PApplet's random functions.
     *
     * @param parent A reference to the parent PApplet instance. This is typically passed in from a sketch                using the 'this' keyword.
     */
    public Ops(PApplet parent) {
    	this(parent, null);
    }

    /**
     * Transforms a unipolar FloatOp into a bipolar FloatOp.
     * The resulting FloatOp takes a position as an argument and applies the unipolar operation,
     * then scales the result from a unipolar range [0, 1] to a bipolar range [-1, 1].
     *
     * @param unipolarOp The unipolar FloatOp to be transformed into a bipolar FloatOp.
     * @return A new FloatOp that applies the unipolar operation and scales the result to the bipolar range.
     */
    public FloatOp bipolarize(FloatOp unipolarOp) {
    	return pos -> {
    		float unipolarValue = unipolarOp.apply(pos);
    		return unipolarValue * 2f - 1f;
    	};
    }

    /**
     * Transforms a bipolar FloatOp into a unipolar FloatOp.
     * The resulting FloatOp takes a position as an argument and applies the bipolar operation,
     * then scales the result from a bipolar range [-1, 1] to a unipolar range [0, 1].
     *
     * @param bipolarOp The bipolar FloatOp to be transformed into a unipolar FloatOp.
     * @return A new FloatOp that applies the bipolar operation and scales the result to the unipolar range.
     */
    public FloatOp rectify(FloatOp bipolarOp) {
    	return pos -> {
    		float bipolarValue = bipolarOp.apply(pos);
    		return bipolarValue * 0.5f + 0.5f;
    	};
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
        return (float) Math.toRadians(degrees); // Java's function to convert degrees to radians
    }

    /**
     * Generates a random number using triangular distribution between a given range.
     *
     * @param lo   Lower limit of the distribution.
     * @param hi   Upper limit of the distribution.
     * @param mode Mode of the distribution.
     * @return A random number generated using the triangular distribution.
     */
    public float triDist(float lo, float hi, float mode) {
        float F = (mode - lo) / (hi - lo);
        float rand = parent.random(1f);
        if (rand < F) {
            return lo + (float) Math.sqrt(rand * (hi - lo) * (mode - lo));
        } else {
            return hi - (float) Math.sqrt((1 - rand) * (hi - lo) * (hi - mode));
        }
    }

    /**
     * Returns a FloatOp that returns a constant value regardless of input.
     *
     * @param value Constant value to be returned.
     * @return FloatOp that returns a constant value.
     */
    public FloatOp c(float value) {
        return (float pos) -> value;
    }

    /**
     * Returns a FloatOp that always returns 0.0 regardless of the input.
     *
     * @return A FloatOp that always returns 0.0.
     */
    public FloatOp zero() {
        return (float pos) -> 0.0f;
    }

    /**
     * Returns a FloatOp that always returns 0.25 regardless of the input.
     *
     * @return A FloatOp that always returns 0.25.
     */
    public FloatOp fourth() {
        return (float pos) -> 0.25f;
    }

    /**
     * Returns a FloatOp that always returns 1/3 regardless of the input.
     *
     * @return A FloatOp that always returns 1/3.
     */
    public FloatOp third() {
        return (float pos) -> 1.0f/3.0f;
    }

    /**
     * Returns a FloatOp that always returns 0.5 regardless of the input.
     *
     * @return A FloatOp that always returns 0.5.
     */
    public FloatOp half() {
        return (float pos) -> 0.5f;
    }

    /**
     * Returns a FloatOp that always returns 1.0 regardless of the input.
     *
     * @return A FloatOp that always returns 1.0.
     */
    public FloatOp one() {
        return (float pos) -> 1.0f;
    }

    /**
     * Returns a FloatOp that always returns 2.0 regardless of the input.
     *
     * @return A FloatOp that always returns 2.0.
     */
    public FloatOp two() {
        return (float pos) -> 2.0f;
    }

    /**
     * Returns a FloatOp that always returns 3.0 regardless of the input.
     *
     * @return A FloatOp that always returns 3.0.
     */
    public FloatOp three() {
        return (float pos) -> 3.0f;
    }

    /**
     * Returns a FloatOp that always returns 4.0 regardless of the input.
     *
     * @return A FloatOp that always returns 4.0.
     */
    public FloatOp four() {
        return (float pos) -> 4.0f;
    }

    /**
     * Returns a FloatOp that always returns quarter pi regardless of the input.
     *
     * @return A FloatOp that always returns quarter pi.
     */
    public FloatOp quarterPi() {
        return (float pos) -> PConstants.QUARTER_PI;
    }

    /**
     * Returns a FloatOp that always returns a third of pi regardless of the input.
     *
     * @return A FloatOp that always returns quarter pi.
     */
    public FloatOp thirdPi() {
        return (float pos) -> PConstants.THIRD_PI;
    }

    /**
     * Returns a FloatOp that always returns half pi regardless of the input.
     *
     * @return A FloatOp that always returns half pi.
     */
    public FloatOp halfPi() {
        return (float pos) -> PConstants.HALF_PI;
    }

    /**
     * Returns a FloatOp that always returns pi regardless of the input.
     *
     * @return A FloatOp that always returns pi.
     */
    public FloatOp pi() {
        return (float pos) -> PConstants.PI;
    }

    /**
     * Returns a FloatOp that always returns two pi regardless of the input.
     *
     * @return A FloatOp that always returns two pi.
     */
    public FloatOp twoPi() {
        return (float pos) -> PConstants.TWO_PI;
    }

    /**
     * Returns a FloatOp that always returns the current width of the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the current width of the parent PApplet.
     */
    public FloatOp width() {
        return (float pos) -> parent.width;
    }

    /**
     * Returns a FloatOp that always returns the current height of the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the current height of the parent PApplet.
     */
    public FloatOp height() {
        return (float pos) -> parent.height;
    }

    /**
     * Returns a FloatOp that always returns the current frame count of the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the current frame count of the parent PApplet.
     */
    public FloatOp frameCount() {
        return (float pos) -> parent.frameCount;
    }

    /**
     * Returns a FloatOp that always returns the current x position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the current x position of the mouse in the parent PApplet.
     */
    public FloatOp mouseX() {
        return (float pos) -> parent.mouseX;
    }

    /**
     * Returns a FloatOp that always returns the current y position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the current y position of the mouse in the parent PApplet.
     */
    public FloatOp mouseY() {
        return (float pos) -> (float)parent.mouseY;
    }

    /**
     * Returns a FloatOp that always returns the previous x position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the previous x position of the mouse in the parent PApplet.
     */
    public FloatOp pmouseX() {
        return (float pos) -> (float)parent.pmouseX;
    }

    /**
     * Returns a FloatOp that always returns the previous y position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A FloatOp that always returns the previous y position of the mouse in the parent PApplet.
     */
    public FloatOp pmouseY() {
        return (float pos) -> (float)parent.pmouseY;
    }

    /**
     * Returns a FloatOp which represents a wavetable.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp table() {
		return pos -> {
			int t = (int) PApplet.map(pos, 0, 1, 0, this.table.length);
			float samp = this.table[t];
			return (samp + 1f) / 2f;
		};
    }

    /**
     * Generates a FloatOp for a Gaussian distribution that fluctuates between the values produced by the input FloatOps.
     * The resulting FloatOp applies a Gaussian operation to a position and scales it between the values provided by the lo and hi FloatOps at that position.
     * If either lo or hi is null, their respective values are defaulted to 0 and 1 respectively.
     *
     * @param lo The lower bound FloatOp. If null, defaults to 0.
     * @param hi The upper bound FloatOp. If null, defaults to 1.
     * @return A new FloatOp that applies the Gaussian operation and scales it between the values provided by the lo and hi FloatOps.
     */
    public FloatOp gaussian(FloatOp lo, FloatOp hi) {
        return pos -> {
            float g = parent.randomGaussian();
            if (g < -1f) {
            	g = g % -1f;
            } else if (g > 1f) {
            	g = g % 1f;
            }
            g = (g + 1) * 0.5f;
            float loVal = (lo != null) ? lo.apply(pos) : 0f;
            float hiVal = (hi != null) ? hi.apply(pos) : 1f;
            return loVal + (g * (hiVal - loVal));
        };
    }

    /**
     * Generates a FloatOp for a Gaussian distribution that fluctuates between 0 and 1.
     * This is equivalent to calling gaussian(null, null).
     *
     * @return A new FloatOp that applies the Gaussian operation and scales it between 0 and 1.
     */
    public FloatOp gaussian() {
    	return gaussian(null, null);
    }

    /**
     * Generates a FloatOp for a Gaussian distribution that fluctuates between 0 and the value produced by the hi FloatOp.
     * This is equivalent to calling gaussian(null, hi).
     *
     * @param hi The upper bound FloatOp.
     * @return A new FloatOp that applies the Gaussian operation and scales it between 0 and the value provided by the hi FloatOp.
     */
    public FloatOp gaussian(FloatOp hi) {
    	return gaussian(null, hi);
    }

    /**
     * Generates a FloatOp for a Gaussian distribution that fluctuates between 0 and the constant value hi.
     * This is equivalent to calling gaussian(null, c(hi)).
     *
     * @param hi The upper bound as a constant float value.
     * @return A new FloatOp that applies the Gaussian operation and scales it between 0 and the constant hi value.
     */
    public FloatOp gaussian(float hi) {
    	return gaussian(null, c(hi));
    }

    /**
     * Returns a FloatOp that applies a random operation within the range defined by two other FloatOps (lo and hi).
     * The random operation can be either uniformly random across the range, or triangularly distributed according to a third FloatOp (mode).
     *
     * @param lo   The lower bound FloatOp. If null, defaults to 0.
     * @param hi   The upper bound FloatOp. If null, defaults to 1.
     * @param mode A FloatOp that determines the mode of the triangular distribution. If null, the operation is uniformly random.
     * @return A new FloatOp that applies the random operation according to the parameters.
     */
    public FloatOp random(FloatOp lo, FloatOp hi, FloatOp mode) {
        return pos -> {
            float loVal = (lo != null) ? lo.apply(pos) : 0f;
            float hiVal = (hi != null) ? hi.apply(pos) : 1f;
            if (mode != null) {
                float modeVal = mode.apply(pos);
                return triDist(loVal, hiVal, modeVal);
            } else {
                return loVal + parent.random(hiVal - loVal);
            }
        };
    }

    /**
     * Returns a FloatOp that generates a random float between 0 and 1.
     * Equivalent to calling random(null, null, null).
     *
     * @return A new FloatOp that generates a random float between 0 and 1.
     */
    public FloatOp random() {
    	return random(null, null, null);
    }

    /**
     * Returns a FloatOp that generates a random float between 0 and the value produced by the hi FloatOp.
     * Equivalent to calling random(null, hi, null).
     *
     * @param hi The upper bound FloatOp.
     * @return A new FloatOp that generates a random float between 0 and the value produced by the hi FloatOp.
     */
    public FloatOp random(FloatOp hi) {
    	return random(null, hi, null);
    }

    /**
     * Returns a FloatOp that generates a random float between 0 and the constant hi.
     * Equivalent to calling random(null, c(hi), null).
     *
     * @param hi The upper bound as a constant float value.
     * @return A new FloatOp that generates a random float between 0 and the constant hi.
     */
    public FloatOp random(float hi) {
    	return random(null, c(hi), null);
    }

    /**
     * Returns a FloatOp that applies a Perlin noise operation to the values produced by the x, y, and z FloatOps. The noise detail is
     * determined by the octaves and falloff FloatOps.
     *
     * @param x       The FloatOp for the x-coordinate of the Perlin noise. It cannot be null.
     * @param y       The FloatOp for the y-coordinate of the Perlin noise. If null, the function becomes 1D Perlin noise.
     * @param z       The FloatOp for the z-coordinate of the Perlin noise. If null, the function becomes 2D Perlin noise.
     * @param octaves The FloatOp for the number of octaves of the Perlin noise. If null, defaults to 4.
     * @param falloff The FloatOp for the falloff factor of the Perlin noise. If null, defaults to 0.5.
     * @return A new FloatOp that applies the Perlin noise operation according to the parameters.
     */
    public FloatOp perlin(FloatOp x, FloatOp y, FloatOp z, FloatOp octaves, FloatOp falloff) {
        return pos -> {
        	int lod = 4;
        	float fof = 0.5f;
        	if (octaves != null) {
        		lod = (int) octaves.apply(pos);
        	}
        	if (falloff != null) {
        		fof = falloff.apply(pos);
        	}
        	parent.noiseDetail(lod, fof);
            float xV = x.apply(pos);
            if (y == null) {
                return parent.noise(xV);
            }
            float yV = y.apply(pos);
            if (z == null) {
                return parent.noise(xV, yV);
            }
            float zV = z.apply(pos);
            return parent.noise(xV, yV, zV);
        };
    }

    /**
     * Returns a FloatOp that applies a 1D Perlin noise operation to the value produced by the x FloatOp.
     * Equivalent to calling perlin(x, null, null, null, null).
     *
     * @param x The FloatOp for the x-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 1D Perlin noise operation.
     */
    public FloatOp perlin(FloatOp x) {
    	return perlin(x, null, null, null, null);
    }

    /**
     * Returns a FloatOp that applies a 1D Perlin noise operation to the constant x.
     * Equivalent to calling perlin(c(x), null, null, null, null).
     *
     * @param x The constant value for the x-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 1D Perlin noise operation.
     */
    public FloatOp perlin(float x) {
    	return perlin(c(x), null, null, null, null);
    }

    /**
     * Returns a FloatOp that applies a 2D Perlin noise operation to the values produced by the x and y FloatOps.
     * Equivalent to calling perlin(x, y, null, null, null).
     *
     * @param x The FloatOp for the x-coordinate of the Perlin noise.
     * @param y The FloatOp for the y-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 2D Perlin noise operation.
     */
    public FloatOp perlin(FloatOp x, FloatOp y) {
    	return perlin(x, y, null, null, null);
    }

    /**
     * Returns a FloatOp that applies a 2D Perlin noise operation to the constant values x and y.
     * Equivalent to calling perlin(c(x), c(y), null, null, null).
     *
     * @param x The constant value for the x-coordinate of the Perlin noise.
     * @param y The constant value for the y-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 2D Perlin noise operation.
     */
    public FloatOp perlin(float x, float y) {
    	return perlin(c(x), c(y), null, null, null);
    }

    /**
     * Returns a FloatOp that applies a 3D Perlin noise operation to the values produced by the x, y, and z FloatOps.
     * Equivalent to calling perlin(x, y, z, null, null).
     *
     * @param x The FloatOp for the x-coordinate of the Perlin noise.
     * @param y The FloatOp for the y-coordinate of the Perlin noise.
     * @param z The FloatOp for the z-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 3D Perlin noise operation.
     */
    public FloatOp perlin(FloatOp x, FloatOp y, FloatOp z) {
    	return perlin(x, y, z, null, null);
    }

    /**
     * Returns a FloatOp that applies a 3D Perlin noise operation to the constant values x, y, and z.
     * Equivalent to calling perlin(c(x), c(y), c(z), null, null).
     *
     * @param x The constant value for the x-coordinate of the Perlin noise.
     * @param y The constant value for the y-coordinate of the Perlin noise.
     * @param z The constant value for the z-coordinate of the Perlin noise.
     * @return A new FloatOp that applies the 3D Perlin noise operation.
     */
    public FloatOp perlin(float x, float y, float z) {
    	return perlin(c(x), c(y), c(z), null, null);
    }

    /**
     * Returns a FloatOp which represents a linear ramp function.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp phasor() {
        return pos -> pos;
    }

    /**
     * Returns a FloatOp which represents a sawtooth waveform function.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp saw() {
        return pos -> 1f - pos;
    }

    /**
     * Returns a FloatOp which represents a triangular waveform function.
     *
     * @param s Symmetry operator, defaults to 0.5
     * @return FloatOp representing the described function.
     */
    public FloatOp tri(FloatOp s) {
        return pos -> {
        	float sValue = 0.5f;
        	if (s != null) sValue = s.apply(pos);
            return pos < sValue ? pos / sValue : 1f - ((pos - sValue) / (1f - sValue));
        };
    }

    /**
     * Returns a FloatOp which represents a triangular waveform function, with default symmetry around 0.5
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp tri() {
    	return tri(null);
    }

    /**
     * Returns a FloatOp which represents a triangular waveform function.
     *
     * @param s Symmetry float value, defaults to 0.5
     * @return FloatOp representing the described function.
     */
    public FloatOp tri(float s) {
    	return tri(c(s));
    }

    /**
     * Returns a FloatOp which represents a sine waveform function with optional feedback.
     *
     * @param fb function to calculate the amount of feedback.
     * @return FloatOp representing the described function.
     */
    public FloatOp sine(FloatOp fb) {
        return pos -> {
            if (fb != null) {
            	float fbScale = fb.apply(pos);
            	pos = pos + fbScale * (float) (Math.sin(pos2Rad(pos)) * 0.5f) + 0.5f;
            }
            return (float) (Math.sin(pos2Rad(pos % 1f)) * 0.5f) + 0.5f;
        };
    }

    /**
     * Returns a FloatOp which represents a sine waveform function with no feedback.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp sine() {
    	return sine(null);
    }

    /**
     * Returns a FloatOp which represents a sine waveform function with optional feedback.
     *
     * @param fb float to set the amount of feedback.
     * @return FloatOp representing the described function.
     */
    public FloatOp sine(float fb) {
    	return sine(c(fb));
    }

    /**
     * Returns a FloatOp which represents an arc sine waveform.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp asin() {
    	return pos -> {
    		pos = pos * 2f - 1f;
    		return (PApplet.asin(pos) + PConstants.HALF_PI) / PConstants.PI;
    	};
    }

    /**
     * Creates a cosine wave function with optional feedback
     *
     * @param fb function to calculate the amount of feedback
     * @return a FloatOp representing the function
     */
    public FloatOp cos(FloatOp fb) {
        return pos -> {
            if (fb != null) {
            	float fbScale = fb.apply(pos);
            	pos = pos + fbScale * (float) ((Math.cos(pos2Rad(pos)) * 0.5f) + 0.5f);
            }
            return (float) (Math.cos(pos2Rad(pos % 1f)) * 0.5f) + 0.5f;
        };
    }

    /**
     * Returns a FloatOp which represents a cosine waveform function with no feedback.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp cos() {
    	return cos(null);
    }

    /**
     * Returns a FloatOp which represents a cosine waveform function with optional feedback.
     *
     * @param fb float to set the amount of feedback.
     * @return FloatOp representing the described function.
     */
    public FloatOp cos(float fb) {
    	return cos(c(fb));
    }

    /**
     * Returns a FloatOp which represents an arc cosine waveform.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp acos() {
    	return pos -> {
    		pos = pos * 2f - 1f;
    		return PApplet.acos(pos) / PConstants.PI;
    	};
    }

    /**
     * Creates a tangent wave function with optional feedback.
     *
     * @param fb function to calculate the amount of feedback
     * @return a FloatOp representing the function
     */
    public FloatOp tan(FloatOp fb) {
        return pos -> {
            if (fb != null) {
            	float fbScale = fb.apply(pos);
            	pos = pos + fbScale * (float) ((Math.tan(pos2Rad(pos)) * 0.5f) + 0.5f);
            }
            return (float) (Math.tan(pos2Rad(pos % 1f)) * 0.5f) + 0.5f;
        };
    }

    /**
     * Creates a tangent wave function with no feedback.
     *
     * @return a FloatOp representing the function
     */
    public FloatOp tan() {
    	return tan(null);
    }

    /**
     * Creates a tangent wave function with optional feedback.
     *
     * @param fb float to set the amount of feedback
     * @return a FloatOp representing the function
     */
    public FloatOp tan(float fb) {
    	return tan(c(fb));
    }

    /**
     * Returns a FloatOp which represents a pulse waveform function.
     *
     * @param w Width operator.
     * @return FloatOp representing the described function.
     */
    public FloatOp pulse(FloatOp w) {
        return pos -> {
        	float wValue = 0.5f;
        	if (w != null) wValue = w.apply(pos);
            return pos < wValue ? 0f : 1f;
        };
    }

    /**
     * Returns a FloatOp which represents a pulse waveform function with default width 0.5
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp pulse() {
    	return pulse(null);
    }

    /**
     * Returns a FloatOp which represents a square waveform function, convenience alias for default pulse()
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp square() {
    	return pulse(null);
    }

    /**
     * Returns a FloatOp which represents a pulse waveform function.
     *
     * @param w Width float.
     * @return FloatOp representing the described function.
     */
    public FloatOp pulse(float w) {
    	return pulse(c(w));
    }

    /**
     * Returns a FloatOp which represents an ease-in curve function.
     *
     * @param e Ease operator, default value 2.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeIn(FloatOp e) {
        return pos -> {
        	float eValue = 2.0f;
        	if (e != null) eValue = e.apply(pos);
            return (float) Math.pow(pos, eValue);
        };
    }

    /**
     * Returns a FloatOp which represents an ease-in curve function with default e value of 2.0.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp easeIn() {
    	return easeIn(null);
    }

    /**
     * Returns a FloatOp which represents an ease-in curve function.
     *
     * @param e Ease float, default value 2.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeIn(float e) {
    	return easeIn(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-out curve function.
     *
     * @param e Ease operator, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOut(FloatOp e) {
        return pos -> {
            float eValue = 3.0f;
            if (e != null) eValue = e.apply(pos);
            return (float) (1 - Math.pow((1 - pos), eValue));
        };
    }

    /**
     * Returns a FloatOp which represents an ease-out curve function with default e value of 3.0.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOut() {
    	return easeOut(null);
    }

    /**
     * Returns a FloatOp which represents an ease-out curve function.
     *
     * @param e Ease float, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOut(float e) {
    	return easeOut(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-in-out curve function.
     *
     * @param e Ease operator, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeInOut(FloatOp e) {
        return pos -> {
            float value = pos * 2f;
            float eValue = 3.0f;
            if (e != null) eValue = e.apply(pos);
            if (value < 1) {
                return 0.5f * (float) Math.pow(value, eValue);
            } else {
                return 0.5f * (float) (2f - Math.pow((2f - value), eValue));
            }
        };
    }

    /**
     * Returns a FloatOp which represents an ease-in-out curve function with default e value of 3.0.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp easeInOut() {
    	return easeInOut(null);
    }

    /**
     * Returns a FloatOp which represents an ease-in-out curve function.
     *
     * @param e Ease float, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeInOut(float e) {
    	return easeInOut(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-out-in curve function.
     *
     * @param e Ease operator, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOutIn(FloatOp e) {
        return pos -> {
        	float value = pos * 2f;
            float eValue = 3.0f;
            if (e != null) eValue = e.apply(pos);
            if (value < 1) {
            	return (float) (1f - Math.pow((1f - value), eValue) * 0.5f) - 0.5f;
            } else {
            	value = value - 1;
            	return (float) (Math.pow(value, eValue) * 0.5f) + 0.5f;
            }
        };
    }

    /**
     * Returns a FloatOp which represents an ease-out-in curve function with default e value of 3.0.
     *
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOutIn() {
    	return easeOutIn(null);
    }

    /**
     * Returns a FloatOp which represents an ease-out-in curve function.
     *
     * @param e Ease float, default value 3.0.
     * @return FloatOp representing the described function.
     */
    public FloatOp easeOutIn(float e) {
    	return easeOutIn(c(e));
    }

    /**
     * Returns a FloatOp that multiplies the output of the provided FloatOp with the specified scalar.
     *
     * @param op     The FloatOp whose output will be multiplied.
     * @param scalar The scalar to multiply by.
     * @return A new FloatOp that multiplies the output of the original FloatOp by the scalar.
     */
    public FloatOp mult(FloatOp op, float scalar) {
    	return pos -> {
    		float v = op.apply(pos);
    		return v * scalar;
    	};
    }

    /**
     * Returns a FloatOp that adds the specified offset to the output of the provided FloatOp.
     *
     * @param op     The FloatOp whose output will be biased.
     * @param offset The offset to add.
     * @return A new FloatOp that adds the offset to the output of the original FloatOp.
     */
    public FloatOp bias(FloatOp op, float offset) {
    	return pos -> {
    		float v = op.apply(pos);
    		return v + offset;
    	};
    }

    /**
     * Returns a FloatOp that adds the output of the offset FloatOp to the output of the original FloatOp.
     *
     * @param op     The original FloatOp.
     * @param offset The FloatOp that provides the offset to add.
     * @return A new FloatOp that adds the output of the offset FloatOp to the output of the original FloatOp.
     */
    public FloatOp bias(FloatOp op, FloatOp offset) {
    	return pos -> {
    		float v = op.apply(pos);
    		float offV = offset.apply(pos);
    		return v + offV;
    	};
    }

    /**
     * Phase float op.
     *
     * @param op          the op
     * @param phaseOffset the phase offset
     * @return the float op
     */
    public FloatOp phase(FloatOp op, float phaseOffset) {
        return pos -> {
            pos = pos + phaseOffset;
            if (pos > 1.0f)
                pos = pos % 1.0f;
            return op.apply(pos);
        };
    }

    /**
     * Phase float op.
     *
     * @param op          the op
     * @param phaseOffset the phase offset
     * @return the float op
     */
    public FloatOp phase(FloatOp op, FloatOp phaseOffset) {
        return pos -> {
            float po = phaseOffset.apply(pos);
            pos = pos + po;
            if (pos > 1.0f)
                pos = pos % 1.0f;
            return op.apply(pos);
        };
    }

    /**
     * Rate float op.
     *
     * @param op         the op
     * @param rateOffset the rate offset
     * @return the float op
     */
    public FloatOp rate(FloatOp op, float rateOffset) {
        return pos -> {
            pos = pos * rateOffset;
            if (pos > 1.0f)
                pos = pos % 1.0f;
            return op.apply(pos);
        };
    }

    /**
     * Rate float op.
     *
     * @param op         the op
     * @param rateOffset the rate offset
     * @return the float op
     */
    public FloatOp rate(FloatOp op, FloatOp rateOffset) {
        return pos -> {
            float ro = rateOffset.apply(pos);
            pos = pos * ro;
            if (pos > 1.0f)
                pos = pos % 1.0f;
            return op.apply(pos);
        };
    }

    /**
     * Returns a FloatOp that multiplies the output of opA with the output of opB.
     *
     * @param opA The first FloatOp.
     * @param opB The second FloatOp.
     * @return A new FloatOp that multiplies the outputs of the two original FloatOps.
     */
    public FloatOp ring(FloatOp opA, FloatOp opB) {
    	return pos -> {
    		float aVal = opA.apply(pos);
    		float bVal = opB.apply(pos);
    		return aVal * bVal;
    	};
    }

    /**
     * Returns a FloatOp that applies a folding operation on the output of the provided FloatOp based on a threshold.
     * The fold operation reduces the output to within the range [0, threshold], "folding" values over the threshold
     * back into the range.
     *
     * @param op        The FloatOp whose output will be folded.
     * @param threshold The FloatOp that provides the threshold.
     * @return A new FloatOp that applies the fold operation on the output of the original FloatOp.
     */
    public FloatOp fold(FloatOp op, FloatOp threshold) {
        return pos -> {
        	float tVal = threshold.apply(pos);
            float value = op.apply(pos);
            while (value > tVal) {
            	value = tVal - (value - tVal);
            }
            return value;
        };
    }

    /**
     * Fold float op.
     *
     * @param op        the op
     * @param threshold the threshold
     * @return the float op
     */
    public FloatOp fold(FloatOp op, float threshold) {
        return pos -> {
            float value = op.apply(pos);
            while (value > threshold) {
                value = threshold - (value - threshold);
            }
            return value;
        };
    }

    /**
     * Fold float op. Default threshold of 1
     *
     * @param op the op
     * @return the float op
     */
    public FloatOp fold(FloatOp op) {
        return pos -> {
            float value = op.apply(pos);
            while (value > 1f) {
                value = 1f - (value - 1f);
            }
            return value;
        };
    }

    /**
     * Low pass filter float op.
     *
     * @param inputOp    the input op
     * @param windowSize the window size
     * @return the float op
     */
    public FloatOp lowPassFilter(FloatOp inputOp, int windowSize) {
        return pos -> {
            float sum = 0;
            for(int i = 0; i < windowSize; i++) {
                float offsetPos = pos - ((float) i / windowSize);
                // Ensure the position is within the curve bounds
                offsetPos = Math.max(0, Math.min(1, offsetPos));
                sum += inputOp.apply(offsetPos);
            }
            return sum / windowSize;
        };
    }

    /**
     * Chain float op.
     *
     * @param ops the ops
     * @return the float op
     */
    public FloatOp chain(FloatOp[] ops) {
    	return pos -> {
    		float value = ops[0].apply(pos);
    		for (int i = 1; i < ops.length; i++) {
    			value = ops[i].apply(value);
    		}
    		return value;
    	};
    }

    /**
     * Choose float op.
     *
     * @param ops the ops
     * @return the float op
     */
    public FloatOp choose(FloatOp[] ops) {
    	return pos -> {
    		int idx = (int) parent.random(ops.length);
    		return ops[idx].apply(pos);
    	};
    }

    /**
     * Normalizes the given array of values.
     *
     * @param values Array of values to normalize.
     * @return Array of normalized values.
     */
    public float[] normalize(float[] values) {
        double[] dValues = new double[values.length];
        for (int i = 0; i < values.length; i++)
        {
        	dValues[i] = values[i];
        }
        double min = Arrays.stream(dValues).min().getAsDouble();
        double max = Arrays.stream(dValues).max().getAsDouble();
        double[] normDVals = Arrays.stream(dValues).map(v -> (v - min) / (max - min)).toArray();
        float[] normFloats = new float[values.length];
        for (int i=0; i< values.length; i++) {
        	normFloats[i] = (float) normDVals[i];
        }
        return normFloats;
    }

    /**
     * Returns a FloatOp which interpolates between given y-values at given position.
     *
     * @param yvalues Array of y-values.
     * @return FloatOp representing the described function.
     */
    public FloatOp timeseries(float[] yvalues) {
    	float[] normalizedYvalues = normalize(yvalues);
        return pos -> {
            int index = (int) (pos * (normalizedYvalues.length - 1f));
            float fraction = pos * (normalizedYvalues.length - 1f) - index;
            return (normalizedYvalues[index] * (1.0f - fraction)) + (normalizedYvalues[index + 1] * fraction);
        };
    }

    /**
     * Transforms a given FloatOp into an array of floats.
     *
     * @param op         FloatOp to convert to array.
     * @param numSamples Number of samples to use for transformation.
     * @param mapFunc    Mapping function to apply to each value.
     * @return Array of transformed values.
     */
    public float[] array(FloatOp op, int numSamples, FloatOp mapFunc) {
    	float step = 1f / numSamples;
    	float[] table = new float[numSamples];
    	for (int i=0; i<numSamples; i++) {
    		float x = i * step;
    		table[i] = op.apply(x / numSamples);
    	}
    	if (mapFunc == null) {
    		return table;
    	}
        float[] mapOut = new float[numSamples];
        for(int i=0; i<numSamples; i++) {
        	mapOut[i] = mapFunc.apply(table[i]);
        }
        return mapOut;
    }

    /**
     * Generates an array of points that are distributed along the provided curve.
     * <p>
     * The curve is represented by the given FloatOp function.
     * The points are spaced equally along the x-axis between the provided start and end values.
     * The total number of points generated is specified by the numPoints parameter.
     *
     * @param curve     a FloatOp function representing the curve.
     * @param start     the x-coordinate of the first point.
     * @param end       the x-coordinate of the last point.
     * @param numPoints the total number of points to generate.
     * @param yscale    scalar to multiply by the y value
     * @return an array of PVector objects representing the points along the curve.
     */
    public PVector[] points(FloatOp curve, float start, float end, int numPoints, float yscale) {
        PVector[] points = new PVector[numPoints];
        float step = (end - start) / numPoints;
        end = end - (step - 1);

        for(int i = 0; i < numPoints; i++) {
            float x = start + i * step;
            float y = curve.apply(x / end);
            points[i] = new PVector(x, parent.height - y*yscale);
        }

        return points;
    }

    /**
     * Plots the curve of the provided function on the parent PApplet instance.
     * The function is evaluated across the width of the sketch, and the result is scaled and drawn as a curve.
     *
     * @param func   The function to plot. This should be a FloatOp which takes a single argument in the range [0, 1]              and returns a single value, which will be plotted on the y-axis.
     * @param yscale The vertical scale factor to apply to the output of the function. This controls the height of the curve on the sketch.
     * @param color  Processing color int to use for stroke or fill
     * @param fill   If true fill w/ noStroke, if false stroke w/ noFill
     */
    public void plot(FloatOp func, float yscale, int color, boolean fill) {
    	if (fill) {
    		parent.noStroke();
    		parent.fill(color);
    	} else {
    		parent.noFill();
    		parent.stroke(color);
    	}
        PVector[] points = points(func, 0, parent.width, parent.width, yscale);
        parent.beginShape();
        for (PVector p : points) {
            parent.vertex(p.x, parent.height - p.y);
        }
        parent.endShape();
    }
}
