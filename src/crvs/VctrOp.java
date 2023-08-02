package crvs;

import processing.core.PVector;

/**
 * The interface Krnl.
 */
public interface VctrOp {
    /**
     * Apply p vector.
     *
     * @param point the point
     * @return the p vector
     */
    PVector apply(PVector point);
}
