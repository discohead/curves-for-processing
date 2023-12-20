package crvs;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.Objects;

/**
 * Class to render two Crvs as a Lissajous figure
 */
public class Lsjs extends Crv {

	/**
	 * Crv to calculate x component
	 */
	public Crv xCrv;
	/**
	 * Crv to calculate y component
	 */
	public Crv yCrv;

	/**
	 * The Krnl.
	 */
	public VctrOp vctrOp;

	/**
	 * Instantiates a new Lsjs.
	 *
	 * @param parent the parent
	 * @param window the window
	 * @param xCrv   the x crv
	 * @param yCrv   the y crv
	 * @param VctrOp   the krnl
	 */
	public Lsjs(PApplet parent, Window window, Crv xCrv, Crv yCrv, VctrOp VctrOp) {
		super(parent, window);
		this.xCrv = xCrv;
		this.yCrv = yCrv;
		if (VctrOp == null) {
			this.vctrOp = (p) -> {
				PVector np = new PVector();
				np.x = this.componentAt(Component.X, p.x);
				np.y = this.componentAt(Component.Y, p.y);
				return np;
			};
		} else {
			this.vctrOp = VctrOp;
		}
	}

	/**
	 * Instantiates a new Lsjs.
	 *
	 * @param parent the parent
	 * @param window the window
	 * @param xCrv   the x crv
	 * @param yCrv   the y crv
	 */
	public Lsjs(PApplet parent, Window window, Crv xCrv, Crv yCrv) {
		this(parent, window, xCrv, yCrv, null);
	}

	/**
	 * Component at float.
	 *
	 * @param c   the c
	 * @param pos the pos
	 * @return float
	 */
	public float componentAt(Component c, float pos) {
		pos = this.calcPos(pos);
		float value;
		if (Objects.requireNonNull(c) == Component.X) {
			PVector xV = this.xCrv.uVector(pos);
			value = xV.y;
		} else {
			PVector yV = this.yCrv.uVector(pos);
			value = this.quantize(yV.y);
		}
		value = this.bipolarize(value);
		value = this.ampBias(value, pos);
		return value;
	}

	/**
	 * Convolve p vector.
	 *
	 * @param point the point
	 * @return the p vector
	 */
	public PVector convolve(PVector point) {
		return this.vctrOp.apply(point);
	}

	/**
	 * Convolve p vector [ ].
	 *
	 * @param points the points
	 * @return the p vector [ ]
	 */
	public PVector[] convolve(PVector[] points) {
		PVector[] newPoints = new PVector[points.length];
		for (int i = 0; i < points.length; i++) {
			newPoints[i] = this.convolve(points[i]);
		}
		return newPoints;
	}

}
