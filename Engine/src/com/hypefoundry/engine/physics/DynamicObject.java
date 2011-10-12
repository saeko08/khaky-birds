/**
 * 
 */
package com.hypefoundry.engine.physics;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
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
	public float					m_linearSpeed;
	public float					m_rotationSpeed;
	
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
			m_rotation = ( m_rotation > 0 ) ? rotMag : -rotMag;
		}
	}
	
	/**
	 * Calculates the maximum rotation the body can perform this frame.
	 * 
	 * @param rotationAngle
	 * @param deltaTime
	 * @return
	 */
	public final float getRotationPerFrame( float rotationAngle, float deltaTime ) 
	{
		float rotMag = Math.abs( rotationAngle );
		float maxRotThisFrame = m_rotationSpeed * deltaTime;
		if ( rotMag > maxRotThisFrame )
		{
			rotationAngle = ( rotationAngle > 0 ) ? maxRotThisFrame : -maxRotThisFrame;
		}
		
		return rotationAngle / deltaTime;
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
		
		// slow down
		m_velocity.set( 0, 0, 0 );
		m_rotation = 0;
	}

	@Override
	public void load( DataLoader loader )
	{
		DataLoader node = loader.getChild( "DynamicObject" );
		if ( node == null )
		{
			// parent node doesn't contain the description of this shape
			return;
		}
			
		m_velocity.load( "velocity", node );
		m_rotation = node.getFloatValue( "rotation" );	
	}
	
	@Override
	public void save( DataSaver saver )
	{
		DataSaver node = saver.addChild( "DynamicObject" );
			
		m_velocity.save( "velocity", node );
		node.setFloatValue( "rotation", m_rotation );	
	}

}
