package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.physics.DynamicObject;
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
	 * Serialization support constructor.
	 */
	public ElectricShock() 
	{		
		// register events listeners
		attachEventListener( this );
						
		// add movement capabilities
		final float maxLinearSpeed = 1.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cableYOffset
	 */
	public ElectricShock( float cableXOffset ) 
	{
		// call the default constructor first to perform the generic initialization
		this();
			
		// custom initialization of the position
		setPosition( cableXOffset, 0, 5 );
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
