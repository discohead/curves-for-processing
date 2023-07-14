package crvs;

import processing.core.PApplet;
import processing.core.PGraphics;

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
	 * @param parent
	 * @param window
	 * @param xCrv
	 * @param yCrv
	 */
	public Lsjs(PApplet parent, PGraphics window, Crv xCrv, Crv yCrv) {
		super(parent, window);
		this.xCrv = xCrv;
		this.yCrv = yCrv;
	}

	/**
	 * @param c
	 * @param pos
	 * @return
	 */
	public float componentAt(Component c, float pos) {
		pos = this.calcPos(pos);
		float value;
		switch(c) {
		case X:
			value = this.xCrv.yAt(pos);
			break;
		case Y:
		default:
			value = this.yCrv.yAt(pos);
			break;
		}
		value = this.bipolarize(value);
		value = this.ampBias(value, pos);
		return this.rectify(value);
	}

}
