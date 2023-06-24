package crvs;


import processing.core.*;
import java.util.function.DoubleUnaryOperator;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.lang.Math;

/**
 * This class provides a set of operations to create and manipulate curves.
 */
public class Crvs {

    PApplet parent;

    /**
     * Initializes a new instance of the Curves class with a reference to the parent PApplet.
     * This is necessary for the Curves class to be able to draw on the parent PApplet's canvas.
     *
     * @param parent A reference to the parent PApplet instance. This is typically passed in from a sketch 
     *               using the 'this' keyword.
     */
    public Crvs(PApplet parent) {
        this.parent = parent;
    }

    /**
     * Clamps the position value between 0 and 1.
     * @param pos Position value to be clamped.
     * @return Clamped position value.
     */
    private double clamp(double pos) {
        return Math.max(0.0, Math.min(1.0, pos));
    }

    /**
     * Calculates a position value based on input rate and phase.
     * @param pos Original position.
     * @param rate Rate operator to adjust the position.
     * @param phase Phase operator to adjust the position.
     * @return Calculated position.
     */
    private double calcPos(double pos, DoubleUnaryOperator rate, DoubleUnaryOperator phase) {
        pos = clamp(pos);
        if (rate != null) {
            pos = pos * rate.applyAsDouble(pos);
        }
        if (phase != null) {
            pos = (pos + phase.applyAsDouble(pos)) % 1.0;
        }
        return pos;
    }

    /**
     * Amplifies and biases the value based on provided amp and bias parameters.
     * @param value Input value to be amplified and biased.
     * @param amp Amplifier operator.
     * @param bias Bias operator.
     * @param pos Position at which the operation is performed.
     * @return Biased and amplified value.
     */
    private double ampBias(double value, DoubleUnaryOperator amp, DoubleUnaryOperator bias, double pos) {
        if (amp != null) {
            value = value * amp.applyAsDouble(pos);
        }
        if (bias != null) {
            value = value + bias.applyAsDouble(pos);
        }
        return value;
    }
    
    /**
     * Converts a position from a 0-1 range to radians in a 0-2π range.
     *
     * @param pos The position to convert, in the 0-1 range.
     * @return The corresponding radian value in the 0-2π range.
     */
    public double pos2Rad(double pos) {
        pos = clamp(pos);
        double degrees = pos * 360;
        return Math.toRadians(degrees); // Java's function to convert degrees to radians
    }


    /**
     * Generates a random number using triangular distribution between a given range.
     * @param low Lower limit of the distribution.
     * @param high Upper limit of the distribution.
     * @param mode Mode of the distribution.
     * @return A random number generated using the triangular distribution.
     */
    private double triDistribution(double low, double high, double mode) {
        double r = parent.random(1f);
        if (mode == -1) {
            mode = 0.5;
        } else {
            double divisor = high - low;
            if (divisor < 0) divisor = low;
        }
        if (r > mode) {
            r = 1.0 - r;
            mode = 1.0 - mode;
            double temp = low;
            low = high;
            high = temp;
        }
        return low + (high - low) * Math.sqrt(r * mode);
    }

    /**
     * Returns a DoubleUnaryOperator that returns a constant value regardless of input.
     * @param value Constant value to be returned.
     * @return DoubleUnaryOperator that returns a constant value.
     */
    public DoubleUnaryOperator c(double value) {
        return (double pos) -> value;
    }
    
    /**
     * Returns a DoubleUnaryOperator that always returns 1.0 regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns 1.0.
     */
    public DoubleUnaryOperator one() {
        return (double pos) -> 1.0;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns 0.0 regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns 0.0.
     */
    public DoubleUnaryOperator zero() {
        return (double pos) -> 0.0;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns 0.5 regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns 0.5.
     */
    public DoubleUnaryOperator half() {
        return (double pos) -> 0.5;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns 0.25 regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns 0.25.
     */
    public DoubleUnaryOperator fourth() {
        return (double pos) -> 0.25;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns 1/3 regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns 1/3.
     */
    public DoubleUnaryOperator third() {
        return (double pos) -> 1.0/3.0;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns half pi regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns half pi.
     */
    public DoubleUnaryOperator halfPi() {
        return (double pos) -> (double)PConstants.HALF_PI;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns pi regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns pi.
     */
    public DoubleUnaryOperator pi() {
        return (double pos) -> (double)PConstants.PI;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns quarter pi regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns quarter pi.
     */
    public DoubleUnaryOperator quarterPi() {
        return (double pos) -> (double)PConstants.QUARTER_PI;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns two pi regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns two pi.
     */
    public DoubleUnaryOperator twoPi() {
        return (double pos) -> (double)PConstants.TWO_PI;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the current width of the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the current width of the parent PApplet.
     */
    public DoubleUnaryOperator width() {
        return (double pos) -> (double)parent.width;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the current height of the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the current height of the parent PApplet.
     */
    public DoubleUnaryOperator height() {
        return (double pos) -> (double)parent.height;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the current frame count of the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the current frame count of the parent PApplet.
     */
    public DoubleUnaryOperator frameCount() {
        return (double pos) -> (double)parent.frameCount;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the current x position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the current x position of the mouse in the parent PApplet.
     */
    public DoubleUnaryOperator mouseX() {
        return (double pos) -> (double)parent.mouseX;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the current y position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the current y position of the mouse in the parent PApplet.
     */
    public DoubleUnaryOperator mouseY() {
        return (double pos) -> (double)parent.mouseY;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the previous x position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the previous x position of the mouse in the parent PApplet.
     */
    public DoubleUnaryOperator pmouseX() {
        return (double pos) -> (double)parent.pmouseX;
    }

    /**
     * Returns a DoubleUnaryOperator that always returns the previous y position of the mouse in the parent PApplet regardless of the input.
     *
     * @return A DoubleUnaryOperator that always returns the previous y position of the mouse in the parent PApplet.
     */
    public DoubleUnaryOperator pmouseY() {
        return (double pos) -> (double)parent.pmouseY;
    }

    /**
     * Returns a DoubleUnaryOperator which represents a constant value function with optional modification.
     * @param v Value operator.
     * @param m Boolean modifier flag. If true, the pos value will be modified before applying to value operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator constValue(DoubleUnaryOperator v, boolean m, DoubleUnaryOperator a, 
                                          DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            if (m) {
                pos = calcPos(pos, r, p);
                if (v != null) {
                    return ampBias(v.applyAsDouble(pos), a, b, pos);
                }
            }
            return v.applyAsDouble(pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a noise function with optional modification.
     * @param lo Lower bound operator.
     * @param hi Upper bound operator.
     * @param mode Mode operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator noise(DoubleUnaryOperator lo, DoubleUnaryOperator hi, 
                                     DoubleUnaryOperator mode, DoubleUnaryOperator a, 
                                     DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double hiVal = (hi != null) ? hi.applyAsDouble(pos) : pos;
            if (mode != null) {
                double modeVal = mode.applyAsDouble(pos);
                return ampBias(triDistribution((lo != null) ? lo.applyAsDouble(pos) : 0, hiVal, modeVal), a, b, pos);
            } else {
                double randFloat = (lo != null ? lo.applyAsDouble(pos) : 0) + parent.random((float)(hiVal - (lo != null ? lo.applyAsDouble(pos) : 0)));
                return ampBias(randFloat, a, b, pos);
            }
        };
    }

    /**
     * Generates a DoubleUnaryOperator for Perlin noise. The noise value is determined by applying the
     * supplied DoubleUnaryOperator parameters as x, y, and z coordinates in the Perlin noise space.
     *
     * @param x A DoubleUnaryOperator used to compute the x-coordinate for the Perlin noise function.
     * @param y A DoubleUnaryOperator used to compute the y-coordinate for the Perlin noise function.
     * @param z A DoubleUnaryOperator used to compute the z-coordinate for the Perlin noise function.
     * @return A DoubleUnaryOperator that represents Perlin noise over the 3-dimensional space defined by the input functions.
     */
    public DoubleUnaryOperator perlin(DoubleUnaryOperator x, DoubleUnaryOperator y, DoubleUnaryOperator z) {
        return pos -> {
            double xV = x.applyAsDouble(pos);
            if (y == null) {
                return parent.noise((float) xV);
            }
            double yV = y.applyAsDouble(pos);
            if (z == null) {
                return parent.noise((float) xV, (float) yV);
            }
            double zV = z.applyAsDouble(pos);
            return parent.noise((float) xV, (float) yV, (float) zV);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a ramp function with optional modification.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator ramp(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias(pos, a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a sawtooth waveform function.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator saw(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(1 - pos, r, p);
            return ampBias(pos, a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a triangular waveform function.
     * @param s Shape operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator tri(DoubleUnaryOperator s, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double sValue = s.applyAsDouble(pos);
            double value = pos < sValue ? pos / sValue : 1 - ((pos - sValue) / (1 - sValue));
            return ampBias(value, a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a sine waveform function.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator sine(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.sin(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents a pulse waveform function.
     * @param w Width operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator pulse(DoubleUnaryOperator w, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double wValue = w.applyAsDouble(pos);
            double value = pos < wValue ? 0 : 1;
            return ampBias(value, a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents an ease-in curve function.
     * @param e Ease operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator easeIn(DoubleUnaryOperator e, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double eValue = e.applyAsDouble(pos);
            return ampBias(Math.pow(pos, eValue), a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents an ease-out curve function.
     * @param e Ease operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator easeOut(DoubleUnaryOperator e, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double eValue = e.applyAsDouble(pos);
            return ampBias((1 - Math.pow((1 - pos), eValue)), a, b, pos);
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents an ease-in-out curve function.
     * @param e Ease operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator easeInOut(DoubleUnaryOperator e, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double value = pos * 2;
            double eValue = e.applyAsDouble(pos);
            if (value < 1) {
                return ampBias(0.5 * Math.pow(value, eValue), a, b, pos);
            } else {
                return ampBias(0.5 * (2 - Math.pow((2 - value), eValue)), a, b, pos);
            }
        };
    }

    /**
     * Returns a DoubleUnaryOperator which represents an ease-out-in curve function.
     * @param e Ease operator.
     * @param a Amplifier operator.
     * @param r Rate operator.
     * @param p Phase operator.
     * @param b Bias operator.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator easeOutIn(DoubleUnaryOperator e, DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            double eValue = e.applyAsDouble(pos);
            double value = (pos * 2) - 1;
            if (value < 0) {
                return ampBias(0.5 * Math.pow(value, eValue) + 0.5, a, b, pos);
            } else {
                return ampBias(1 - (0.5 * Math.pow(value, eValue) + 0.5), a, b, pos);
            }
        };
    }

    /**
     * Creates a cosine wave function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator cos(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.cos(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates a tangent wave function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator tan(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.tan(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates a hyperbolic sine function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator sinh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.sinh(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates a hyperbolic cosine function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator cosh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.cosh(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates a hyperbolic tangent function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator tanh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.tanh(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates an inverse cosine function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator acos(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.acos(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates an inverse tangent function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator atan(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.atan(pos2Rad(pos)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates an inverse hyperbolic sine function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator asinh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.log(pos + Math.sqrt(pos*pos + 1)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates an inverse hyperbolic cosine function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator acosh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias((Math.log(pos + Math.sqrt(pos*pos - 1)) * 0.5) + 0.5, a, b, pos);
        };
    }

    /**
     * Creates an inverse hyperbolic tangent function.
     * 
     * @param a amplitude function
     * @param r rate function
     * @param p phase function
     * @param b bias function
     * @return a DoubleUnaryOperator representing the function
     */
    public DoubleUnaryOperator atanh(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias(0.5 * Math.log((1 + pos) / (1 - pos)), a, b, pos);
        };
    }

    /**
     * Creates a DoubleUnaryOperator representing the natural logarithm function (ln).
     *
     * @param a the amplitude as a DoubleUnaryOperator.
     * @param r the rate as a DoubleUnaryOperator.
     * @param p the phase as a DoubleUnaryOperator.
     * @param b the bias as a DoubleUnaryOperator.
     * @return a DoubleUnaryOperator representing the natural logarithm function.
     */
    public DoubleUnaryOperator ln(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias(Math.log(pos), a, b, pos);
        };
    }

    /**
     * Creates a DoubleUnaryOperator representing the exponential function (exp).
     *
     * @param a the amplitude as a DoubleUnaryOperator.
     * @param r the rate as a DoubleUnaryOperator.
     * @param p the phase as a DoubleUnaryOperator.
     * @param b the bias as a DoubleUnaryOperator.
     * @return a DoubleUnaryOperator representing the exponential function.
     */
    public DoubleUnaryOperator exp(DoubleUnaryOperator a, DoubleUnaryOperator r, DoubleUnaryOperator p, DoubleUnaryOperator b) {
        return pos -> {
            pos = calcPos(pos, r, p);
            return ampBias(Math.exp(pos), a, b, pos);
        };
    }


    /**
     * Normalizes the given array of values.
     * @param values Array of values to normalize.
     * @return Array of normalized values.
     */
    public double[] normalize(double[] values) {
        double min = Arrays.stream(values).min().getAsDouble();
        double max = Arrays.stream(values).max().getAsDouble();
        return Arrays.stream(values).map(v -> (v - min) / (max - min)).toArray();
    }

    /**
     * Returns a DoubleUnaryOperator which interpolates between given y-values at given position.
     * @param yvalues Array of y-values.
     * @return DoubleUnaryOperator representing the described function.
     */
    public DoubleUnaryOperator timeseries(double[] yvalues) {
    	double[] normalizedYvalues = normalize(yvalues);
        return pos -> {
            int index = (int) (pos * (normalizedYvalues.length - 1));
            double fraction = pos * (normalizedYvalues.length - 1) - index;
            return (normalizedYvalues[index] * (1.0 - fraction)) + (normalizedYvalues[index + 1] * fraction);
        };
    }

    /**
     * Transforms a given function into a table of values.
     * @param f Function to transform.
     * @param numSamples Number of samples to use for transformation.
     * @param mapFunc Mapping function to apply to each value.
     * @return Array of transformed values.
     */
    public double[] toTable(DoubleUnaryOperator f, int numSamples, DoubleUnaryOperator mapFunc) {
        return DoubleStream.iterate(0, i -> i + 1.0 / numSamples).limit(numSamples)
                .map(f).map(mapFunc).toArray();
    }
    
    /**
     * Generates an array of points that are distributed along the provided curve.
     *
     * The curve is represented by the given DoubleUnaryOperator function.
     * The points are spaced equally along the x-axis between the provided start and end values.
     * The total number of points generated is specified by the numPoints parameter.
     *
     * @param curve a DoubleUnaryOperator function representing the curve.
     * @param start the x-coordinate of the first point.
     * @param end the x-coordinate of the last point.
     * @param numPoints the total number of points to generate.
     * @return an array of PVector objects representing the points along the curve.
     */
    public PVector[] distributePoints(DoubleUnaryOperator curve, float start, float end, int numPoints) {
        PVector[] points = new PVector[numPoints];
        float step = (end - start) / (numPoints - 1);

        for(int i = 0; i < numPoints; i++) {
            float x = start + i * step;
            float y = (float) curve.applyAsDouble(x / end);
            points[i] = new PVector(x, y);
        }

        return points;
    }

    /**
     * Plots the curve of the provided function on the parent PApplet instance.
     * The function is evaluated across the width of the sketch, and the result is scaled and drawn as a curve.
     *
     * @param func The function to plot. This should be a DoubleUnaryOperator which takes a single argument in the range [0, 1] 
     *             and returns a single value, which will be plotted on the y-axis.
     * @param yscale The vertical scale factor to apply to the output of the function. This controls the height of the curve on the sketch.
     */
    public void plot(DoubleUnaryOperator func, float yscale) {
        parent.stroke(255, 0, 0); // Set line color to red
        parent.beginShape();
        for (int x = 0; x < parent.width; x++) {
            float t = (float)x / parent.width;  // Normalize x to range [0, 1]
            float y = (float)func.applyAsDouble(t); // Evaluate the function at t
            parent.vertex(x, parent.height - y*yscale); // Add a vertex at (x, y). Flip y to match Processing's coordinate system
        }
        parent.endShape();
    }

}
