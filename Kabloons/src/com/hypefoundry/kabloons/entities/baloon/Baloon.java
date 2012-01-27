/**
 * 
 */
package com.hypefoundry.kabloons.entities.baloon;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class Baloon extends Entity 
{
	public enum State
	{
		Flying,
		Dead,
		Safe
	};
	
	String 		m_floatingAnim;
	State		m_state;
	
	/**
	 * Initializes a baloon instance.
	 * 
	 * @param localBounds
	 * @param pos
	 * @param floatingAnim
	 */
	public void initialize( BoundingBox localBounds, Vector3 pos, String floatingAnim )
	{
		setBoundingBox( localBounds );
		setPosition( pos );
		
		m_floatingAnim = floatingAnim;
		
		m_state = State.Flying;
		
		// add movement capabilities
		final float maxLinearSpeed = 0.3f;
		final float maxRotationSpeed = 0.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}

	/**
	 * Tells if the baloon is still intact ( it wasn't burst ). 
	 * @return
	 */
	public boolean isAlive() 
	{
		return m_state != State.Dead;
	}

	/**
	 * Returns 'true' if the baloon flew out the safety door, 'false' if it's still in the level.
	 * @return
	 */
	public boolean isSafe() 
	{
		return m_state == State.Safe;
	}

	public void applyForce( Vector3 force ) 
	{
		DynamicObject dynObj = query( DynamicObject.class );
		dynObj.applyForce( force );
	}
}
