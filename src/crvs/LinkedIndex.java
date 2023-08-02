package crvs;

import processing.core.PApplet;

/**
 * The type Linked index.
 */
public class LinkedIndex {

    /**
     * The Array.
     */
    LinkedArray array;
    /**
     * The Index.
     */
    int index;
    /**
     * The Links.
     */
    int[] links;
    /**
     * The Link count.
     */
    int linkCount;

    /**
     * Instantiates a new Linked index.
     *
     * @param a the a
     * @param i the
     */
    public LinkedIndex(LinkedArray a, int i){
		array = a;
		index = i;
		links = new int[1];
		linkCount = 0;
	}

    /**
     * Link to.
     *
     * @param i the
     */
    public void linkTo(int i){
		if( links.length == linkCount )
			links = PApplet.expand(links);
		links[linkCount++] = i;
	}

    /**
     * Linked boolean.
     *
     * @param i the
     * @return the boolean
     */
    public boolean linked(int i){
		for(int j=0; j<linkCount; j++)
			if(links[j]==i)
				return true;
		return false;
	}

    /**
     * Get links int [ ].
     *
     * @return the int [ ]
     */
    public int[] getLinks(){
		return links;
	}

}