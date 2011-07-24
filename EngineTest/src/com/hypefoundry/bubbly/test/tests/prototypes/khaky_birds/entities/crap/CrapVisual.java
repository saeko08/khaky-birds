/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;

/**
 * Crap visualisation
 * @author azagor
 *
 */
public class CrapVisual extends EntityVisual 
{

	private Pixmap m_pixmap;
	private Pixmap m_pixmapHit;
	private Crap m_crap;

	/**
	 * Constructor.
	 * 
	 * @param graphics 
	 * @param entity
	 */
	public CrapVisual( Graphics graphics, Entity entity ) 
	{
		super(entity);
		//pixmaps below must have same width and height
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/crap.png", PixmapFormat.ARGB4444 );
		m_pixmapHit = graphics.newPixmap( "khaky_birds_prototype/crap_hit.png", PixmapFormat.ARGB4444 );
		m_crap = (Crap)entity;
		
		// set the bounds based on the visual representation used
		float width = m_pixmap.getWidth();
		float height = m_pixmap.getHeight();
		m_crap.setBoundingBox( new BoundingBox( -width / 2, -height / 2, -1, width / 2, height / 2, 1 ) );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
	Vector3 pos = m_crap.getPosition();
		
		if (m_crap.pedestrianHit == false)
		{
			float width = m_pixmap.getWidth();
			float height = m_pixmap.getHeight();
			
			graphics.drawPixmap( m_pixmap, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}
		else
		{
			float width = m_pixmapHit.getWidth();
			float height = m_pixmapHit.getHeight();
			
			graphics.drawPixmap( m_pixmapHit, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}

	}

}
