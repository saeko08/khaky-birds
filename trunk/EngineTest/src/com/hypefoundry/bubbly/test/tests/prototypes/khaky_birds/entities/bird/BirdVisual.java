package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;


/**
 * Visual representation of a bird entity.
 * 
 * @author paksas
 *
 */
public class BirdVisual extends EntityVisual 
{
	private Pixmap		m_pixmap;
	private Pixmap		m_pixmapCrosshair;
	private Bird		m_bird;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public BirdVisual( Graphics graphics, Entity entity ) 
	{
		super(entity);
		//pixmaps below must have same width and height
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/bird.png", PixmapFormat.ARGB4444 );
		m_pixmapCrosshair = graphics.newPixmap( "khaky_birds_prototype/bird_cross_hair.png", PixmapFormat.ARGB4444 );
		m_bird = (Bird)entity;
		
		// set the bounds based on the visual representation used
		float width = m_pixmap.getWidth();
		float height = m_pixmap.getHeight();
		m_bird.setBoundingBox( new BoundingBox( -width / 2, -height / 2, -1, width / 2, height / 2, 1 ) );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		Vector3 pos = m_bird.getPosition();
		
		if (m_bird.crosshairInitialized == false)
		{
			float width = m_pixmap.getWidth();
			float height = m_pixmap.getHeight();
			
			graphics.drawPixmap( m_pixmap, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}
		else
		{
			float width = m_pixmapCrosshair.getWidth();
			float height = m_pixmapCrosshair.getHeight();
			
			graphics.drawPixmap( m_pixmapCrosshair, (int)( pos.m_x - width / 2 ), (int)( pos.m_y - height / 2 ) );
		}
	}

}
