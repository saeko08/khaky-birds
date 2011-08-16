/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.engine.math.Vector3;

/**
 * Provides the bird with positions of the cables it can jump.
 * 
 * @author azagor
 *
 */
public interface CableProvider 
{
	/**
	 * Returns the x position on the specified cable
	 * 
	 * @param cableIdx
	 * @param y
	 * @return
	 */
	float getPositionOnCable( int cableIdx, float y );

	/**
	 * Returns the index of a cable to the left
	 * 
	 * @param cableIdx
	 * @return
	 */
	int getLeftCable( int cableIdx );
	
	/**
	 * Returns the index of a cable to the right
	 * 
	 * @param cableIdx
	 * @return
	 */
	int getRightCable(int cableIdx);

	/**
	 * Returns a cable closest to the specified position.
	 *  
	 * @param position
	 * @return
	 */
	int getNearestCableIdx(Vector3 position);
}
