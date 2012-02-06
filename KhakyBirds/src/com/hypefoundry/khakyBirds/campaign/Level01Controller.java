/**
 * 
 */
package com.hypefoundry.khakyBirds.campaign;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.khakyBirds.GameScreen;
import com.hypefoundry.khakyBirds.entities.bird.Bird;
import com.hypefoundry.khakyBirds.entities.pedestrian.Pedestrian;

/**
 * @author Paksas
 *
 */
public class Level01Controller extends FiniteStateMachine
{
	private Level01				m_level;
	private GameScreen 			m_screen;
	
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	class EntryCutscene extends FSMState implements ButtonListener
	{
		private HudLayout 			m_layout;
		
		
		@Override
		public void activate()
		{
			// pause the game
			m_screen.pause( true );
			
			// load the cutscene hud layout
			m_layout = m_screen.getResourceManager().instantiateResource( HudLayout.class, "campaign/l01/hud/cutscene01.xml" );
			m_layout.attachButtonListener( this );
			
			m_layout.attachRenderer( m_screen.m_hudRenderer );
		}
		
		@Override 
		public void deactivate()
		{
			// hide the hud
			m_layout.detachRenderer( m_screen.m_hudRenderer );
			
			m_layout.detachButtonListener( this );
			m_layout = null;
		}

		@Override
		public void onButtonPressed( String id ) 
		{
			// unpause the game
			m_screen.pause( false );
			
			// move to the next stage
			// TODO: transition there once the assets are ready transitionTonTo( MovementTutorialCutscene.class );
			transitionTonTo( MovementTutorialGameplay.class );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class MovementTutorialCutscene extends FSMState implements ButtonListener
	{
		private HudLayout 			m_layout;
		
		
		@Override
		public void activate()
		{
			// pause the game
			m_screen.pause( true );
			
			// load the cutscene hud layout
			m_layout = m_screen.getResourceManager().instantiateResource( HudLayout.class, "campaign/l01/hud/movementTutorialCutscene.xml" );
			m_layout.attachButtonListener( this );
			
			m_layout.attachRenderer( m_screen.m_hudRenderer );
		}
		
		@Override 
		public void deactivate()
		{
			// hide the hud
			m_layout.detachRenderer( m_screen.m_hudRenderer );
			
			m_layout.detachButtonListener( this );
			m_layout = null;
		}

		@Override
		public void onButtonPressed( String id ) 
		{
			// unpause the game
			m_screen.pause( false );
			
			// move to the next stage
			transitionTonTo( MovementTutorialGameplay.class );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class MovementTutorialGameplay extends FSMState
	{
		private Vector3			m_initialBirdPos = new Vector3();
		private	Bird 			m_bird;
		private float			m_movementTutorialTimer = 0;
		
		@Override
		public void activate()
		{
			m_bird = (Bird) m_screen.m_world.findEntity( Bird.class );
			m_bird.pause(false);
			m_initialBirdPos.set( m_bird.getPosition() );
			
			// don't allow the bird to crap for a moment 
			m_bird.enableCrapping( false );
			
			m_movementTutorialTimer = m_level.m_movementTutorialTimer;
		}
		
		@Override
		public void deactivate()
		{
			// allow bird to crap again
			m_bird.enableCrapping( true );
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// monitor birds position and wait until the bird moves a bit away
			Vector3 currPos = m_bird.getPosition();
			if ( currPos.distSq2D( m_initialBirdPos ) >= m_level.m_minMovementTutorialDistance )
			{
				// ok - the user was able to move the bird to the desired distance - proceed
				transitionTo( CrappingTutorialCutscene.class );
				return;
			}
			
			// check the timer - maybe the user needs a 'friendly' reminder
			m_movementTutorialTimer -= deltaTime;
			if ( m_movementTutorialTimer <= 0 )
			{
				transitionTo( MovementTutorialCutscene.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class CrappingTutorialCutscene extends FSMState implements ButtonListener
	{
		private HudLayout 			m_layout;
		private	Bird 				m_bird;
		
		@Override
		public void activate()
		{
			m_bird = (Bird) m_screen.m_world.findEntity( Bird.class );
			
			m_bird.pause(true);
			// pause the game
			m_screen.pause( true );
			
			// load the cutscene hud layout
			m_layout = m_screen.getResourceManager().instantiateResource( HudLayout.class, "campaign/l01/hud/crappingTutorialCutscene.xml" );
			m_layout.attachButtonListener( this );
			
			m_layout.attachRenderer( m_screen.m_hudRenderer );
		}
		
		@Override 
		public void deactivate()
		{
			// hide the hud
			m_layout.detachRenderer( m_screen.m_hudRenderer );
			
			m_layout.detachButtonListener( this );
			m_layout = null;
		}

		@Override
		public void onButtonPressed( String id ) 
		{
			// unpause the game
			m_screen.pause( false );
			
			// move to the next stage
			transitionTonTo( CrappingTutorialGameplay.class );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class CrappingTutorialGameplay extends FSMState
	{
		private	Pedestrian		m_boy;
		private float			m_crappingTutorialTimer = 0;
		private	Bird 			m_bird;
		
		
		@Override
		public void activate()
		{
			m_bird = (Bird) m_screen.m_world.findEntity( Bird.class );
			m_bird.pause(false);
			m_boy = (Pedestrian) m_screen.m_world.findEntity( Pedestrian.class );			
			m_crappingTutorialTimer = m_level.m_crappingTutorialTimer;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// monitor birds position and wait until the bird moves a bit away
			if ( m_boy.wasHitWithShit() )
			{
				transitionTo( FinalCutscene.class );
				return;
			}
			
			// check the timer - maybe the user needs a 'friendly' reminder
			m_crappingTutorialTimer -= deltaTime;
			if ( m_crappingTutorialTimer <= 0 )
			{
				transitionTo( CrappingTutorialCutscene.class );
			}
		}
	}

	// ------------------------------------------------------------------------
	
	class FinalCutscene extends FSMState implements ButtonListener
	{
		private HudLayout 			m_layout;
		
		
		@Override
		public void activate()
		{
			// pause the game
			m_screen.pause( true );
			
			// load the cutscene hud layout
			m_layout = m_screen.getResourceManager().instantiateResource( HudLayout.class, "campaign/l01/hud/finalCutscene.xml" );
			m_layout.attachButtonListener( this );
			
			m_layout.attachRenderer( m_screen.m_hudRenderer );
		}
		
		@Override 
		public void deactivate()
		{
			// hide the hud
			m_layout.detachRenderer( m_screen.m_hudRenderer );
			
			m_layout.detachButtonListener( this );
			m_layout = null;
		}

		@Override
		public void onButtonPressed( String id ) 
		{
			// unpause the game
			m_screen.pause( false );
			
			// load the next level
			m_screen.loadLevel( 2 );
		}
	}
	
	// ------------------------------------------------------------------------
	// Controller stuff
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param screen
	 * @param entity
	 */
	public Level01Controller( GameScreen screen, Entity entity ) 
	{
		super( entity );
		
		m_level = (Level01)entity;
		m_screen = screen;
		
		// register states
		register( new EntryCutscene() );
		register( new MovementTutorialCutscene() );
		register( new MovementTutorialGameplay() );
		register( new CrappingTutorialCutscene() );
		register( new CrappingTutorialGameplay() );
		register( new FinalCutscene() );
		begin( EntryCutscene.class );
	}

}
