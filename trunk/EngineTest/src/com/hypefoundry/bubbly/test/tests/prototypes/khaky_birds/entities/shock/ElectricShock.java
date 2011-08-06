package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;


/**
 * An electricity spike running through the electric cables
 * that may shock and eventually kill our bird.
 * 
 * @author paksas
 *
 */
public class ElectricShock extends Entity implements EntityEventListener
{

	/**
	 * Constructor.
	 * 
	 * @param cableYOffset
	 */
	public ElectricShock( float cableXOffset ) 
	{
		setPosition( cableXOffset, 0, 5 );
		
		// register events listeners
		attachEventListener( this );
	}

	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof CollisionEvent )
		{
			((CollisionEvent)event).m_collider.sendEvent( Shocked.class );
		}
	}
}
