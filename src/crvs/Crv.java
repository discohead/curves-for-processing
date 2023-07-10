package crvs;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;


public class Crv {
	PApplet parent;
	
	public FloatOp op;
	
	public Crv amp;
	public Crv rate;
	public Crv phase;
	public Crv bias;
	
	public float ampOffset = 1.0f;
	public float rateOffset = 1.0f;
	public float phaseOffset;
	public float biasOffset;
	
	public float randomScale;
	public float noiseScale;
	
	public int resolution;
	public int color;
	public boolean fill;
	
	public PGraphics window;
	public PVector anchor;
	public PVector translation;
	public float rotation;
	
	public Crv(PApplet parent, PGraphics window, FloatOp op) {
		this.parent = parent;
		if (window == null) {
			this.window = parent.createGraphics(parent.width, parent.height);
		} else {
			this.window = window;
		}
		this.resolution = (int) parent.width;
		this.anchor = new PVector(parent.width/2f, parent.height/2f);
		this.translation = new PVector(0, 0);
		if (op == null) {
			this.op = pos -> pos;
		} else {
			this.op = op;
		}
	}
	
	public Crv(PApplet parent) {
		this(parent, null, null);
	}
	
	public Crv(PApplet parent, FloatOp op) {
		this(parent, null, op);
	}
	
	public Crv(PApplet parent, Crv amp, Crv rate, Crv phase, Crv bias) {
		this(parent);
		this.amp = amp;
		this.rate = rate;
		this.phase = phase;
		this.bias = bias;
	}
	
	public Crv(PApplet parent, float ampOffset, float rateOffset, float phaseOffset, float biasOffset) {
		this(parent);
		this.ampOffset = ampOffset;
		this.rateOffset = rateOffset;
		this.phaseOffset = phaseOffset;
		this.biasOffset = biasOffset;
	}
	
	public Crv(PApplet parent, Crv amp, Crv rate, Crv phase, Crv bias, float ampOffset, float rateOffset, float phaseOffset, float biasOffset) {
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
	
	protected float calculate(float pos) {
		return this.op.apply(pos);
	};
	
	public float ampBias(float value, float pos) {
		if (this.amp != null) {
			value *= this.amp.uAt(pos);
		}
		if (this.bias != null) {
			value += this.bias.uAt(pos);
		}
		return value * this.ampOffset + this.biasOffset;
	}
	
	public float uAt(float pos) {
		pos = this.calcPos(pos);
		float value = this.calculate(pos);
		return this.ampBias(this.fuzz(value, pos), pos);
	}
	
	public PVector wAt(float pos) {
		float value = this.uAt(pos);
		return windowed(pos, value);
	}
	
	private PVector windowed(float pos, float value) {
		float xW = pos * this.window.width;
		float yW = value * this.window.height;
		return new PVector(xW, yW);
	}
	
    /**
     * Clamps the position value between 0 and 1.
     * @param pos Position value to be clamped.
     * @return Clamped position value.
     */
    private float clamp(float pos) {
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
     * Calculates a position value based on input rate and phase.
     * @param pos Original position.
     * @param rate Rate operator to adjust the position.
     * @param phase Phase operator to adjust the position.
     * @return Calculated position.
     */
    private float calcPos(float pos) {
    	pos = Math.abs(pos);
    	pos = pos * this.rateOffset;
    	if (pos > 1.0f) pos = pos % 1.0f;
        if (this.rate != null) {
            pos = pos * this.rate.uAt(pos);
        }
        if (this.phase != null) {
            pos = (pos + this.phase.uAt(pos));
        }
    	pos = pos + this.phaseOffset;
    	if (pos > 1.0f) return pos % 1.0f;
        return pos;
    }
    
    private float fuzz(float value, float pos) {
    	if (this.noiseScale != 0.0f) {
    		float n = this.parent.noise(pos) * this.noiseScale;
    		if (value + n > 1.0f) {
    			value = value - n;
    		} else {
    			value = value + n;
    		}
    	}
    	if (this.randomScale != 0.0f) {
    		float r = this.parent.random(1.0f) * this.randomScale;
    		if (value + r > 1.0f) {
    			value = value - r;
    		} else {
    			value = value + r;
    		}
    	}
    	if (value > 1.0f) return value % 1.0f;
    	return value;
    }
    
    /**
     * Transforms a given function into a table of values.
     * @param f Function to transform.
     * @param numSamples Number of samples to use for transformation.
     * @return Array of values.
     */
    public float[] toTable(int numSamples) {
    	float step = 1f / numSamples;
    	float[] table = new float[numSamples];
    	for (int i=0; i<numSamples; i++) {
    		float x = i * step;
    		table[i] = this.uAt(x);
    	}
    	return table;
    }
    
    /**
     * Generates an array of PVectors that are distributed along the provided curve.
     *
     * The points are spaced equally along the x-axis between the provided start and end values.
     * The total number of points generated is specified by the numPoints parameter.
     *
     * @param start the x-coordinate of the first point.
     * @param end the x-coordinate of the last point.
     * @param numPoints the total number of points to generate.
     * @return an array of PVector objects representing the points along the curve.
     */
    public PVector[] points(float start, float end) {
        PVector[] points = new PVector[this.resolution];
        float step = (end - start) / this.resolution;
        end = end - (step - 1);

        for(int i = 0; i < this.resolution; i++) {
            float x = start + i * step;
            points[i] = this.wAt(x / end);
        }

        return points;
    }
    
    
    public PVector[] points() {
    	return points(0f, this.window.width);
    }
    
    /**
     * Plots the curve of the provided function on the parent PApplet instance in the window.
     * The function is evaluated across the width of the window.
     */
    public void draw() {
    	this.parent.push();
    	if (fill) {
    		this.window.noStroke();
    		this.window.fill(this.color);
    	} else {
    		this.window.noFill();
    		this.window.stroke(this.color);
    	}
        PVector[] points = this.points();
        this.window.beginDraw();
        // translate in parent space
        this.parent.translate(this.translation.x, this.translation.y);
        // translate in window space for rotation
        this.parent.translate(this.anchor.x, this.anchor.y);
        //rotate
        this.parent.rotate(this.rotation);
        //draw
        this.window.beginShape();
        for (int i = 0; i < points.length; i++) {
        	PVector p = points[i];
        	this.window.vertex(p.x, this.window.height - p.y);
        }
        this.window.endShape();
        // translate back to desired parent space
        this.parent.translate(-this.anchor.x, -this.anchor.y);
        this.window.endDraw();
        // place window on canvas
        this.parent.image(this.window, 0, 0);
        this.parent.pop();
    }
	
}
