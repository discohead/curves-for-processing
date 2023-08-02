package crvs;

import processing.core.*;

/**
 * The type Int array.
 */
public class IntArray {

    /**
     * The Data.
     */
    int[] data;
    /**
     * The Length.
     */
    int length;

    /**
     * Instantiates a new Int array.
     */
    public IntArray(){
		this(1);
	}

    /**
     * Instantiates a new Int array.
     *
     * @param l the l
     */
    public IntArray( int l ){
		data = new int[l];
		length = 0;
	}

    /**
     * Add.
     *
     * @param d the d
     */
    public void add( int d ){
		if( length==data.length )
			data = PApplet.expand(data);
		data[length++] = d;
	}

    /**
     * Get int.
     *
     * @param i the
     * @return the int
     */
    public int get( int i ){
		return data[i];
	}

    /**
     * Contains boolean.
     *
     * @param d the d
     * @return the boolean
     */
    public boolean contains( int d ){
		for(int i=0; i<length; i++)
			if(data[i]==d)
				return true;
		return false;
	}

}
