/**
 * 
 */
package com.hypefoundry.kabloons.entities.fan;


import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class FanPhysicalBody extends PhysicalBody 
{

	/**
	 * Constructor.
	 * 
	 * @param buzzSawEntity
	 */
	public FanPhysicalBody( Entity fanEntity )
	{		
		super( fanEntity, ((Fan)fanEntity).getWindBounds(), true );
	}
	
	@Override
	protected void respondToCollision( PhysicalBody collider, Vector3 collisionPoint ) 
	{
		// do nothing
	}
}
