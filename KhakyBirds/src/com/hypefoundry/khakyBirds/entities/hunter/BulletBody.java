/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hunter;

import com.hypefoundry.engine.math.Ray;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BulletBody extends PhysicalBody 
{

	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public BulletBody( Entity entity ) 
	{
		super( entity, new Ray(), true );
	}

	@Override
	protected void respondToCollision( PhysicalBody collider, Vector3 collisionPoint )
	{
		// nothing to do here
	}

}
