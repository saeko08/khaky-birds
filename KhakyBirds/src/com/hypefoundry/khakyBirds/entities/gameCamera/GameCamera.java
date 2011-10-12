/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.gameCamera;


import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author Paksas
 */
public class GameCamera extends Entity
{
	enum State
	{
		Idle,
		FollowBird,
		FocusOn
	}
	
	State		m_state			= State.Idle;
	Vector3		m_focusPoint	= new Vector3();
	
	/**
	 * Constructor.
	 */
	public GameCamera()
	{
		// add movement capabilities
		final float maxLinearSpeed = 3.0f;
		final float maxRotationSpeed = 0.0f; // the camera can't rotate
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{		
		setPosition( hostWorld.getWidth() / 2, hostWorld.getHeight() / 2, 0 );
	}
	
	/**
	 * Makes the camera rest idly on its current position.
	 */
	void idle()
	{
		m_state = State.Idle;
	}
	
	/**
	 * Makes the camera follow the bird's movement.
	 */
	void followBird()
	{
		m_state = State.FollowBird;
	}
	
	/**
	 * Makes the camera focus on the specified point.
	 * 
	 * @param pos
	 */
	void focusOn( Vector3 pos )
	{
		m_focusPoint.set( pos );
		m_state = State.FocusOn;
	}
}
