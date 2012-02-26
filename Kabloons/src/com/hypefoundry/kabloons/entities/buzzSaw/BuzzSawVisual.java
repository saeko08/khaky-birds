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
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BuzzSawVisual extends EntityVisual 
{
	private BuzzSaw					m_buzzSaw;
	private AnimationPlayer			m_animPlayer;
	private ParticleSystemPlayer	m_fxPlayer;
	
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
		
		// animation
		if ( m_buzzSaw.m_animPath.length() > 0 )
		{
			m_animPlayer = new AnimationPlayer();
			Animation buzzSawAnim = resMgr.getResource( Animation.class, m_buzzSaw.m_animPath );
			m_animPlayer.addAnimation( buzzSawAnim );
		}
		
		// effect
		if ( m_buzzSaw.m_fxPath.length() > 0 )
		{
			ParticleSystem fx = resMgr.getResource( ParticleSystem.class, m_buzzSaw.m_fxPath );
			m_fxPlayer = new ParticleSystemPlayer( fx, true );
		}
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
				if ( m_animPlayer != null )
				{
					batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( deltaTime ) );
				}
				
				if ( m_fxPlayer != null )
				{
					m_fxPlayer.enableEmitters( true );
				}
				break;
			}
			
			case SwitchedOff:
			{
				if ( m_animPlayer != null )
				{
					batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( 0.0f ) );
				}
				
				if ( m_fxPlayer != null )
				{
					m_fxPlayer.enableEmitters( false );
				}
				break;
			}
		}
		
		if ( m_fxPlayer != null )
		{
			m_fxPlayer.draw( pos.m_x, pos.m_y, batcher, deltaTime );
		}

	}

}
