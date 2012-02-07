/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.bird;


import java.util.List;

import com.hypefoundry.khakyBirds.GameScreen;
import com.hypefoundry.khakyBirds.entities.falcon.Eaten;
import com.hypefoundry.khakyBirds.entities.hunter.Shot;
import com.hypefoundry.khakyBirds.entities.shock.Shocked;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.animation.AnimEvent;


/**
 * Bird entity controller.
 * 
 * @author paksas
 *
 */
public class BirdController extends FiniteStateMachine 
{
	private GameScreen			m_screen;
	private Camera2D			m_camera;
	private Bird				m_bird;
	private SteeringBehaviors	m_sb;
	private Vector3				m_dragStart = new Vector3( 0, 0, 0 );
	private final float			AIM_TIMER = 0.4f;
	private boolean				m_isPaused;
	
	
	// ----------------------------------------------------------------
	
	class Idle extends FSMState implements EntityEventListener, InputHandler, ButtonListener
	{
		private boolean	m_gestureStarted = false;
		private	Vector3 m_goToPos  = new Vector3();
		private	Vector3 m_gestureDir  = new Vector3();
		private HudLayout			m_hudLayout;
		
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Idle;
			m_gestureStarted = false;
			
			if ( m_hudLayout == null )
			{
				m_hudLayout = m_screen.getResourceManager().getResource( HudLayout.class, "hud/gameplay/gameHudcrap.xml" );
				m_hudLayout.attachRenderer( m_screen.m_hudRenderer ); 
				m_hudLayout.attachButtonListener( this );
			}
			
			// attach the input handler
			m_screen.registerInputHandler( this );
			
		}
		
		@Override
		public void deactivate()
		{
			m_gestureStarted = false;
			
		
			m_hudLayout.detachButtonListener( this );
			m_hudLayout.detachRenderer( m_screen.m_hudRenderer ); 
			m_hudLayout = null;
			
			// detach the input handler
			m_screen.unregisterInputHandler( this );

		}
		
		@Override
		public boolean handleInput( Input input, float deltaTime ) 
		{
			
			List< TouchEvent > inputEvents = input.getTouchEvents();
			
			// first check if we received a double tap event - if so, discard the rest
			int count = inputEvents.size();
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
				
				// we double tapped the screen
				if ( lastEvent.type == TouchEvent.TOUCH_DOUBLE_TAP )
				{
					transitionTo( Flying.class );
					return false;
				}
			}
			
			// ok - no double tap was received - start processing the gestures
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
			
				// maybe we are drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN && m_gestureStarted == false )
				{
					m_dragStart.m_x = lastEvent.x;
					m_dragStart.m_y = lastEvent.y;
					m_gestureStarted = true;
					break;
				}
				
				// we stopped drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_UP && m_gestureStarted == true )
				{			
					float dx = lastEvent.x - m_dragStart.m_x;
					float dy = lastEvent.y - m_dragStart.m_y;	
					
					boolean canJump = calculateJumpPosition(dx, dy);
					if ( canJump )
					{
						transitionTo( Jumping.class ).setJumpingPosition( m_goToPos );
						break;
					}
					m_gestureStarted = false;
				}
			}
			
			return false;
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
			m_camera.screenVecToWorld( m_gestureDir, m_gestureDir );
			
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
			if ( event instanceof Shocked || event instanceof Shot)
			{
				die();
			}
			else if (event instanceof AnimEvent )
			{
				AnimEvent animEvent = (AnimEvent)event;
				if ( animEvent.m_event instanceof Crapping )
				{
					m_bird.enableCrapping(true);
				}
			}
		}

		@Override
		public void onButtonPressed(String buttonId) 
		{
			if ( buttonId.equals( "Crap" ) && m_bird.m_canCrap )
			{
				m_bird.enableCrapping(false);
				m_bird.makeShit();
				
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
	
	class Flying extends FSMState implements EntityEventListener, InputHandler, ButtonListener
	{
		
		private	Vector3 m_goToPos  		= new Vector3();
		private	Vector3 m_tmpScreenPos	= new Vector3();
		private	Vector3 m_tmpWorldPos  	= new Vector3();
		private boolean m_canFly      	= false;
		private HudLayout			m_hudLayout;

		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Flying;
			m_canFly = false;
			
			if ( m_hudLayout == null )
			{
				m_hudLayout = m_screen.getResourceManager().getResource( HudLayout.class, "hud/gameplay/gameHudcrap.xml" );
				m_hudLayout.attachRenderer( m_screen.m_hudRenderer ); 
				m_hudLayout.attachButtonListener( this );
			}
			
			// attach the input handler
			m_screen.registerInputHandler( this );
		}
		
		@Override
		public void deactivate()
		{
			m_canFly      = false;
			m_sb.clear();
			
			m_hudLayout.detachButtonListener( this );
			m_hudLayout.detachRenderer( m_screen.m_hudRenderer ); 
			m_hudLayout = null;
			
			// detach the input handler
			m_screen.unregisterInputHandler( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if( m_canFly )
			{
				m_sb.begin().arrive( m_goToPos, 1.5f ).faceMovementDirection();
				m_canFly = false;
			}
		}
		
		@Override
		public boolean handleInput(Input input, float deltaTime) 
		{	
			List< TouchEvent > inputEvents = input.getTouchEvents();
			int count = inputEvents.size();
			
			// first check if we received a double tap event - if so, discard the rest
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
				
				// we double tapped the screen
				if ( lastEvent.type == TouchEvent.TOUCH_DOUBLE_TAP )
				{
					tryLanding();
					return false;
				}
			}
			
			// no landing command - look for flight directions 
			for ( int i = 0 ; i < count; ++i )
			{	
				TouchEvent lastEvent = inputEvents.get(i);
			
				// maybe we are drawing a gesture
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
				{					
					calculateFlightPosition( lastEvent.x, lastEvent.y );
					break;
				}
			}
			
			return false;
		}
		
		/**
		 * Calculates a flight position in the direction pointed by the gesture.
		 * 
		 * @param dx
		 * @param dy
		 * @return
		 */
		private void calculateFlightPosition( float dx, float dy ) 
		{
			// change the gesture direction from screen to model space
			m_tmpScreenPos.set( dx, dy, 0 );
			
			//m_camera.directionToWorld( m_gestureDir );
			m_camera.screenPosToWorld( m_tmpScreenPos, m_tmpWorldPos );
						
			if ( m_tmpWorldPos.distSq2D( m_bird.getPosition() ) > 1e-2 )
			{
				m_goToPos.set( m_tmpWorldPos );
				m_canFly = true;
			}
			else
			{
				m_canFly = false;
			}
		}
		
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shot )
			{
				die();
			}
			else if (event instanceof AnimEvent )
			{
				AnimEvent animEvent = (AnimEvent)event;
				if ( animEvent.m_event instanceof Crapping )
				{
					m_bird.enableCrapping(true);
				}
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
		
		@Override
		public void onButtonPressed(String buttonId) 
		{
			if ( buttonId.equals( "Crap" ) && m_bird.m_canCrap )
			{
				m_bird.enableCrapping(false);
				m_bird.makeShit();
				
			}
			if ( buttonId.equals( "CrapSpecial" ) && m_bird.m_canCrap )
			{
				if(m_bird.m_currentSpecialCrapAmount > 0)
				{
					m_bird.enableCrapping(false);
					m_bird.makeSpecialShit();
				}
				
			}
		}
		
		
	}
	
	// ----------------------------------------------------------------
	class Paused extends FSMState 
	{
		
	
		@Override
		public void activate()
		{
			m_isPaused = true;
		}
		
		@Override
		public void deactivate()
		{
			m_isPaused = false;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			if( m_bird.m_paused == false )
			{
				if (m_bird.m_state == Bird.State.Idle)
				{
					transitionTo( Idle.class );
				}
				
				else if (m_bird.m_state == Bird.State.Flying)
				{
					transitionTo( Flying.class );
				}
				else 
				{
					transitionTo( Flying.class );
				}
			}
		}
		
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param input			input manager
	 * @param camera		active camera
	 * @param entity
	 */
	public BirdController( GameScreen screen, Camera2D camera, Entity entity ) 
	{
		super( entity );
		
		m_screen = screen;
		m_camera = camera;
		m_bird = (Bird)entity;
		m_sb = new SteeringBehaviors( m_bird );
		
		// set up state machine
		register( new Idle() );
		register( new Jumping() );
		register( new Flying() );
		register( new Landing() );
		register( new Paused() );
		
		begin( Paused.class );
		
		// define events the entity responds to
		m_bird.registerEvent( Eaten.class, new EventFactory< Eaten >() { @Override public Eaten createObject() { return new Eaten (); } } );
		m_bird.registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
		m_bird.registerEvent( Shot.class, new EventFactory< Shot >() { @Override public Shot createObject() { return new Shot (); } } );
		m_bird.registerEvent( AnimEvent.class, new EventFactory< AnimEvent >() { @Override public AnimEvent createObject() { return new AnimEvent (); } } );
	
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update( deltaTime );
		
		if (m_bird.m_paused == true)
		{
			if (m_isPaused == false)
			{
				transitionTo( Paused.class );
			}
		}
	}
	
	void die()
	{
		m_bird.m_world.removeEntity( m_bird );
	}

}
