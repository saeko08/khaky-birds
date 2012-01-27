/**
 * 
 */
package com.hypefoundry.kabloons.entities.background;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class BackgroundVisual extends EntityVisual 
{
	private TextureRegion		m_pixmap;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param backgroundEntity
	 */
	public BackgroundVisual( ResourceManager resMgr, Entity backgroundEntity )
	{
		super( backgroundEntity );
		
		Background background = (Background)backgroundEntity;
		
		m_pixmap = resMgr.getResource( TextureRegion.class, background.m_path );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos, bs, m_pixmap );
	}

}
