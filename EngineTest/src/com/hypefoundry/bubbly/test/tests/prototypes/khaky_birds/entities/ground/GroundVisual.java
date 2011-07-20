/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;


/**
 * Visual representation of the ground entity.
 * 
 * @author paksas
 *
 */
public class GroundVisual extends EntityVisual 
{
	private Pixmap		m_pixmap;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public GroundVisual( Graphics graphics, Entity entity ) 
	{
		super( entity );
		
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/background.png", PixmapFormat.RGB565 );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		graphics.drawPixmap( m_pixmap, 0, 0 );
	}
}
