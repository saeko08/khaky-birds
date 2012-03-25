/**
 * 
 */
package com.hypefoundry.kabloons.entities.exitDoor;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class ExitDoorPhysicalBody extends PhysicalBody 
{


	public ExitDoorPhysicalBody( Entity entity ) 
	{
		super( entity, ((ExitDoor)entity).getExitAreaBounds(), true );
	}

	@Override
	protected void respondToCollision( PhysicalBody collider, Vector3 collisionPoint)  
	{
		// do nothing
	}

}
