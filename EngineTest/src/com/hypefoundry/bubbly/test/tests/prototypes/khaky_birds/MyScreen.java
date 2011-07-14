package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds;

import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.game.Updatable;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.renderer2D.Renderer2D;

public class MyScreen extends Screen 
{
	World			m_world;
	Renderer2D	m_worldRenderer;
	
	/**
	 * Constructor.
	 * 
	 * @param game				host game
	 */
	public MyScreen( Game game ) 
	{
		super(game);
		
		// create the game world
		m_world = new World();

		// register the updatables
		addUpdatable( m_world );
		
		// create the renderer
		m_worldRenderer = new Renderer2D( game.getGraphics() );
		m_world.attachView( m_worldRenderer );
	}


	@Override
	public void present( float deltaTime ) 
	{	
		// draw the world contents
		m_worldRenderer.draw();
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub

	}

}
