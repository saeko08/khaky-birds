/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.crap;

import com.hypefoundry.khakyBirds.entities.crap.Crap.State;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;

/**
 * @author azagor
 *
 */
public class GranadeCrap extends Entity 
{
	public boolean   pedestrianHit           = false;
	public boolean    m_canHit				 = false;	
	
	public enum State
	{
		Falling,
		Hitting,
		Splat,
	};
	
	public State				m_state;
///////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor.
	 */
	public GranadeCrap()
	{
		this( 0, 0 );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public GranadeCrap( float x, float y )
	{
		setPosition( x, y, 70 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, 0.2f, 0.2f ) );	// TODO: config
		
		// add movement capabilities
		final float maxLinearSpeed = 5.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	public void canHit(boolean canHit)
	{
		if (canHit == true)
		{
			m_canHit = true;
		}
		else
		{
			m_canHit = false;
		}
	}
}