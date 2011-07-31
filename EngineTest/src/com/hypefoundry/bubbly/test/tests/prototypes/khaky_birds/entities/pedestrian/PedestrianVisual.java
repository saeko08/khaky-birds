/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;

/**
 * Visual representation of a pedestrian entity.
 * 
 * @author paksas
 *
 */
public class PedestrianVisual extends EntityVisual 
{
	private Pedestrian			m_pedestrian;
	private TextureRegion		m_pixmap;
	private TextureRegion		m_pixmapHit;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public PedestrianVisual( Entity entity ) 
	{
		super( entity );
		
		m_pedestrian = (Pedestrian)entity;
	}

	@Override
	public void draw( SpriteBatcher batcher ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		BoundingShape bs = m_pedestrian.getBoundingShape();
	
		if (m_pedestrian.hitWithShit == false)
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
		else
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmapHit );
		}
			
	}

}
