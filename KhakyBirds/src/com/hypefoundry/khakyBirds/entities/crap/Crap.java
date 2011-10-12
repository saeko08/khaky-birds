package com.hypefoundry.khakyBirds.entities.crap;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;

/**
 * Crap a bird makes.
 * 
 * @author azagor
 *
 */
public class Crap extends Entity 
{
	public boolean   pedestrianHit           = false;
	public boolean    m_canHit				 = false;	
	//private Camera2D m_camera;
	
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
	public Crap()
	{
		this( 0, 0 );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Crap( float x, float y )
	{
		setPosition( x, y, 80 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 15f ) );	// TODO: config
		
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
