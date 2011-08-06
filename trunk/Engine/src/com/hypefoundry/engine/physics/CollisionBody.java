/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.world.Entity;

/**
 * This is the simplest representation of a static physical body
 * that just sits there and is interested in collision detection
 * notifications.
 * 
 * @author Paksas
 *
 */
public class CollisionBody extends PhysicalBody 
{

	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public CollisionBody( Entity entity ) 
	{
		super(entity);
	}

	@Override
	public void respondToCollision( PhysicalBody collider )
	{
		// nothing to do here
	}
}
