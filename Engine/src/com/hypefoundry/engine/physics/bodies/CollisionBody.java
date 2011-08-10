/**
 * 
 */
package com.hypefoundry.engine.physics.bodies;

import com.hypefoundry.engine.physics.PhysicalBody;
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
	 * @param entity				represented entity
	 * @param checkCollisions		checks collisions with other entities
	 */
	public CollisionBody( Entity entity, boolean checkCollisions ) 
	{
		super( entity, checkCollisions );
	}

	@Override
	public void respondToCollision( PhysicalBody collider )
	{
		// nothing to do here
	}
}
