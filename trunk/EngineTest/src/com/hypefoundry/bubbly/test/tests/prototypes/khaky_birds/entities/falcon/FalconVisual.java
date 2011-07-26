/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;

/**
 * Visual representation of a falcon entity.
 * @author azagor
 *
 */
public class FalconVisual extends EntityVisual 
{
	private Pixmap		m_pixmap;
	private Pixmap		m_pixmapFromRight;
	private Falcon		m_falcon;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public FalconVisual( Graphics graphics, Entity entity ) 
	{
		super(entity);
		
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/falcon_left.png", PixmapFormat.ARGB4444 );
		m_pixmapFromRight = graphics.newPixmap( "khaky_birds_prototype/falcon_right.png", PixmapFormat.ARGB4444 );
		m_falcon = (Falcon)entity;
		
		// set the bounds based on the visual representation used
		float width = m_pixmap.getWidth();
		float height = m_pixmap.getHeight();
		m_falcon.setBoundingBox( new BoundingBox( -width / 2, -height / 2, -1, width / 2, height / 2, 1 ) );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		Vector3 pos = m_falcon.getPosition();
		
		if (m_falcon.m_flyingFromLeft == true)
		{
			float width = m_pixmap.getWidth();
			float height = m_pixmap.getHeight();
			
			graphics.drawPixmap( m_pixmap, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}
		else
		{
			float width = m_pixmapFromRight.getWidth();
			float height = m_pixmapFromRight.getHeight();
			
			graphics.drawPixmap( m_pixmapFromRight, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}
	}

}
