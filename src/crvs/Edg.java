package crvs;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.HALF_PI;

/**
 * The type Edg.
 */
@SuppressWarnings("unused")
public class Edg {

	/**
	 * The Source.
	 */
	public PVector source;

	/**
	 * The Target.
	 */
	public PVector target;

	/**
	 * The Resolution.
	 */
	public int resolution;

	/**
	 * The current translation of the edg from its origin.
	 */
	public PVector translation = new PVector();

	/**
	 * The scale of the edg in x and y dimensions.
	 */
	public PVector scale = new PVector(1,1);

	/**
	 * The rotation of the edg in degrees.
	 */
	public float rotation;

	/**
	 * Instantiates a new Edg.
	 *
	 * @param edge        the edge
	 * @param resolution  the resolution
	 * @param translation the translation
	 * @param scale       the scale
	 * @param rotation    the rotation
	 */
	public Edg(PVector[] edge, int resolution, PVector translation, PVector scale, float rotation) {
		this.source = edge[0];
		this.target = edge[1];
		this.resolution = resolution;
		if (translation != null) {
			this.translation = translation;
		}
		if (scale != null) {
			this.scale = scale;
		}
		this.rotation = rotation;
	}

	/**
	 * Instantiates a new Edg.
	 *
	 * @param edge       the edge
	 * @param resolution the resolution
	 */
	public Edg(PVector[] edge, int resolution) {
		this(edge, resolution, null, null, 0);
	}

	/**
	 * Instantiates a new Edg.
	 *
	 * @param source     the source
	 * @param target     the target
	 * @param resolution the resolution
	 */
	public Edg(PVector source, PVector target, int resolution) {
        this(new PVector[] { source, target }, resolution, null, null, 0);
    }

	/**
	 * Instantiates a new Edg.
	 *
	 * @param edge       the edge
	 * @param resolution the resolution
	 */
	public Edg(float[] edge, int resolution) {
    	this(new PVector[] { new PVector(edge[0], edge[1]), new PVector(edge[2], edge[3])}, resolution, null, null, 0);
    }


	/**
	 * To floats float [ ].
	 *
	 * @return the float [ ]
	 */
	public float[] toFloats() {
    	float[] fa = new float[4];
    	fa[0] = this.source.x;
    	fa[1] = this.source.y;
    	fa[2] = this.target.x;
    	fa[3] = this.target.y;
    	return fa;
    }

	/**
	 * Length float.
	 *
	 * @return the float
	 */
	public float length() {
        return this.source.dist(this.target);
    }

	/**
	 * At p vector.
	 *
	 * @param pos the pos
	 * @return the p vector
	 */
	public PVector at(float pos) {
    	PVector point = new PVector();
    	point.x = PApplet.map(pos, 0, 1, this.source.x, this.target.x);
    	point.y = PApplet.map(pos, 0, 1, this.source.y, this.target.y);
    	return point;
    }

	/**
	 * Midpoint p vector.
	 *
	 * @return the p vector
	 */
	public PVector midpoint() {
    	return this.at(0.5f);
    }

	/**
	 * Copy edg.
	 *
	 * @return the edg
	 */
	public Edg copy() {
    	return new Edg(
    			new PVector[] { this.source.copy(), this.target.copy() }, 
    			this.resolution, 
    			this.translation, 
    			this.scale, 
    			this.rotation
			);
    }

	/**
	 * Transformed edg.
	 *
	 * @return the edg
	 */
	public Edg transformed() {
		PVector src = this.source.copy();
		PVector trgt = this.target.copy();
		PVector[] nodes = new PVector[] { src, trgt };
		PVector[] tNodes = Utils.transform(nodes, this.midpoint(), this.scale, this.translation, this.rotation);
		return new Edg(tNodes, this.resolution);
	}

	/**
	 * Points p vector [ ].
	 *
	 * @param numPoints the num points
	 * @return the p vector [ ]
	 */
	public PVector[] points(int numPoints) {
    	PVector[] samples = new PVector[numPoints];
    	for (int i = 0; i < numPoints; i++) {
    		float x = (float) i / numPoints;
    		samples[i] = this.at(x);
    	}
    	return samples;
    }

	/**
	 * Points p vector [ ].
	 *
	 * @return the p vector [ ]
	 */
	public PVector[] points() {
    	return this.points(this.resolution);
    }

	/**
	 * Points list array list.
	 *
	 * @param numPoints the num points
	 * @return the array list
	 */
	public ArrayList<PVector> pointsList(int numPoints) {
		return Utils.va2vl(this.points(numPoints));
    }

	/**
	 * Angle float.
	 *
	 * @return the float
	 */
	public float angle() {
		float dy = target.y - source.y;
		float dx = target.x - source.x;
		return (float) Math.atan2(-dy, dx);
	}

	/**
	 * Gets perpendicular point.
	 *
	 * @param pointOnEdge the point on edge
	 * @param magnitude   the magnitude
	 * @return the perpendicular point
	 */
	public PVector getPerpendicularPoint(PVector pointOnEdge, float magnitude) {
		// Calculate the direction vector of the edge
		PVector dir = this.asVector();
		dir.normalize();

		// Rotate by 90 degrees to get the perpendicular direction
		dir.rotate(HALF_PI);

		// Scale by the desired magnitude
		dir.mult(magnitude);

		// Calculate the position of the new point

		return PVector.add(pointOnEdge, dir);
	}

	/**
	 * Get crv points p vector [ ].
	 *
	 * @param crv        the crv
	 * @param resolution the resolution
	 * @return the p vector [ ]
	 */
	public PVector[] getCrvPoints(Crv crv, int resolution) {
		PVector[] edgPoints = this.points(resolution);
		PVector[] crvPoints = new PVector[resolution];
		for (int i = 0; i < resolution; i++) {
			float x = i / (resolution - 1f);
			float mag = crv.yAt(x);
			crvPoints[i] = this.getPerpendicularPoint(edgPoints[i], mag);
		}
		return crvPoints;
	}

	/**
	 * Get crv points p vector [ ].
	 *
	 * @param crv the crv
	 * @return the p vector [ ]
	 */
	public PVector[] getCrvPoints(Crv crv) {
		return this.getCrvPoints(crv, this.resolution);
	}

	/**
	 * As vector p vector.
	 *
	 * @return the p vector
	 */
	public PVector asVector() {
		// Create a new PVector that points from the source to the target

		return PVector.sub(this.target, this.source);
	}
}