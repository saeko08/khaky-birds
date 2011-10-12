/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.gameCamera;

import com.hypefoundry.khakyBirds.entities.bird.Bird;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;

/**
 * @author Paksas
 *
 */
public class GameCameraController extends FiniteStateMachine implements WorldView
{
	private World					m_world;
	private Camera2D				m_camera;
	private GameCamera				m_gameCamera;
	private DynamicObject			m_camDynamicObject;
	private	Bird					m_observedBird;
	private SteeringBehaviors		m_sb;
	
	private float 					m_minOXBirdDist;
	private float 					m_minOYBirdDist;
	private float 					m_maxOXBirdDist;
	private float 					m_maxOYBirdDist;
	
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	/**
	 * Stops the camera on the spot.
	 * 
	 * @author Paksas
	 */
	class Idle extends FSMState
	{
		@Override
		public void activate()
		{
			m_gameCamera.m_state = GameCamera.State.Idle;
			m_sb.clear();
		}
	}
	
	/**
	 * Makes the camera follow the bird's movement.
	 * 
	 * @author Paksas
	 */
	class FollowBird extends FSMState
	{	
		private DynamicObject		m_birdDynamicObj;
		
		@Override
		public void activate()
		{
			if ( m_observedBird == null )
			{
				transitionTo( Idle.class );
			}
			else
			{
				m_gameCamera.m_state = GameCamera.State.FollowBird; 
				m_sb.clear();
				
				m_birdDynamicObj = m_observedBird.query( DynamicObject.class );
			}
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// if there's no bird to follow, then transition to the Idle state
			if ( m_observedBird == null )
			{
				transitionTo( Idle.class );
				return;
			}
			
			// adjust maximum speed to that of the bird, but only to its fraction - if the camera
			// follows the bird at the same pace, it results in an awfully torn movement
			m_camDynamicObject.m_linearSpeed = m_birdDynamicObj.m_linearSpeed * 0.5f;
			
			// make sure the camera doesn't start moving unless the bird 
			// reaches the border area of the frustum
			Vector3 birdPos = m_observedBird.getPosition();
			float oxDist = Math.abs( birdPos.m_x - m_camera.m_position.m_x );
			float oyDist = Math.abs( birdPos.m_y - m_camera.m_position.m_y );
			
			if ( oxDist > m_maxOXBirdDist || oyDist > m_maxOYBirdDist )
			{
				m_sb.begin().follow( m_observedBird );
			}
			else if ( oxDist <= m_minOXBirdDist || oyDist <= m_minOYBirdDist )
			{
				m_sb.clear();
			}
			
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
	}
	
	/**
	 * Moves the camera to the specified point in the game world.
	 * 
	 * @author Paksas
	 */
	class FocusOn extends FSMState
	{
		@Override
		public void activate()
		{
			m_gameCamera.m_state = GameCamera.State.FocusOn;
			m_sb.begin().arrive( m_gameCamera.m_focusPoint, 1.0f );
			
			// set the maximum movement speed
			m_camDynamicObject.m_linearSpeed = 3.0f;
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
	}
	
	// ------------------------------------------------------------------------
	// Controller methods
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param entity
	 * @param camera		controlled camera
	 */
	public GameCameraController( World world, Entity entity, Camera2D camera ) 
	{
		super( entity );
		
		m_world = world;
		
		m_gameCamera = (GameCamera)entity;
		m_camDynamicObject = entity.query( DynamicObject.class );
		
		m_sb = new SteeringBehaviors( entity );
		m_camera = camera;
		
		// calculate the maximum distances
		m_minOXBirdDist = m_camera.m_frustumWidth * 0.2f;
		m_minOYBirdDist = m_camera.m_frustumHeight * 0.2f;
		m_maxOXBirdDist = m_camera.m_frustumWidth * 0.3f;
		m_maxOYBirdDist = m_camera.m_frustumHeight * 0.3f;
		
		// setup the state machine
		register( new Idle() );
		register( new FollowBird() );
		register( new FocusOn() );
		begin( Idle.class );
		
		// attach the observer
		world.attachView( this );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		// update the current state
		switch( m_gameCamera.m_state )
		{
			case Idle:
			{
				transitionTo( Idle.class );
				break;
			}
			
			case FollowBird:
			{
				transitionTo( FollowBird.class );
				break;
			}
			
			case FocusOn:
			{
				transitionTo( FocusOn.class );
				break;
			}
		}
		
		// update the steering behavior
		m_sb.update( deltaTime );
		
		// simulate the movement
		m_camDynamicObject.simulate( deltaTime, m_entity );
		
		// position the active camera, but make sure the camera stays inside the world bounds
		Vector3 pos = m_entity.getPosition();
		m_world.constrainToWorldBoundaries( pos, m_camera.m_frustumWidth * 0.5f, m_camera.m_frustumHeight * 0.5f );
		m_camera.m_position.set( pos );
	}
	
	// ------------------------------------------------------------------------
	// WorldView implementation
	// ------------------------------------------------------------------------
	
	@Override
	public void onAttached( World world ) 
	{
	}

	@Override
	public void onDetached( World world ) 
	{
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
		if ( entity instanceof Bird )
		{
			m_observedBird = (Bird)entity;
			
			// automatically start following the bird, as soon as it appears
			// somewhere in the world
			transitionTo( FollowBird.class );
		}
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		if ( m_observedBird == entity )
		{
			m_observedBird = null;
			transitionTo( Idle.class );
		}
	}

}
