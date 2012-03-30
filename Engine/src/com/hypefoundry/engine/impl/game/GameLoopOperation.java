/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.impl.game.GameOperation;

/**
 * @author Paksas
 *
 */
class GameLoopOperation implements GameOperation
{		
	@Override
	public void update( float deltaTime, GLGame game )
	{
		// update and draw the screen, if the game's running, providing it with a proper time delta
		try
		{
			game.m_input.update( deltaTime );
			
			game.m_screen.update( deltaTime );
			game.m_screen.present( deltaTime );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
}
