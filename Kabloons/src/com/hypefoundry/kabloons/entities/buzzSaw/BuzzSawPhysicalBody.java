/**
 * 
 */
package com.hypefoundry.kabloons.entities.buzzSaw;

import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BuzzSawPhysicalBody extends PhysicalBody 
{
	/**
	 * Constructor.
	 * 
	 * @param buzzSawEntity
	 */
	public BuzzSawPhysicalBody( Entity buzzSawEntity )
	{		
		super( buzzSawEntity, 
				new BoundingSphere( buzzSawEntity.getPosition().m_x, buzzSawEntity.getPosition().m_y, buzzSawEntity.getBoundingShape().getWidth() * 0.5f ),
				true );
	}
	
	@Override
	protected void respondToCollision( PhysicalBody collider, Vector3 collisionPoint ) 
	{
		// do nothing
	}

}
