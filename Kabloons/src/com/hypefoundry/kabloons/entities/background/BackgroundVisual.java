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
	private Background 			m_background;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param backgroundEntity
	 */
	public BackgroundVisual( ResourceManager resMgr, Entity backgroundEntity )
	{
		super( backgroundEntity );
		
		m_background = (Background)backgroundEntity;
		m_pixmap = resMgr.getResource( TextureRegion.class, m_background.m_path );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		if ( m_background.m_rotationSpeed <= 0.0f )
		{
			batcher.drawSprite( pos, bs, m_pixmap );
		}
		else
		{
			float facing = m_background.getFacing();
			facing += m_background.m_rotationSpeed * deltaTime;
			m_background.setFacing( facing );
			batcher.drawSprite( pos, bs, facing, m_pixmap );
		}
	}
}
