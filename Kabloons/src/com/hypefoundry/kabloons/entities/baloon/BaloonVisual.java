/**
 * 
 */
package com.hypefoundry.kabloons.entities.baloon;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BaloonVisual extends EntityVisual 
{
	private Baloon				m_baloon;
	private	AnimationPlayer		m_animPlayer;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param baloonEntity
	 */
	public BaloonVisual( ResourceManager resMgr, Entity baloonEntity ) 
	{
		super( baloonEntity );
		
		m_baloon = (Baloon)baloonEntity;
		
		m_animPlayer = new AnimationPlayer();
		Animation floatingAnim = resMgr.getResource( Animation.class, m_baloon.m_floatingAnim );
		m_animPlayer.addAnimation( floatingAnim );
		
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		if( m_baloon.m_state == Baloon.State.Flying )
		{
			Vector3 pos = m_entity.getPosition();
			BoundingBox bs = m_entity.getBoundingShape();
			
			batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( deltaTime ) );
		}
	}

}
