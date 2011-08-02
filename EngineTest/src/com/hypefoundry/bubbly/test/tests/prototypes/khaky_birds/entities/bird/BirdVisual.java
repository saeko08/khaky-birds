package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * Visual representation of a bird entity.
 * 
 * @author paksas
 *
 */
public class BirdVisual extends EntityVisual 
{
	private TextureRegion	m_pixmap;
	private TextureRegion	m_pixmapCrosshair;
	private Bird			m_bird;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public BirdVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);

		m_bird = (Bird)entity;
		
		Texture atlas = resMgr.getResource( Texture.class, "khaky_birds_prototype/atlas.png" );
		m_pixmapCrosshair = new TextureRegion( atlas, 619, 0, 40, 73 );
		m_pixmap = new TextureRegion( atlas, 662, 0, 40, 57 );
	}

	@Override
	public void draw( SpriteBatcher batcher ) 
	{
		Vector3 pos = m_bird.getPosition();
		BoundingShape bs = m_bird.getBoundingShape();
		
		if ( m_bird.crosshairInitialized )
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmapCrosshair );
		}
		else
		{
			batcher.drawSprite(pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
	}

}
