package crvs;

/**
 * FloatOp is a functional interface representing an operation on a single float operand
 * that produces a float result. This is a primitive specialization of java.util.function.Function for float.
 * 
 * This interface is used to pass functions as parameters, allowing for operations 
 * to be parameterized by different mathematical functions.
 *
 * @see java.util.function.Function
 */
public interface FloatOp {
	
    /**
     * Applies this function to the given operand.
     *
     * @param operand The operand on which the function will be applied.
     * @return The function result as a float.
     */
	float apply(float operand);
}
