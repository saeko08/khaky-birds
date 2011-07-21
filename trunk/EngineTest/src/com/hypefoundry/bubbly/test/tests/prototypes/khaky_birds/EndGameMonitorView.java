/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShock;
import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.WorldView;

/**
 * When the bird dies, this view informs the player about it.
 * 
 * @author azagor
 *
 */
public class EndGameMonitorView implements WorldView 
{
	private Graphics 		m_graphics;
	private boolean 		m_endGame		= false;
	
	private Pixmap			m_endGamePixmap	= null;

	/**
	 * Constructor.
	 */
	public EndGameMonitorView( Graphics graphics ) 
	{
		m_graphics = graphics;
		m_endGamePixmap = m_graphics.newPixmap( "khaky_birds_prototype/endGame.png", PixmapFormat.ARGB4444 );
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
	}

	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		if ( Bird.class.isInstance( entity) )
		{
			m_endGame = true;
		}
	}

	public void draw() 
	{
		if ( m_endGame )
		{
			m_graphics.drawPixmap( m_endGamePixmap, 0, 0 );
		}
	}

}
