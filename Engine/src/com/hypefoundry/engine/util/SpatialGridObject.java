/**
 * 
 */
package com.hypefoundry.engine.util;

import com.hypefoundry.engine.math.BoundingBox;


/**
 * Object that can be placed in a spatial grid.
 * 
 * @author Paksas
 */
public interface SpatialGridObject 
{
	/**
	 * Returns object's bounding box ( not just any shape - we require a bounding box )in world space.
	 * 
	 * @return
	 */
	BoundingBox getBounds();
}

