/**
 * 
 */
package com.hypefoundry.kabloons.entities.help;

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
public class HelpVisual extends EntityVisual 
{
	private TextureRegion 		m_image;
	private Help				m_help;
	

	public HelpVisual( ResourceManager resMgr, Entity helpEntity ) 
	{
		super(helpEntity);
		
		m_help = (Help)helpEntity;
		m_image = resMgr.getResource( TextureRegion.class, m_help.m_imagePath );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		if ( m_help.m_isVisible )
		{
			Vector3 pos = m_entity.getPosition();
			BoundingBox bs = m_entity.getBoundingShape();
			batcher.drawSprite( pos, bs, m_image );
		}
	}
}
