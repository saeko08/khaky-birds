/**
 * 
 */
package com.hypefoundry.engine.math;

/**
 * A bounding shape interface.
 * 
 * @author paksas
 *
 */
public interface BoundingShape 
{
	/**
	 * Checks if the shape overlaps a sphere.
	 * 
	 * @param sphere
	 * @return
	 */
	boolean doesOverlap( BoundingSphere sphere );

	/**
	 * Checks if the shape overlaps a box.
	 * 
	 * @param box
	 * @return
	 */
	boolean doesOverlap( BoundingBox box );
	
	/**
	 * Checks if the shape overlaps a point.
	 * 
	 * @param point
	 * @return
	 */
	boolean doesOverlap( Vector3 point );
}
