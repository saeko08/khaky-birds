/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;


import java.util.List;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Eaten;
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


/**
 * Bird entity controller.
 * 
 * @author paksas
 *
 */
public class BirdController extends FiniteStateMachine 
{
	private Bird				m_bird;
	private SteeringBehaviors	m_sb;
	private Input				m_input;
	private Vector3				m_dragStart = new Vector3( 0, 0, 0 );
	private final int			m_inputSensitivityThreshold = 25;
	private final float			AIM_TIMER = 0.4f;
	private final float 		m_dy 				 = 1;
	
	
	// ----------------------------------------------------------------
	
	class Idle extends FSMState implements EntityEventListener
	{
		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Idle;
			m_bird.attachEventListener( this );
		}
		
		@Override
		public void deactivate()
		{
			m_bird.detachEventListener( this );
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
			for ( TouchEvent lastEvent : inputEvents )
			{	
				if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
				{
					m_dragStart.m_x = lastEvent.x;
					m_dragStart.m_y = lastEvent.y;
				}
				if ( lastEvent.type == TouchEvent.TOUCH_UP)
				{			
					float dx = lastEvent.x - m_dragStart.m_x;
					float dy = lastEvent.y - m_dragStart.m_y;
					
					transitionTo( Jumping.class ).setJumpingPosition(dx,dy );
					break;
					//moveBird( dx, dy );
				}
				if ( lastEvent.type == TouchEvent.TOUCH_DOUBLE_TAP )
				{
					transitionTo( Flying.class );
					break;
				}
			}
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shocked )
			{
				die();
			}
		}
	}
	// ----------------------------------------------------------------
	class Jumping extends FSMState implements EntityEventListener
	{
		
		private	Vector3 m_goToPos  = new Vector3();
		private Vector3 m_direction = new Vector3();
		@Override
		public void activate()
		{
			m_direction.set(m_goToPos);
			m_direction.sub(m_bird.getPosition());
			m_direction.normalize2D();
			m_bird.m_state = Bird.State.Jumping;
			m_bird.attachEventListener( this );
			m_sb.begin().lookAt(m_direction).seek( m_goToPos ).faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_bird.detachEventListener( this );
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			Vector3 currPos = m_bird.getPosition();
			float distSqToGoal = currPos.distSq( m_goToPos );
			if ( distSqToGoal < 1e-1 )
			{
				transitionTo( Idle.class );
				//int cableIdx = m_bird.m_cables.getNearestCableIdx( m_bird.getPosition() );	
				//transitionTo( Landing.class ).setLandingPosition(cableIdx);
			}
			
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten || event instanceof Shocked )
			{
				die();
			}
		}
		public void setJumpingPosition(float dx, float dy) 
		{
			
			// decide where to move
			if ( dx > m_inputSensitivityThreshold )
			{
				jumpRight();
			}
			else if ( dx < -m_inputSensitivityThreshold )
			{
				jumpLeft();
			}
			
			if ( dy > m_inputSensitivityThreshold )
			{
				jumpDown();
			}
			else if ( dy < -m_inputSensitivityThreshold )
			{
				jumpUp();
			}
		}
		
		void jumpLeft() 
		{
			if ( m_bird.m_cables == null )
			{
				return;
			}
			
			m_goToPos.set( m_bird.getPosition() );
			
			m_bird.m_cableIdx = m_bird.m_cables.getLeftCable( m_bird.m_cableIdx );
			m_goToPos.m_x = m_bird.m_cables.getPositionOnCable( m_bird.m_cableIdx, m_goToPos.m_y );
			
			//m_sb.begin().seek( m_goToPos ).faceMovementDirection();
		}

		/**
		 * Moves the bird to the next cable to its right. 
		 */
		void jumpRight() 
		{
			if ( m_bird.m_cables == null )
			{
				return;
			}
			m_goToPos.set( m_bird.getPosition() );
			
			m_bird.m_cableIdx = m_bird.m_cables.getRightCable( m_bird.m_cableIdx );
			m_goToPos.m_x = m_bird.m_cables.getPositionOnCable( m_bird.m_cableIdx, m_goToPos.m_y );
			
			//m_sb.begin().seek( m_goToPos ).faceMovementDirection();
		
			
		}

		/**
		 * Moves the bird down the cable it's sitting on. 
		 */
		void jumpDown() 
		{
			if ( m_bird.m_cables == null )
			{
				return;
			}
			
			m_goToPos.set( m_bird.getPosition() );
			m_goToPos.m_x = m_bird.m_cables.getPositionOnCable( m_bird.m_cableIdx, m_goToPos.m_y - m_dy );
			m_goToPos.m_y = m_goToPos.m_y - m_dy;
			
			//m_sb.begin().seek( m_goToPos ).faceMovementDirection();
		}

		/**
		 * Moves the bird up the cable it's sitting on. 
		 */
		void jumpUp() 
		{
			if ( m_bird.m_cables == null )
			{
				return;
			}
			
			m_goToPos.set( m_bird.getPosition() );
			m_goToPos.m_x = m_bird.m_cables.getPositionOnCable( m_bird.m_cableIdx, m_goToPos.m_y + m_dy );
			m_goToPos.m_y = m_goToPos.m_y + m_dy;
			//m_sb.begin().seek( m_goToPos ).faceMovementDirection();
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
			float distSqToGoal = currPos.distSq( m_goToPos );
			if ( distSqToGoal < 1e-1 )
			{
				transitionTo( Idle.class );
			}				
		}

		public void setLandingPosition(int cableIdx) 
		{
			m_goToPos.set( m_bird.getPosition() );
			m_goToPos.m_x = m_bird.m_cables.getPositionOnCable(cableIdx, m_goToPos.m_y );
		}
	}
	
	// ----------------------------------------------------------------
	
	class Flying extends FSMState implements EntityEventListener
	{

		@Override
		public void activate()
		{
			m_bird.m_state = Bird.State.Flying;
			m_bird.attachEventListener( this );
		}
		
		@Override
		public void deactivate()
		{
			m_bird.m_state = Bird.State.Landing;
			m_bird.detachEventListener( this );
		}
		
		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Eaten )
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
			
			m_bird.attachEventListener( this );
		}
		
		@Override
		public void deactivate()
		{
			m_bird.detachEventListener( this );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			List< TouchEvent > inputEvents = m_input.getTouchEvents();
			for ( TouchEvent lastEvent : inputEvents )
			{	
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
			if ( event instanceof Eaten || event instanceof Shocked )
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
	 * @param entity
	 */
	public BirdController( Input input, Entity entity ) 
	{
		super( entity );
		
		m_input = input;
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
	
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update();
	}
	
	void die()
	{
		m_bird.m_world.removeEntity( m_bird );
	}
	

	/*private void moveBird( float dx, float dy ) 
	{	
		// decide where to move
		if ( dx > m_inputSensitivityThreshold )
		{
			m_bird.jumpRight();
			m_sb.faceMovementDirection();
		}
		else if ( dx < -m_inputSensitivityThreshold )
		{
			m_bird.jumpLeft();
		}
		
		if ( dy > m_inputSensitivityThreshold )
		{
			m_bird.jumpDown();
		}
		else if ( dy < -m_inputSensitivityThreshold )
		{
			m_bird.jumpUp();
		}
	}*/

}
