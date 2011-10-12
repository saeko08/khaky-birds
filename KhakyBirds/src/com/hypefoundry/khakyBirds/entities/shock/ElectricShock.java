package com.hypefoundry.khakyBirds.entities.shock;

import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.renderer2D.Spline;
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
	Spline		m_hostWire;
	float		m_offset;
	
	
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
		
		m_offset = 0;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param wire		wire on which the shock should run
	 */
	public ElectricShock( Spline wire ) 
	{
		// call the default constructor first to perform the generic initialization
		this();
			
		m_hostWire = wire;

		// custom initialization of the position and facing
		setPosition( m_hostWire.m_points[0].m_x, m_hostWire.m_points[0].m_y, 5 );
		setFacing( m_hostWire.m_directions[0].angleXY() );
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
