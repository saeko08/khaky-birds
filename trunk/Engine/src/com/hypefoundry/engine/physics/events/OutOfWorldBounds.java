/**
 * 
 */
package com.hypefoundry.engine.physics.events;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.EntityEvent;

/**
 * Event emitted when entity wanders outside the world's boundaries.
 * 
 * @author Paksas
 *
 */
public class OutOfWorldBounds implements EntityEvent 
{
	public enum ExitSide
	{
		ES_X,
		ES_Y,
		ES_Z
	};
	
	public ExitSide		m_side;

	/**
	 * One of the things we will want to to when handling this
	 * event is to make the entities turn around so that they
	 * don't move outside the world.
	 * 
	 *  This method provides the easy way to do that - it calculates
	 *  a new direction vector relative to the current one that will
	 *  ensure that an entity will stay in the world's bounds.
	 * 
	 * @param targetDirection			output direction
	 * @param sourceDirection			input direction
	 */
	public void reflectVector( Vector3 targetDirection, Vector3 sourceDirection )
	{
		targetDirection.set( sourceDirection );
		switch( m_side )
		{
			case ES_X:
			{
				targetDirection.m_x = -sourceDirection.m_x;
				break;
			}
			case ES_Y:
			{
				targetDirection.m_y = -sourceDirection.m_y;
				break;
			}
			case ES_Z:
			{
				targetDirection.m_z = -sourceDirection.m_z;
				break;
			}
		}
	}
	
	@Override
	public void deserialize( DataLoader loader ) {}
}
	
