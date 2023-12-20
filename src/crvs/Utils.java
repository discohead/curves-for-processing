package crvs;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * The type Utils.
 */
@SuppressWarnings("unused")
public class Utils {

	/**
	 * Scale 2 trans float.
	 *
	 * @param scale the scale
	 * @return the float
	 */
	public static float scale2trans(float scale) {
		return (1f-scale)/2f;
	}

	/**
	 * V 2 f float [ ] [ ].
	 *
	 * @param vectors the vectors
	 * @return the float [ ] [ ]
	 */
	public static float[][] v2f(PVector[] vectors) {
		float[][] points = new float[vectors.length][2];
		for (int i = 0; i < vectors.length; i++) {
			points[i] = vectors[i].array();
		}
		return points;
	}

	/**
	 * F 2 v p vector [ ].
	 *
	 * @param points the points
	 * @return the p vector [ ]
	 */
	public static PVector[] f2v(float[][] points) {
		PVector[] vectors = new PVector[points.length];
		for (int i = 0; i < points.length; i++) {
			vectors[i] = new PVector(points[i][0], points[i][1]);
		}
		return vectors;
	}

	/**
	 * F 2 e edg [ ].
	 *
	 * @param edges      the edges
	 * @param resolution the resolution
	 * @return the edg [ ]
	 */
	public static Edg[] f2e(float[][] edges, int resolution) {
		Edg[] edgs = new Edg[edges.length];
		for (int i = 0; i < edges.length; i++) {
			edgs[i] = new Edg(edges[i], resolution);
		}
		return edgs;
	}

	/**
	 * E 2 f float [ ] [ ].
	 *
	 * @param edgs the edgs
	 * @return the float [ ] [ ]
	 */
	public static float[][] e2f(Edg[] edgs) {
		float[][] edges = new float[edgs.length][4];
		for (int i = 0; i < edgs.length; i++) {
			edges[i] = edgs[i].toFloats();
		}
		return edges;
	}

	/**
	 * Va 2 vl array list.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<PVector> va2vl(PVector[] array) {
		return new ArrayList<>(Arrays.asList(array));
	}

	/**
	 * Convert List of Vectors to Array of Vectors.
	 *
	 * @param vectorList the vector list
	 * @return the p vector [ ]
	 */
	public static PVector[] vl2va(ArrayList<PVector> vectorList) {
		return vectorList.toArray(new PVector[0]);
	}

	/**
	 * Transforms the given PVector according to the curve's scale, rotation, and
	 * translation.
	 *
	 * @param vector          The PVector to transform.
	 * @param center          the center
	 * @param scale           the scale
	 * @param translation     the translation
	 * @param rotationDegrees the rotation degrees
	 * @return The transformed PVector.
	 */
	public static PVector transform(PVector vector, PVector center, PVector scale, PVector translation, float rotationDegrees) {
		if (rotationDegrees != 0) {
			vector.sub(center);
			vector.rotate(PApplet.radians(rotationDegrees));
			vector.add(center);
		}
		if (scale != null) {
			vector.x *= scale.x;
			vector.y *= scale.y;
		}
		if (translation != null) {
			vector.add(translation);
		}
		return vector;
	}

	/**
	 * Transforms an array of PVectors according to the scale, rotation, and
	 * translation.
	 *
	 * @param vectors         The array of PVectors to transform.
	 * @param center          the center
	 * @param scale           the scale
	 * @param translation     the translation
	 * @param rotationDegrees the rotation degrees
	 * @return The array of transformed PVectors.
	 */
	public static PVector[] transform(PVector[] vectors, PVector center, PVector scale, PVector translation, float rotationDegrees) {
		PVector[] newVectors = new PVector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			PVector p = vectors[i];
			newVectors[i] = Utils.transform(p, center, scale, translation, rotationDegrees);
		}
		return newVectors;
	}

	/**
	 * Clipped p vector.
	 *
	 * @param v    the v
	 * @param xMin the x min
	 * @param xMax the x max
	 * @param yMin the y min
	 * @param yMax the y max
	 * @return the p vector
	 */
	public static PVector clipped(PVector v, float xMin, float xMax, float yMin, float yMax) {
		v.x = Math.max(xMin, Math.min(xMax, v.x));
		v.y = Math.max(yMin, Math.min(yMax, v.y));
		return v;
	}

	/**
	 * Inset window window.
	 *
	 * @param x                  the x
	 * @param y                  the y
	 * @param width              the width
	 * @param height             the height
	 * @param widthInsetPercent  the width inset percent
	 * @param heightInsetPercent the height inset percent
	 * @return the window
	 */
	public static Window insetWindow(
			int x,
			int y,
			int width,
			int height,
			float widthInsetPercent,
			float heightInsetPercent
	) {
		int widthPad = (int) (width * widthInsetPercent);
		int heightPad = (int) (height * heightInsetPercent);

		int insetX = x + widthPad/2;
		int insetY = y + heightPad/2;
		int insetWidth = width - widthPad;
		int insetHeight = height - heightPad;

		return new Window(insetX, insetY, insetWidth, insetHeight);
	}

}
