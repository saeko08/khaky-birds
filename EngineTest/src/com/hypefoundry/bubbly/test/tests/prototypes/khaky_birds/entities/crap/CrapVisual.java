/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;

/**
 * Crap visualisation
 * @author azagor
 *
 */
public class CrapVisual extends EntityVisual 
{

	private TextureRegion m_pixmap;
	private TextureRegion m_pixmapHit;
	private Crap m_crap;

	/**
	 * Constructor.
	 * 
	 * @param graphics 
	 * @param entity
	 */
	public CrapVisual( Entity entity ) 
	{
		super(entity);
		m_crap = (Crap)entity;
	}

	@Override
	public void draw( SpriteBatcher batcher) 
	{
		Vector3 pos = m_crap.getPosition();
		BoundingShape bs = m_crap.getBoundingShape();
		
		if (m_crap.pedestrianHit == false)
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
		else
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmapHit );
		}

	}

}
