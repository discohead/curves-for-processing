package crvs;

/**
 * The type Linked array.
 */
public class LinkedArray {

    /**
     * The Array.
     */
    LinkedIndex[] array;

    /**
     * Instantiates a new Linked array.
     *
     * @param size the size
     */
    public LinkedArray(int size){
		array = new LinkedIndex[size];
		for(int i=0; i<array.length; i++)
			array[i] = new LinkedIndex(this, i);
	}

    /**
     * Get linked index.
     *
     * @param i the
     * @return the linked index
     */
    public LinkedIndex get(int i){
		return array[i];
	}

    /**
     * Link.
     *
     * @param a the a
     * @param b the b
     */
    public void link(int a, int b){
		array[a].linkTo( b );
		array[b].linkTo( a );
	}

    /**
     * Linked boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    public boolean linked(int a, int b){
		return array[a].linked(b);
	}

}