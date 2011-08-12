/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
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
	 * @param resMgr 
	 * @param entity
	 */
	public CrapVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);
		m_crap = (Crap)entity;
		
		Texture atlas = resMgr.getResource( Texture.class, "khaky_birds_prototype/atlas.png" );
		m_pixmap = new TextureRegion( atlas, 713, 0, 30, 30 );
		m_pixmapHit = new TextureRegion( atlas, 751, 0, 30, 30 );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
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
