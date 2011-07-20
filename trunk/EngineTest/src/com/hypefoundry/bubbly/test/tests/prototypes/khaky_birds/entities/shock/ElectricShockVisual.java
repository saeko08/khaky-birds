package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.core.Graphics;
import com.hypefoundry.engine.core.Pixmap;
import com.hypefoundry.engine.core.Graphics.PixmapFormat;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;


/**
 * Visual representation of an electric shock entity.
 * 
 * @author paksas
 *
 */
public class ElectricShockVisual extends EntityVisual 
{
	private ElectricShock	m_electricShock;
	private Pixmap			m_pixmap;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param screenHeight
	 * @param entity
	 */
	public ElectricShockVisual( Graphics graphics, Entity entity ) 
	{
		super( entity );
		
		// TODO: extremely slow introduce a resources manager
		m_electricShock = (ElectricShock)entity;
		m_pixmap = graphics.newPixmap( "khaky_birds_prototype/shock.png", PixmapFormat.ARGB4444  );
		
		// set bounds of the entity based on its graphical representation
		float width = m_pixmap.getWidth();
		float height = m_pixmap.getHeight();
		m_electricShock.setBoundingBox( new BoundingBox( -width / 2, -height / 2, -1, width / 2, height / 2, 1 ) );
		
		// set the position of the shock
		Vector3 pos = m_electricShock.getPosition();
		float screenHeight = graphics.getHeight();
		m_electricShock.setPosition( pos.m_x - width / 2, screenHeight + height / 2, pos.m_z );
	}

	@Override
	public void draw( Graphics graphics ) 
	{
		Vector3 pos = m_electricShock.getPosition();
		graphics.drawPixmap( m_pixmap, (int)pos.m_x, (int)pos.m_y );
	}

}
