/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;

/**
 * Visual representation of a falcon entity.
 * @author azagor
 *
 */
public class FalconVisual extends EntityVisual 
{
	private TextureRegion		m_pixmap;
	private Falcon				m_falcon;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param entity
	 */
	public FalconVisual( Entity entity ) 
	{
		super(entity);
		
		m_falcon = (Falcon)entity;
	}

	@Override
	public void draw( SpriteBatcher batcher ) 
	{
		Vector3 pos = m_falcon.getPosition();
		BoundingShape bs = m_falcon.getBoundingShape();
		
		if (m_falcon.m_flyingFromLeft == true)
		{			
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
		else
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), 180.0f, m_pixmap );
		}
	}

}
