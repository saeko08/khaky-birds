package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.cables;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;


/**
 * Visual representation of an electric cables entity.
 * @author paksas
 *
 */
public class ElectricCablesVisual extends EntityVisual 
{
	private Pixmap		m_pixmap;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public ElectricCablesVisual( Graphics graphics, Entity entity )
	{
		super( entity );
		
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/cables.png", PixmapFormat.ARGB4444 );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		graphics.drawPixmap( m_pixmap, 0, 0 );
	}

}
