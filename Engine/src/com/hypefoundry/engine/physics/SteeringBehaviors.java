/**
 * 
 */
package com.hypefoundry.engine.physics;

import java.util.*;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.world.Entity;

/**
 * Steering behaviors controller.
 * 
 * @author Paksas
 *
 */
public class SteeringBehaviors 
{		
	// controlled entity
	private	Entity								m_entity;
	
	// behaviors indices
	private final Seek							m_seek = new Seek();
	private final Wander						m_wander = new Wander();
	private final FaceMovementDirection 		m_faceMovementDirection = new FaceMovementDirection();
	private final LookAt						m_lookAt = new LookAt();
	
	private SteeringBehavior[]					m_behaviors = {
			m_seek, 
			m_wander,
			m_faceMovementDirection,
			m_lookAt
	};

	
	// ------------------------------------------------------------------------
	
	abstract class SteeringBehavior
	{
		boolean			m_isActive = false;
		
		/**
		 * Calculates new velocity for the specified movable.
		 * 
		 * @param movable
		 */
		abstract void update( DynamicObject movable );
	};
	
	// ------------------------------------------------------------------------
	
	class Seek extends SteeringBehavior
	{
		private Vector3 	m_tmpDir = new Vector3();
		public Vector3		m_staticGoal = new Vector3();
		
		void activate( Vector3 goal )
		{
			m_isActive = true;
			m_staticGoal.set( goal );
		}
		
		@Override
		void update( DynamicObject movable )
		{
			Vector3 currPos = m_entity.getPosition();
			m_tmpDir.set( m_staticGoal ).sub( currPos );
			
			movable.m_velocity.set( m_tmpDir );
		}
	};
	
	// ------------------------------------------------------------------------
	
	class Wander extends SteeringBehavior
	{		
		private Vector3 m_movementDir = new Vector3();
		void activate()
		{
			m_isActive = true;
			m_movementDir.set( (float)Math.random(), (float)Math.random(), 0 );
			m_movementDir.normalize2D();
		}
		
		@Override
		void update( DynamicObject movable )
		{
			movable.m_velocity.set( m_movementDir );
			movable.m_velocity.scale( movable.m_linearSpeed );
		}
	};
	
	// ------------------------------------------------------------------------
	
	class FaceMovementDirection extends SteeringBehavior
	{
		void activate()
		{
			m_isActive = true;
		}
		
		@Override
		void update( DynamicObject movable ) 
		{
			float rotationAngle = movable.m_velocity.angleXY() - m_entity.m_facing;
			movable.m_rotation = rotationAngle;
		}
	};
	
	// ------------------------------------------------------------------------
	
	class LookAt extends SteeringBehavior
	{
		private Vector3 m_lookAtVec = new Vector3();
		
		void activate( Vector3 lookAtVec )
		{
			m_isActive = true;
			m_lookAtVec.set( lookAtVec );
		}
		
		@Override
		void update( DynamicObject movable ) 
		{
			float rotationAngle = m_lookAtVec.angleXY() - m_entity.m_facing;
			movable.m_rotation = rotationAngle;
		}
	};
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param entity		controlled entity
	 */
	public SteeringBehaviors( Entity entity )
	{
		m_entity = entity;
	}
	
	/**
	 * Updates the movement params of the entity.
	 */
	public void update()
	{
		DynamicObject movable = m_entity.query( DynamicObject.class );
		if ( movable == null )
		{
			// can't move without a dynamic object aspect
			return;
		}
		
		for ( SteeringBehavior beh : m_behaviors )
		{
			if( beh.m_isActive )
			{
				beh.update( movable );
			}
		}
	
		movable.constrain();
	}
	
	// ------------------------------------------------------------------------
	// Behaviors control
	// ------------------------------------------------------------------------
	/**
	 * Resets all behaviors.
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors reset()
	{
		for ( SteeringBehavior beh : m_behaviors )
		{
			beh.m_isActive = false;
		}
		return this;
	}
	
	/**
	 * Makes the entity seek the specified goal position.
	 * 
	 * @param goal
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors seek( Vector3 goal )
	{
		m_seek.activate( goal );
		return this;
	}
	
	/**
	 * Makes the entity wander around aimlessly.
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors wander()
	{
		m_wander.activate();
		return this;
	}

	/**
	 * Makes the entity face its movement direction ( direction of the velocity vector )
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors faceMovementDirection() 
	{
		m_faceMovementDirection.activate();
		return this;
	}

	/**
	 * Makes the entity rotate so that it's looking down the specified vector.
	 * 
	 * @param lookAtVec
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors lookAt( Vector3 lookAtVec ) 
	{
		m_lookAt.activate( lookAtVec );
		return this;
	}
}

