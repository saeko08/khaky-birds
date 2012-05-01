/**
 * 
 */
package com.hypefoundry.kabloons.entities.webLink;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.background.Background;

/**
 * @author Paksas
 *
 */
public class WebLingVisual extends EntityVisual 
{

	private TextureRegion		m_pixmap;
	private WebLink 			m_demoEnd;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param backgroundEntity
	 */
	public WebLingVisual( ResourceManager resMgr, Entity demoEndEntity )
	{
		super( demoEndEntity );
		
		m_demoEnd = (WebLink)demoEndEntity;
		m_pixmap = resMgr.getResource( TextureRegion.class, m_demoEnd.m_imagePath );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		batcher.drawSprite( pos, bs, m_pixmap );
	}
}