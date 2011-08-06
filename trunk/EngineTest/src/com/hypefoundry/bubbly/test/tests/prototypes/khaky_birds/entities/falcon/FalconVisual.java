/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
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
	 * @param resMgr
	 * @param entity
	 */
	public FalconVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);
		m_falcon = (Falcon)entity;
		
		Texture atlas = resMgr.getResource( Texture.class, "khaky_birds_prototype/atlas.png" );
		m_pixmap = new TextureRegion( atlas, 620, 88, 40, 60 );
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
