/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
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
	 * @param resMgr
	 * @param entity
	 */
	public PedestrianVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_pedestrian = (Pedestrian)entity;
		
		Texture atlas = resMgr.getResource( Texture.class, "khaky_birds_prototype/atlas.png" );
		m_pixmap = new TextureRegion( atlas, 719, 91, 43, 43 );
		m_pixmapHit = new TextureRegion( atlas, 668, 91, 43, 43 );
	}

	@Override
	public void draw( SpriteBatcher batcher ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		BoundingShape bs = m_pedestrian.getBoundingShape();
	
		if ( m_pedestrian.m_hitWithShit == false )
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
		else
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmapHit );
		}
			
	}

}
