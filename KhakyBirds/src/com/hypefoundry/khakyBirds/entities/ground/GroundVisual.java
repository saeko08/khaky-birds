/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.ground;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
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
		
		Ground ground = (Ground)entity;
		
		m_pixmap = resMgr.getResource( TextureRegion.class, ground.m_texturePath );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
	}
}
