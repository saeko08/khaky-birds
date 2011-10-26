/**
 * 
 */
package com.hypefoundry.khakyBirds.campaign;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.hud.ButtonListener;
import com.hypefoundry.engine.hud.HudLayout;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.khakyBirds.GameScreen;

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
			transitionTonTo( Gameplay.class );
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Gameplay extends FSMState
	{
		private float	m_timer;
		
		@Override 
		public void activate()
		{
			m_timer = m_level.m_gameplayTime;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_timer -= deltaTime;
			if ( m_timer < 0 )
			{
				// end game after a while
				m_screen.exitToMenu();
			}
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
		register( new Gameplay() );
		begin( EntryCutscene.class );
	}

}
