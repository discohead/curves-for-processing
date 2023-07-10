package crvs;


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import java.util.Arrays;
import java.lang.Math;

/**
 * This class provides a set of operations to create and manipulate curves.
 */
public class Crvs {
	
    PApplet parent;
    float[] table;

    /**
     * Initializes a new instance of the Curves class with a reference to the parent PApplet.
     * This is necessary for the Curves class to be able to draw on the parent PApplet's canvas.
     *
     * @param parent A reference to the parent PApplet instance. This is typically passed in from a sketch 
     *               using the 'this' keyword.
     * @param table An array of floats to be used as a wavetable by the table() method.
     */
    public Crvs(PApplet parent, float[] table) {
        this.parent = parent;
        if (table != null) {
        	this.table = table;
        } else {
        	this.table = new float[1];
        	this.table[0] = 0.0f;
        }
    }
    
    public Crvs(PApplet parent) {
    	this(parent, null);
    }

    /**
     * Clamps the position value between 0 and 1.
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
     * @param low Lower limit of the distribution.
     * @param high Upper limit of the distribution.
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
     * @return FloatOp representing the described function.
     */
    public FloatOp table() {
		return pos -> {
			int t = (int) PApplet.map((float) pos, 0, 1, 0, this.table.length);
			float samp = (float) this.table[t];
			return (samp + 1f) / 2f;
		};
    }
    
    /**
     * Returns a FloatOp which represents a random number generator function with Gaussian/normal distribution.
     * @param lo Lower bound operator.
     * @param hi Upper bound operator.
     * @return FloatOp representing the described function.
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
    
    public FloatOp gaussian() {
    	return gaussian(null, null);
    }
    
    public FloatOp gaussian(FloatOp hi) {
    	return gaussian(null, hi);
    }
    
    public FloatOp gaussian(float hi) {
    	return gaussian(null, c(hi));
    }
    
    /**
     * Returns a FloatOp which represents a random number generator function with optional mode.
     * @param lo Lower bound operator.
     * @param hi Upper bound operator.
     * @param mode Mode operator, if null Uniform else Triangular Distribution.
     * @return FloatOp representing the described function.
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
    
    public FloatOp random() {
    	return random(null, null, null);
    }
    
    public FloatOp random(FloatOp hi) {
    	return random(null, hi, null);
    }
    
    public FloatOp random(float hi) {
    	return random(null, c(hi), null);
    }

    /**
     * Generates a FloatOp for Perlin noise. The noise value is determined by applying the
     * supplied FloatOp parameters as x, y, and z coordinates in the Perlin noise space.
     *
     * @param x A FloatOp used to compute the x-coordinate for the Perlin noise function.
     * @param y A FloatOp used to compute the y-coordinate for the Perlin noise function.
     * @param z A FloatOp used to compute the z-coordinate for the Perlin noise function.
     * @param octaves A FloatOp used to compute the number of octaves to be used by the noise, defaults to 4.
     * @param falloff A FloatOp used to compute the falloff factor for each octave, defaults to 0.5.
     * @return A FloatOp that represents Perlin noise over the 3-dimensional space defined by the input functions.
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
    
    public FloatOp perlin(FloatOp x) {
    	return perlin(x, null, null, null, null);
    }
    
    public FloatOp perlin(float x) {
    	return perlin(c(x), null, null, null, null);
    }
    
    public FloatOp perlin(FloatOp x, FloatOp y) {
    	return perlin(x, y, null, null, null);
    }
    
    public FloatOp perlin(float x, float y) {
    	return perlin(c(x), c(y), null, null, null);
    }
    
    public FloatOp perlin(FloatOp x, FloatOp y, FloatOp z) {
    	return perlin(x, y, z, null, null);
    }
    
    public FloatOp perlin(float x, float y, float z) {
    	return perlin(c(x), c(y), c(z), null, null);
    }

    /**
     * Returns a FloatOp which represents a ramp function.
     * @return FloatOp representing the described function.
     */
    public FloatOp phasor() {
        return pos -> {
            return pos;
        };
    }

    /**
     * Returns a FloatOp which represents a sawtooth waveform function.
     * @return FloatOp representing the described function.
     */
    public FloatOp saw() {
        return pos -> {
            return 1f - pos;
        };
    }

    /**
     * Returns a FloatOp which represents a triangular waveform function.
     * @param s Symmetry operator.
     * @return FloatOp representing the described function.
     */
    public FloatOp tri(FloatOp s) {
        return pos -> {
        	float sValue = 0.5f;
        	if (s != null) sValue = s.apply(pos);
            return pos < sValue ? pos / sValue : 1f - ((pos - sValue) / (1f - sValue));
        };
    }
    
    public FloatOp tri() {
    	return tri(null);
    }
    
    public FloatOp tri(float s) {
    	return tri(c(s));
    }

    /**
     * Returns a FloatOp which represents a sine waveform function with optional feedback.
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
    
    public FloatOp sine() {
    	return sine(null);
    }
    
    public FloatOp sine(float fb) {
    	return sine(c(fb));
    }
    
    /**
     * Creates a cosine wave function with optional feedback
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
    
    public FloatOp cos() {
    	return cos(null);
    }
    
    public FloatOp cos(float fb) {
    	return cos(c(fb));
    }

    /**
     * Creates a tangent wave function with optional feedback.
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
    
    public FloatOp tan() {
    	return tan(null);
    }
    
    public FloatOp tan(float fb) {
    	return tan(c(fb));
    }

    /**
     * Returns a FloatOp which represents a pulse waveform function.
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
    
    public FloatOp pulse() {
    	return pulse(null);
    }
    
    public FloatOp pulse(float w) {
    	return pulse(c(w));
    }

    /**
     * Returns a FloatOp which represents an ease-in curve function.
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
    
    public FloatOp easeIn() {
    	return easeIn(null);
    }
    
    public FloatOp easeIn(float e) {
    	return easeIn(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-out curve function.
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
    
    public FloatOp easeOut() {
    	return easeOut(null);
    }
    
    public FloatOp easeOut(float e) {
    	return easeOut(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-in-out curve function.
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
    
    public FloatOp easeInOut() {
    	return easeInOut(null);
    }
    
    public FloatOp easeInOut(float e) {
    	return easeInOut(c(e));
    }

    /**
     * Returns a FloatOp which represents an ease-out-in curve function.
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
    
    public FloatOp easeOutIn() {
    	return easeOutIn(null);
    }
    
    public FloatOp easeOutIn(float e) {
    	return easeOutIn(c(e));
    }
    
    public FloatOp fold(FloatOp curve, FloatOp threshold) {
        return pos -> {
        	float tVal = threshold.apply(pos);
            float value = curve.apply(pos);
            while (value > tVal) {
            	value = tVal - (value - tVal);
            }
            return value;
        };
    }


    /**
     * Normalizes the given array of values.
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
     * Transforms a given function into a table of values.
     * @param f Function to transform.
     * @param numSamples Number of samples to use for transformation.
     * @param mapFunc Mapping function to apply to each value.
     * @return Array of transformed values.
     */
    public float[] toTable(FloatOp f, int numSamples, FloatOp mapFunc) {
    	float step = 1f / numSamples;
    	float[] table = new float[numSamples];
    	for (int i=0; i<numSamples; i++) {
    		float x = i * step;
    		table[i] = f.apply(x / numSamples);
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
     *
     * The curve is represented by the given FloatOp function.
     * The points are spaced equally along the x-axis between the provided start and end values.
     * The total number of points generated is specified by the numPoints parameter.
     *
     * @param curve a FloatOp function representing the curve.
     * @param start the x-coordinate of the first point.
     * @param end the x-coordinate of the last point.
     * @param numPoints the total number of points to generate.
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
     * @param func The function to plot. This should be a FloatOp which takes a single argument in the range [0, 1] 
     *             and returns a single value, which will be plotted on the y-axis.
     * @param yscale The vertical scale factor to apply to the output of the function. This controls the height of the curve on the sketch.
     * @param color Processing color int to use for stroke or fill
     * @param fill If true fill w/ noStroke, if false stroke w/ noFill
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
        for (int i = 0; i < points.length; i++) {
        	PVector p = points[i];
        	parent.vertex(p.x, parent.height - p.y);
        }
        parent.endShape();
    }
    
    

}
