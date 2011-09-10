/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crappable;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.world.Entity;

/**
 * Hunter that tries to shoot the bird down.
 * 
 * @author azagor
 *
 */
public class Hunter extends Entity  implements Crappable
{
	enum State
	{
		Aiming,
		Shooting,
		Shitted
	}
	
	State		m_state;
	
	/**
	 * Serialization support constructor.
	 */
	public Hunter()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		setPosition( 0, 0, 80 );
		
		// add movement capabilities
		final float maxLinearSpeed = 0.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
		m_state = State.Aiming;

	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		String stateStr = loader.getStringValue( "state" );
		
		try
		{
			m_state = State.valueOf( State.class, stateStr );
		}
		catch( Exception ex )
		{
			m_state = State.Aiming;
		}
	}
	
	@Override
	public void onSave( DataSaver saver ) 
	{
		saver.setStringValue( "state", m_state.toString() );
	}
}
