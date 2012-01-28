/**
 * 
 */
package com.hypefoundry.kabloons.entities.toggle;

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
public class ToggleVisual extends EntityVisual 
{

	private Toggle	 			m_toggle;
	private TextureRegion		m_onTexture;
	private TextureRegion		m_offTexture;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param exitDoorEntity
	 */
	public ToggleVisual( ResourceManager resMgr, Entity toggleEntity )
	{
		super( toggleEntity );
		
		m_toggle = (Toggle)toggleEntity;
		
		m_onTexture = resMgr.getResource( TextureRegion.class, m_toggle.m_onTexturePath );
		m_offTexture = resMgr.getResource( TextureRegion.class, m_toggle.m_offTexturePath );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		if ( m_toggle.m_controlledEntity.isSwitchedOn() )
		{
			batcher.drawSprite( pos, bs, m_onTexture );
		}
		else
		{
			batcher.drawSprite( pos, bs, m_offTexture );
		}
	}
}
