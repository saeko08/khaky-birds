/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

/**
 * Provides the bird with positions of the cables it can jump.
 * 
 * @author azagor
 *
 */
public interface CableProvider 
{
	/**
	 * Returns the initial cable index
	 * 
	 * @return
	 */
	int getStartCableIdx();

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
}
