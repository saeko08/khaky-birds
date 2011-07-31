/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.game.Aspect;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.Vector3;

/**
 * Describes an object with dynamic movement capabilities.
 * 
 * @author Paksas
 */
public class DynamicObject implements Aspect
{
	// state members
	public final Vector3 			m_velocity = new Vector3( 0, 0, 0 );

	// runtime members
	private final Vector3			m_currTranslation = new Vector3( 0, 0, 0 );
	
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
	}
		
}
