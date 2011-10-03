/**
 * 
 */
package com.hypefoundry.engine.math;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;


/**
 * A bounding shape interface.
 * 
 * @author paksas
 *
 */
public interface BoundingShape 
{	
	/**
	 * Returns an axis aligned bounding box of the shape.
	 * 
	 * @param box
	 */
	void getBoundingBox( BoundingBox box );
	
	/**
	 * Creates a new shape and initializes it based on this shape's
	 * characteristics around the specified origin as if it was extruded
	 * in the specified direction.
	 * 
	 * @param origin
	 * @param direction
	 * @param outExtrudedShape
	 * @return
	 */
	BoundingShape extrude( Vector3 origin, Vector3 direction, BoundingShape outExtrudedShape );
	
	// ------------------------------------------------------------------------
	// Overlap tests
	// ------------------------------------------------------------------------
	
	/**
	 * Checks if the shape overlaps another shape.
	 * 
	 * @param shape
	 * @param outIntersectPos		pass a vector here if you want to find out about the intersection point
	 * @return
	 */
	boolean doesOverlap( BoundingShape shape, Vector3 outIntersectPos );
		
	/**
	 * Checks if the shape overlaps a sphere.
	 * 
	 * @param sphere
	 * @param outIntersectPos		pass a vector here if you want to find out about the intersection point
	 * @return
	 */
	boolean doesOverlap( BoundingSphere sphere, Vector3 outIntersectPos );

	/**
	 * Checks if the shape overlaps a box.
	 * 
	 * @param box
	 * @param outIntersectPos		pass a vector here if you want to find out about the intersection point
	 * @return
	 */
	boolean doesOverlap( BoundingBox box, Vector3 outIntersectPos );
	
	/**
	 * Checks if the shape overlaps a point.
	 * 
	 * @param point
	 * @param outIntersectPos		pass a vector here if you want to find out about the intersection point
	 * @return
	 */
	boolean doesOverlap( Vector3 point, Vector3 outIntersectPos );
	
	/**
	 * Checks if the shape overlaps a ray.
	 * 
	 * @param ray
	 * @param outIntersectPos		pass a vector here if you want to find out about the intersection point
	 * @return
	 */
	boolean doesOverlap( Ray ray, Vector3 outIntersectPos );
	
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	/**
	 * Configures the shape based on the config file node's contents.
	 * 
	 * @param id			id of the child node describing this shape
	 * @param parentNode	parent node in which to look for the data
	 */
	void load( String id, DataLoader laoder );
	
	/**
	 * Saves the shape cofiguration.
	 * 
	 * @param id			id of the child node describing this shape
	 * @param parentNode	parent node to which the configuration should be added
	 */
	void save( String id, DataSaver saver );
}
