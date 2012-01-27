/**
 * 
 */
package com.hypefoundry.kabloons.levels.level01;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.GameScreen;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.utils.AssetsFactory;

/**
 * @author Paksas
 *
 */
public class Level01Controller extends FiniteStateMachine 
{
	AssetsFactory			m_assetsFactory;
	Level01					m_level;
	GameScreen				m_screen;
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	class Gameplay extends FSMState
	{		
		private Vector3		m_initialPos = new Vector3( 2.4f, -0.2f, 0.0f );
		private Baloon		m_baloon;
		
		@Override
		public void activate()
		{
			// release a single baloon
			m_baloon = m_assetsFactory.createRandomBaloon( m_initialPos );
			m_screen.m_world.addEntity( m_baloon );
		}
		
		@Override
		public void deactivate()
		{
		}
		
		@Override
		public void execute( float deltaTime )
		{
			// monitor baloon's state
			if ( m_baloon.isAlive() == false )
			{
				// baloon was destroyed
				m_screen.gameFailed();
			}
			else if ( m_baloon.isSafe() == true )
			{
				// baloon reached safety
				m_screen.gameSucceeded();
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param screen
	 * @param assetsFactory
	 * @param entity
	 */
	public Level01Controller( GameScreen screen, AssetsFactory assetsFactory, Entity entity ) 
	{
		super( entity );
		
		m_assetsFactory = assetsFactory;
		m_level = (Level01)entity;
		m_screen = screen;
		
		// register states
		register( new Gameplay() );
		begin( Gameplay.class );
	}

}
