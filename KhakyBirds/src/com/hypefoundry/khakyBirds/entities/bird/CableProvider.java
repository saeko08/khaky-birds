/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.bird;

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
	 * Returns the position on the specified cable
	 * 
	 * @param cableIdx
	 * @param currPos		position in the world we want to cast to a cable pos
	 * @param outPos		position on the cable
	 */
	void getPositionOnCable( int cableIdx, Vector3 currPos, Vector3 outPos );

	/**
	 * Returns a cable closest to the specified position.
	 *  
	 * @param position
	 * @return
	 */
	int getNearestCableIdx( Vector3 position );
}
