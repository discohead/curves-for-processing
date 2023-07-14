package crvs;

import java.util.Random;

import processing.core.*;

public class MPolygon {

	float[][] coords;
	int count;
	
	public MPolygon(){
		this(0);
	}

	public MPolygon(int points){
		coords = new float[points][2];
		count = 0;
	}

	public void add(float x, float y){
		coords[count][0] = x;
		coords[count++][1] = y;
	}

	public void draw(PApplet p){
		draw(p.g);
	}

	public void draw(PGraphics g){
		g.beginShape();
		for(int i=0; i<count; i++){
			g.vertex(coords[i][0], coords[i][1]);
		}
		g.endShape(PApplet.CLOSE);
	}

	public int count(){
		return count;
	}

	public float[][] getCoords(){
		return coords;
	}
	
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

	public float[][] getPointsWithin(int numPoints) {
	    Random random = new Random();
	    float[][] bbox = getBoundingBox();
	    float[][] points = new float[numPoints][2];

	    for (int i = 0; i < numPoints; i++) {
	        float x, y;
	        do {
	            x = bbox[0][0] + (bbox[1][0] - bbox[0][0]) * random.nextFloat();
	            y = bbox[0][1] + (bbox[1][1] - bbox[0][1]) * random.nextFloat();
	        } while (!this.contains(x, y));
	        points[i][0] = x;
	        points[i][1] = y;
	    }

	    return points;
	}



	
	public float[][] getBoundingBox() {
	    float[][] coords = this.getCoords();
	    
	    float minX = Float.MAX_VALUE;
	    float minY = Float.MAX_VALUE;
	    float maxX = Float.MIN_VALUE;
	    float maxY = Float.MIN_VALUE;

	    for(int i = 0; i < coords.length; i++){
	        float x = coords[i][0];
	        float y = coords[i][1];
	        
	        if(x < minX) minX = x;
	        if(y < minY) minY = y;
	        if(x > maxX) maxX = x;
	        if(y > maxY) maxY = y;
	    }
	    
	    // Return as an array of points defining the corners of the bounding box
	    return new float[][]{
	        {minX, minY}, // bottom left corner
	        {maxX, maxY}  // top right corner
	    };
	}
	
	public PGraphics getBoundingWindow(PApplet parent) {
	    float[][] bbox = this.getBoundingBox();

	    // Calculate the width and height of the bounding box
	    int width = Math.round(bbox[1][0] - bbox[0][0]);
	    int height = Math.round(bbox[1][1] - bbox[0][1]);

	    // Create a new PGraphics object
	    PGraphics pg = parent.createGraphics(width, height);

	    // Translate the coordinates so that the top left corner of the bounding box aligns with (0,0)
	    pg.beginDraw();
	    pg.translate(-bbox[0][0], -bbox[1][0]);

	    // Return the PGraphics object
	    return pg;
	}

}