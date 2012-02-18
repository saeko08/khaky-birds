/**
 * 
 */
package com.hypefoundry.engine.physics.locomotion;

import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.math.MathLib;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;

/**
 * Steering behaviors controller.
 * 
 * @author Paksas
 *
 */
public class SteeringBehaviors implements Updatable 
{		
	// controlled entity
	private	Entity								m_entity;
	
	// behaviors indices
	private final Seek							m_seek = new Seek();
	private final Flee							m_flee = new Flee();
	private final Pursuit						m_pursuit = new Pursuit();
	private final Evade							m_evade = new Evade();
	private final Circle						m_circle = new Circle();
	private final Wander						m_wander = new Wander();
	private final FaceMovementDirection 		m_faceMovementDirection = new FaceMovementDirection();
	private final LookAt						m_lookAt = new LookAt();
	private final ActiveLookAt					m_activeLookAt = new ActiveLookAt();
	
	private SteeringBehavior[]					m_behaviors = {
			m_seek, 
			m_flee,
			m_pursuit,
			m_evade,
			m_circle,
			m_wander,
			m_faceMovementDirection,
			m_lookAt, 
			m_activeLookAt
	};

	
	// ------------------------------------------------------------------------
	
	abstract class SteeringBehavior
	{
		boolean			m_isActive = false;
		
		/**
		 * Calculates new velocity for the specified movable.
		 * 
		 * @param movable
		 * @param deltaTime
		 */
		abstract void update( DynamicObject movable, float deltaTime );
	};
	
	// ------------------------------------------------------------------------
	
	class Seek extends SteeringBehavior
	{
		private Vector3 	m_tmpDir = new Vector3();
		Vector3				m_staticGoal = new Vector3();
		float				m_breakDistanceFactor;
		
		void activate( Vector3 goal, float breakDistanceFactor )
		{
			m_isActive = true;
			m_staticGoal.set( goal );
			m_breakDistanceFactor = breakDistanceFactor;
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
		{
			Vector3 currPos = m_entity.getPosition();
			m_tmpDir.set( m_staticGoal ).sub( currPos );
			m_tmpDir.m_z = 0;
			
			float dist = m_tmpDir.mag();
			float breakingDistance = movable.m_linearSpeed * 0.1f * m_breakDistanceFactor;
			
			float desiredSpeed = movable.m_linearSpeed;
			float breakDistFactor = dist / breakingDistance;
			if ( breakDistFactor < 1.0f )
			{
				desiredSpeed *= breakDistFactor;
			}
			m_tmpDir.normalize2D().scale( desiredSpeed );
			
			movable.m_velocity.set( m_tmpDir );
		}
	};
	
	// ------------------------------------------------------------------------
	
	class Flee extends SteeringBehavior
	{
		private Vector3 	m_tmpDir = new Vector3();
		Vector3				m_fromPt = new Vector3();
		
		void activate( Vector3 from )
		{
			m_isActive = true;
			m_fromPt.set( from );
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
		{
			Vector3 currPos = m_entity.getPosition();
			m_tmpDir.set( currPos ).sub( m_fromPt );
			m_tmpDir.m_z = 0;
			
			m_tmpDir.normalize2D().scale( movable.m_linearSpeed );
			movable.m_velocity.set( m_tmpDir );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Pursuit extends Seek
	{
		private Entity			m_goal;
		private DynamicObject	m_goalDO;
		private float			m_predictionFactor;
		
		
		void activate( Entity goal, float predictionFactor )
		{
			m_isActive = true;
			m_goal = goal;
			m_goalDO = goal.query( DynamicObject.class );
			m_breakDistanceFactor = 1;
			m_predictionFactor = predictionFactor;
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
		{
			if ( m_goalDO != null )
			{
				m_staticGoal.set( m_goalDO.m_velocity ).scale( deltaTime * m_predictionFactor ).add( m_goal.getPosition() );
			}
			else
			{
				m_staticGoal.set( m_goal.getPosition() );
			}
			
			// regular seek
			super.update( movable, deltaTime );
		}
	};
	
	// ------------------------------------------------------------------------
	
	class Evade extends Flee
	{
		private Entity			m_pursuer;
		private DynamicObject	m_pursuerDO;
		
		void activate( Entity pursuer )
		{
			m_isActive = true;
			m_pursuer = pursuer;
			m_pursuerDO = pursuer.query( DynamicObject.class );
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
		{
			if ( m_pursuerDO != null )
			{
				m_fromPt.set( m_pursuerDO.m_velocity ).scale( deltaTime ).add( m_pursuer.getPosition() );
			}
			else
			{
				m_fromPt.set( m_pursuer.getPosition() );
			}
			
			// regular seek
			super.update( movable, deltaTime );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Circle extends SteeringBehavior
	{
		private Vector3 	m_tmpDir = new Vector3();
		private Vector3 	m_tmpVel = new Vector3();
		private Vector3 	m_tmpCentrifugalVel = new Vector3();
		Vector3				m_anchorPt = new Vector3();
		private float		m_radius;
		
		
		void activate( Vector3 anchorPt, float radius )
		{
			m_isActive = true;
			m_anchorPt.set( anchorPt );
			m_radius = radius;
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
		{
			m_tmpDir.set( m_anchorPt ).sub( m_entity.getPosition() );
			
			// calculate the centrifugal velocity
			float dist = m_tmpDir.set( m_anchorPt ).sub( m_entity.getPosition() ).mag2D();
			m_tmpDir.normalized2D( m_tmpCentrifugalVel );
			float distToRad = dist - m_radius;
			m_tmpCentrifugalVel.scale( distToRad );
			
			// calculate the pushing velocity
			m_tmpDir.normalize2D().cross( Vector3.EZ, m_tmpVel );
			
			// add the two together and scale them to match the movement speed
			m_tmpDir.set( m_tmpCentrifugalVel ).add( m_tmpVel ).normalize2D().scale( movable.m_linearSpeed );
			
			// set the speed
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
			m_movementDir.set( (float)Math.random() - 0.5f, (float)Math.random()- 0.5f, 0 );
			m_movementDir.normalize2D();
		}
		
		void activate( Vector3 initialDir )
		{
			m_isActive = true;
			m_movementDir.set( initialDir );
			m_movementDir.normalize2D();
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime )
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
		void update( DynamicObject movable, float deltaTime ) 
		{
			float rotationAngle = movable.m_velocity.angleXY() - m_entity.getFacing();
			rotationAngle = MathLib.getDiffAngle( rotationAngle );
			movable.m_rotation = movable.getRotationPerFrame( rotationAngle, deltaTime );
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
		void update( DynamicObject movable, float deltaTime ) 
		{
			float rotationAngle = m_lookAtVec.angleXY() - m_entity.getFacing();
			rotationAngle = MathLib.getDiffAngle( rotationAngle );
			movable.m_rotation = movable.getRotationPerFrame( rotationAngle, deltaTime );
		}
	};
	
	// ------------------------------------------------------------------------
	
	class ActiveLookAt extends LookAt
	{
		private Entity		m_goal;
		private Vector3		m_tmpDir = new Vector3();
		
		
		void activate( Entity goal )
		{
			m_isActive = true;
			m_goal = goal;
		}
		
		@Override
		void update( DynamicObject movable, float deltaTime ) 
		{
			m_tmpDir.set( m_goal.getPosition() ).sub( m_entity.getPosition() );
			super.activate( m_tmpDir );
			super.update( movable, deltaTime );
		}
	}
	
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
	 * 
	 * @param deltaTime
	 */
	public void update( float deltaTime )
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
				beh.update( movable, deltaTime );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// Behaviors control
	// ------------------------------------------------------------------------
	/**
	 * Begins a behavior definition.
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors begin()
	{
		for ( SteeringBehavior beh : m_behaviors )
		{
			beh.m_isActive = false;
		}
		return this;
	}
	
	/**
	 * Clears a behavior definition.
	 */
	public void clear()
	{
		for ( SteeringBehavior beh : m_behaviors )
		{
			beh.m_isActive = false;
		}
	}
	
	/**
	 * Makes the entity rush towards the specified goal position.
	 * 
	 * @param goal
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors seek( Vector3 goal )
	{				
		// activate the seek behavior
		m_seek.activate( goal, 1.0f );
		
		return this;
	}
	
	/**
	 * Makes the entity rush towards the specified goal position, and arrive at that position
	 * gently by breaking at the very last moment
	 * 
	 * @param goal
	 * @param breakDistanceFactor		factor which tells how far before the goal the breaking 
	 * 									process should begin ( the larger the number, the earlier it will start )
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors arrive( Vector3 goal, float breakDistanceFactor )
	{		
		// activate the seek behavior
		m_seek.activate( goal, breakDistanceFactor );
				
		return this;
	}
	
	/**
	 * Makes the entity flee from the specified point.
	 * 
	 * @param from
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors flee( Vector3 from )
	{
		// activate the flee behavior
		m_flee.activate( from );
		
		return this;
	}
	
	/**
	 * Makes the entity follow another entity ( pursuit without prediction ).
	 * 
	 * @param goal
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors follow( Entity goal )
	{		
		// activate the pursuit behavior
		m_pursuit.activate( goal, 0.0f );
				
		return this;
	}
	
	
	/**
	 * Makes the entity chase another entity.
	 * 
	 * @param goal
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors pursuit( Entity goal )
	{		
		// activate the pursuit behavior
		m_pursuit.activate( goal, 1.0f );
				
		return this;
	}
	
	/**
	 * Makes the entity run away from another entity.
	 * 
	 * @param pursuer
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors evade( Entity pursuer )
	{		
		// activate the evade behavior
		m_evade.activate( pursuer );
				
		return this;
	}
	
	/**
	 * Makes the entity circle around a point.
	 * 
	 * @param anchorPt
	 * @oaram radius
	 * 
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors circle( Vector3 anchorPt, float radius )
	{	
		// activate the circling behavior
		m_circle.activate( anchorPt, radius );
				
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
	 * Makes the entity wander around aimlessly.
	 * 
	 * @param initialDir		initial movement direction
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors wander( Vector3 initialDir )
	{
		m_wander.activate( initialDir );
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
	
	/**
	 * Makes the entity rotate so that it's looking at the specified 
	 * 
	 * @param goal
	 * @return instance to self, allowing to chain commands
	 */
	public SteeringBehaviors lookAt( Entity goal ) 
	{
		m_activeLookAt.activate( goal );
		return this;
	}
}

