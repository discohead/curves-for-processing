package crvs;

import java.util.Random;

import processing.core.*;

/**
 * The type M polygon.
 */
@SuppressWarnings("unused")
public class MPolygon {

    /**
     * The Coords.
     */
    float[][] coords;
    /**
     * The Count.
     */
    int count;

    /**
     * Instantiates a new M polygon.
     */
    public MPolygon(){
		this(0);
	}

    /**
     * Instantiates a new M polygon.
     *
     * @param points the points
     */
    public MPolygon(int points){
		coords = new float[points][2];
		count = 0;
	}

    /**
     * Add.
     *
     * @param x the x
     * @param y the y
     */
    public void add(float x, float y){
		coords[count][0] = x;
		coords[count++][1] = y;
	}

    /**
     * Draw.
     *
     * @param p the p
     */
    public void draw(PApplet p){
		draw(p.g);
	}

    /**
     * Draw.
     *
     * @param g    the g
     * @param fill the fill
     */
    public void draw(PGraphics g, boolean fill) {
		if (!fill) {
			g.noFill();
		}
		g.beginShape();
		for(int i=0; i<count; i++){
			g.vertex(coords[i][0], coords[i][1]);
		}
		g.endShape(PApplet.CLOSE);
	}

    /**
     * Draw.
     *
     * @param g the g
     */
    public void draw(PGraphics g) {
		this.draw(g, false);
	}

    /**
     * Count int.
     *
     * @return the int
     */
    public int count(){
		return count;
	}

    /**
     * Get coords float [ ] [ ].
     *
     * @return the float [ ] [ ]
     */
    public float[][] getCoords() {
		return coords;
	}

    /**
     * Get vertices p vector [ ].
     *
     * @return the p vector [ ]
     */
    public PVector[] getVertices() {
		return Utils.f2v(this.coords);
	}

    /**
     * Contains boolean.
     *
     * @param px the px
     * @param py the py
     * @return the boolean
     */
    public boolean contains(float px, float py) {
	    float[][] polygon = this.getCoords();
	    boolean result = false;
	    for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
	        if ((polygon[i][1] > py) != (polygon[j][1] > py) &&
	            (px < (polygon[j][0] - polygon[i][0]) * (py - polygon[i][1]) / (polygon[j][1]-polygon[i][1]) + polygon[i][0])) {
	            result = !result;
	        }
	    }
	    return result;
	}

    /**
     * Get web edgs edg [ ].
     *
     * @param resolution the resolution
     * @return the edg [ ]
     */
    public Edg[] getWebEdgs(int resolution) {
		PVector[] points = this.getVertices();
		int size = points.length * (points.length - 1) / 2;
		Edg[] edgs = new Edg[size];
		int edgsIdx = 0;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < i; j++) {
				Edg edg = new Edg(points[i], points[j], resolution);
				edgs[edgsIdx] = edg;
				edgsIdx++;
			}
		}
		return edgs;
	}

    /**
     * Get web points p vector [ ].
     *
     * @param resolution the resolution
     * @return the p vector [ ]
     */
    public PVector[] getWebPoints(int resolution) {
		Edg[] edgs = this.getWebEdgs(resolution);
		PVector[] points = new PVector[edgs.length * resolution];
		int pointsIdx = 0;
		for (Edg edg : edgs) {
			PVector[] edgPoints = edg.points();
			for (PVector p : edgPoints) {
				points[pointsIdx] = p;
				pointsIdx++;
			}
		}
		return points;
	}

    /**
     * Get points within p vector [ ].
     *
     * @param numPoints the num points
     * @return the p vector [ ]
     */
    public PVector[] getPointsWithin(int numPoints) {
	    Random random = new Random();
	    PVector[] bbox = getBoundingBox();
	    PVector[] points = new PVector[numPoints];

	    for (int i = 0; i < numPoints; i++) {
	        float x, y;
	        do {
	            x = bbox[0].x + (bbox[1].x - bbox[0].x) * random.nextFloat();
	            y = bbox[0].y + (bbox[1].y - bbox[0].y) * random.nextFloat();
	        } while (!this.contains(x, y));
	        PVector p = new PVector();
	        p.x = x;
	        p.y = y;
	        points[i] = p;
	    }

	    return points;
	}

    /**
     * Get bounding box p vector [ ].
     *
     * @return the p vector [ ]
     */
    public PVector[] getBoundingBox() {
	    float[][] coords = this.getCoords();
	    
	    float minX = Float.MAX_VALUE;
	    float minY = Float.MAX_VALUE;
	    float maxX = Float.MIN_VALUE;
	    float maxY = Float.MIN_VALUE;

		for (float[] coord : coords) {
			float x = coord[0];
			float y = coord[1];

			if (x < minX) minX = x;
			if (y < minY) minY = y;
			if (x > maxX) maxX = x;
			if (y > maxY) maxY = y;
		}
	    return new PVector[] {
	    		new PVector(minX, minY), // top right corner
	    		new PVector(maxX, maxY) // bottom left corner
	    };
	}
}