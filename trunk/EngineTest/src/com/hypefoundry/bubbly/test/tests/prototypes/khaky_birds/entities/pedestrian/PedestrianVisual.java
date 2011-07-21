/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;

/**
 * Visual representation of a pedestrian entity.
 * 
 * @author paksas
 *
 */
public class PedestrianVisual extends EntityVisual 
{
	private Pedestrian	m_pedestrian;
	private Pixmap		m_pixmap;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public PedestrianVisual( Graphics graphics, Entity entity ) 
	{
		super( entity );
		
		m_pedestrian = (Pedestrian)entity;
		
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/pedestrian.png", PixmapFormat.ARGB4444 );
		
		// set the bounds based on the visual representation used
		float width = m_pixmap.getWidth();
		float height = m_pixmap.getHeight();
		m_pedestrian.setBoundingBox( new BoundingBox( 0, 0, -1, width, height, 1 ) );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		graphics.drawPixmap( m_pixmap, (int)pos.m_x, (int)pos.m_y );
	}

}
