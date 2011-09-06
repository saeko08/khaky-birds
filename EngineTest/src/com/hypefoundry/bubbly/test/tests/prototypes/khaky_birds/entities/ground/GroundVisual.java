/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.ground;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * Visual representation of the ground entity.
 * 
 * @author paksas
 *
 */
public class GroundVisual extends EntityVisual 
{
	private TextureRegion		m_pixmap;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public GroundVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_pixmap = resMgr.getResource( TextureRegion.class, "khaky_birds_prototype/textures/ground.xml" );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingShape bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
	}
}
