/**
 * 
 */
package com.hypefoundry.kabloons.entities.fan;

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
public class FanVisual extends EntityVisual 
{

	private Fan					m_fan;
	private	AnimationPlayer		m_animPlayer;
	
	private int					m_onAnimIdx;
	private int					m_offAnimIdx;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param fanEntity
	 */
	public FanVisual( ResourceManager resMgr, Entity fanEntity ) 
	{
		super( fanEntity );
		
		m_fan = (Fan)fanEntity;
		
		m_animPlayer = new AnimationPlayer();
		
		Animation onAnim = resMgr.getResource( Animation.class, m_fan.m_onAnim );
		m_onAnimIdx = m_animPlayer.addAnimation( onAnim );
		
		Animation offAnim = resMgr.getResource( Animation.class, m_fan.m_offAnim );
		m_offAnimIdx = m_animPlayer.addAnimation( offAnim );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		switch( m_fan.m_state )
		{
			case On:
			{
				m_animPlayer.select( m_onAnimIdx );
				break;
			}
			
			case Off:
			{
				m_animPlayer.select( m_offAnimIdx );
				break;
			}
		}
		
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( deltaTime ) );
	}

}
