/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;


import java.util.List;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Eaten;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter.Shot;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shocked;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.renderer2D.Camera2D;


/**
 * Bird entity controller.
 * 
 * @author paksas
 *
 */
public class BirdController extends FiniteStateMachine 
{
	private Camera2D			m_camera;
	private Bird				m_bird;
	private SteeringBehaviors	m_sb;
	private Input				m_input;
	private Vector3				m_dragStart = new Vector3( 0, 0, 0 );
	private final float			AIM_TIMER = 0.4f;
	
	
	// ----------------------------------------------------------------
	
	class Idle extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos  = new Vector3();
		private	Vector3 m_gestureDir  = new Vector3();
		
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Idle;
			
			// we're not interested in previous duration events
			m_input.clearTouchDuration();
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if ( m_input.getTouchDuriation( 0 ) > AIM_TIMER )
			{
				transitionTo( Shitting.class );
			}
			else
			{
				updateInput( deltaTime );
			}
		}
		
		private void updateInput( float deltaTime ) 
		{	
			List< TouchEvent > inputEvents = m_input.getTouchEvents();
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
			
				// maybe we are drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
				{
					m_dragStart.m_x = lastEvent.x;
					m_dragStart.m_y = lastEvent.y;
				}
				
				// we stopped drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_UP)
				{			
					float dx = lastEvent.x - m_dragStart.m_x;
					float dy = lastEvent.y - m_dragStart.m_y;
					
					
					boolean canJump = calculateJumpPosition(dx, dy);
					if ( canJump )
					{
						transitionTo( Jumping.class ).setJumpingPosition( m_goToPos );
						break;
					}
				}
				
				// we double tapped the screen
				if ( lastEvent.type == TouchEvent.TOUCH_DOUBLE_TAP )
				{
					transitionTo( Flying.class );
					break;
				}
			}
		}

		/**
		 * Calculates a jump position in the direction pointed by the gesture.
		 * 
		 * @param dx
		 * @param dy
		 * @return
		 */
		private boolean calculateJumpPosition( float dx, float dy ) 
		{
			if ( m_bird.m_cables == null )
			{
				return false;
			}
			
			// change the gesture direction from screen to model space
			m_gestureDir.set( dx, dy, 0 );
			m_camera.directionToWorld( m_gestureDir );
			
			// I need a desired position the gesture points to
			m_goToPos.set( m_bird.getPosition() ).add( m_gestureDir );

			int cableIdx = m_bird.m_cables.getNearestCableIdx( m_goToPos );
			if ( cableIdx < 0 )
			{
				return false;
			}
			
			m_bird.m_cables.getPositionOnCable( cableIdx, m_goToPos, m_gestureDir );
			m_goToPos.set( m_gestureDir );
			return true;
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shocked || event instanceof Shot)
			{
				die();
			}
		}
	}
	// ----------------------------------------------------------------
	class Jumping extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos  = new Vector3();
	
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Jumping;
			m_sb.begin().arrive( m_goToPos, 1.2f ).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			Vector3 currPos = m_bird.getPosition();
			float distSqToGoal = currPos.distSq2D( m_goToPos );
			if ( distSqToGoal < 1e-3 )
			{
				transitionTo( Idle.class );
			}	
		}
		
		@Override
		public void onEvent( EntityEvent event  ) 
		{
			if ( event instanceof Eaten || event instanceof Shot)
			{
				die();
			}
		}
		
		public void setJumpingPosition(Vector3 destination) 
		{
			m_goToPos.set(destination);
		}
	}
	
	// ----------------------------------------------------------------
	
	class Landing extends FSMState
	{
		private Vector3 m_goToPos = new Vector3();
		
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Landing;
			
			m_sb.begin().seek( m_goToPos ).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			Vector3 currPos = m_bird.getPosition();
			float distSqToGoal = currPos.distSq2D( m_goToPos );
			if ( distSqToGoal < 1e-3 )
			{
				transitionTo( Idle.class );
			}				
		}

		public void setLandingPosition(int cableIdx) 
		{
			m_bird.m_cables.getPositionOnCable( cableIdx, m_bird.getPosition(), m_goToPos );
		}
	}
	
	// ----------------------------------------------------------------
	
	class Flying extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos  = new Vector3();
		private	Vector3 m_gestureDir  = new Vector3();
		private boolean m_canFly      = false;

		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Flying;
		}
		
		@Override
		public void deactivate()
		{
			m_canFly      = false;
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			updateInput( deltaTime );
			if(m_canFly)
			{
				m_sb.begin().arrive( m_goToPos, 1.5f ).faceMovementDirection();
			}
		}
		
		private void updateInput( float deltaTime ) 
		{	
			List< TouchEvent > inputEvents = m_input.getTouchEvents();
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
			
				// maybe we are drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
				{
					m_dragStart.m_x = lastEvent.x;
					m_dragStart.m_y = lastEvent.y;
					
					float dx = lastEvent.x;
					float dy = lastEvent.y;
					
					
					m_canFly = calculateFlightPosition(dx, dy);
					
					
				}
				
				// we double tapped the screen
				if ( lastEvent.type == TouchEvent.TOUCH_DOUBLE_TAP )
				{
					tryLanding();
					break;
				}
			}
		}
		
		/**
		 * Calculates a flight position in the direction pointed by the gesture.
		 * 
		 * @param dx
		 * @param dy
		 * @return
		 */
		private boolean calculateFlightPosition( float dx, float dy ) 
		{
		
			// change the gesture direction from screen to model space
			m_gestureDir.set( dx, dy, 0 );
			
			//m_camera.directionToWorld( m_gestureDir );
			m_camera.touchToWorld( m_gestureDir );
			
			// I need a desired position the gesture points to
			m_goToPos.set( m_gestureDir );
			
			if (m_goToPos != m_bird.getPosition())
			{
				setFlightPosition( m_goToPos );
				return true;
			}
			else
			{
				return false;
			}
		}
		
		private void setFlightPosition(Vector3 destination) 
		{
			m_goToPos.set(destination);
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shot )
			{
				die();
			}
		}

		public void tryLanding()
		{
			if ( m_bird.m_cables == null )
			{
				// no cables - can't land
				return;
			}
			
			int cableIdx = m_bird.m_cables.getNearestCableIdx( m_bird.getPosition() );			
			transitionTo( Landing.class ).setLandingPosition( cableIdx );
		}
	}
	
	// ----------------------------------------------------------------
	
	class Shitting extends FSMState implements EntityEventListener
	{
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Shitting;
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
			List< TouchEvent > inputEvents = m_input.getTouchEvents();
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);	
				if ( lastEvent.type == TouchEvent.TOUCH_UP)
				{
					m_bird.makeShit();
					transitionTo( Idle.class );
					break;
				}
			}
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shocked || event instanceof Shot )
			{
				die();
			}
		}
	}
	
	// ----------------------------------------------------------------

	
	/**
	 * Constructor.
	 * 
	 * @param input			input manager
	 * @param camera		active camera
	 * @param entity
	 */
	public BirdController( Input input, Camera2D camera, Entity entity ) 
	{
		super( entity );
		
		m_input = input;
		m_camera = camera;
		m_bird = (Bird)entity;
		m_sb = new SteeringBehaviors( m_bird );
		
		// set up state machine
		register( new Idle() );
		register( new Jumping() );
		register( new Flying() );
		register( new Shitting() );
		register( new Landing() );
		
		begin( Flying.class ).tryLanding();
		
		// define events the entity responds to
		m_bird.registerEvent( Eaten.class, new EventFactory< Eaten >() { @Override public Eaten createObject() { return new Eaten (); } } );
		m_bird.registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
		m_bird.registerEvent( Shot.class, new EventFactory< Shot >() { @Override public Shot createObject() { return new Shot (); } } );
	
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update( deltaTime );
	}
	
	void die()
	{
		m_bird.m_world.removeEntity( m_bird );
	}

}
