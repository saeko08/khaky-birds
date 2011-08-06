/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.world.Aspect;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.Vector3;

/**
 * Describes an object with dynamic movement capabilities.
 * 
 * @author Paksas
 */
public final class DynamicObject implements Aspect
{
	// dynamics attributes
	public final float				m_linearSpeed;
	public final float				m_rotationSpeed;
	
	// state members
	public final Vector3 			m_velocity = new Vector3( 0, 0, 0 );
	public float					m_rotation;

	// runtime members
	private final Vector3			m_currTranslation = new Vector3( 0, 0, 0 );
	
	/**
	 * Constructor.
	 * 
	 * @param linearSpeed
	 */
	public DynamicObject( float linearSpeed, float rotationSpeed )
	{
		m_linearSpeed = linearSpeed;
		m_rotationSpeed = rotationSpeed;
	}
	
	/**
	 * Constrains the values to the max values.
	 */
	public void constrain()
	{
		m_velocity.clamp( m_linearSpeed );
		
		float rotMag = Math.abs( m_rotation );
		if ( rotMag > m_rotationSpeed )
		{
			m_rotation = Math.copySign( rotMag, m_rotation );
		}
	}
	
	/**
	 * Simulates the dynamics that influences the entity.
	 * 
	 * @param entity
	 */
	public void simulate( float deltaTime, Entity entity ) 
	{
		m_currTranslation.set( m_velocity );
		m_currTranslation.scale( deltaTime );
		entity.translate( m_currTranslation );
		entity.rotate( m_rotation * deltaTime );
	}
		
}
