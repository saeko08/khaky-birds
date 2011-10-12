/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hideout;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;

/**
 * @author azagor
 *
 */
public class HideoutVisual extends EntityVisual 
{
private TextureRegion		m_pixmap;
private TextureRegion		m_pixmapDestroyed;
private Hideout				m_hideout;

	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public HideoutVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		m_hideout = (Hideout)entity;
		m_pixmap = resMgr.getResource( TextureRegion.class, "textures/hideouts/hideout1.xml" );
		m_pixmapDestroyed = resMgr.getResource( TextureRegion.class, "textures/hideouts/hideout1_destroyed.xml" );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		if(m_hideout.m_isDemolished == false)
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmap );
		}
		else
		{
			batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pixmapDestroyed );
		}
	}

}
