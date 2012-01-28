/**
 * 
 */
package com.hypefoundry.kabloons.entities.buzzSaw;

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
public class BuzzSawVisual extends EntityVisual 
{
	private BuzzSaw					m_buzzSaw;
	private AnimationPlayer			m_player;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public BuzzSawVisual( ResourceManager resMgr,  Entity buzzSawEntity ) 
	{
		super( buzzSawEntity );
		
		m_buzzSaw = (BuzzSaw)buzzSawEntity;
		
		m_player = new AnimationPlayer();
		Animation buzzSawAnim = resMgr.getResource( Animation.class, m_buzzSaw.m_animPath );
		m_player.addAnimation( buzzSawAnim );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime )
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		switch( m_buzzSaw.m_state )
		{
			case Running:
			{
				batcher.drawSprite( pos, bs, m_player.getTextureRegion( deltaTime ) );
				break;
			}
			
			case SwitchedOff:
			{
				batcher.drawSprite( pos, bs, m_player.getTextureRegion( 0.0f ) );
				break;
			}
		}

	}

}
