/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.crap;

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
 * @author azagor
 *
 */
public class GranadeCrapVisual extends EntityVisual 
{
	private AnimationPlayer			m_animationPlayer;
	private int 					ANIM_FALL;
	private int 					ANIM_HIT;
	
	private ParticleSystemPlayer	m_crapHitFx;
	
	private GranadeCrap           	m_crap;
	float                  			m_scaleFactor = 1.f;
	float                  			m_scaleCounter = 0.05f;

	/**
	 * Constructor.
	 * 
	 * @param resMgr 
	 * @param entity
	 */
	public GranadeCrapVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);
		m_crap = (GranadeCrap)entity;
		
		// load animations
		Animation fallingShit = resMgr.getResource( Animation.class, "animations/fallingShit.xml");
		Animation hittingShit = resMgr.getResource( Animation.class, "animations/hittingShit.xml");	
			
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_FALL = m_animationPlayer.addAnimation( fallingShit );
		ANIM_HIT = m_animationPlayer.addAnimation( hittingShit );
		
		// load the fx
		ParticleSystem crapHitFx = resMgr.getResource( ParticleSystem.class, "fx/crapHit.xml" );
		m_crapHitFx = new ParticleSystemPlayer( crapHitFx, false );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_crap.getPosition();
		BoundingBox bs = m_crap.getBoundingShape();
		
		if( m_crap.m_state == GranadeCrap.State.Falling || m_crap.m_state == GranadeCrap.State.Hitting )
		{
			m_animationPlayer.select(ANIM_FALL);
			
			if (m_scaleFactor > 0)
			{
				m_scaleFactor = m_scaleFactor - m_scaleCounter;
			}
			if (m_scaleFactor <= 0.8f && m_scaleFactor >= 0.7f)
			{
				m_crap.canHit(true);
			}
			if (m_scaleFactor < 0.6f)
			{
				m_crap.canHit(false);
			}
			
			float width =	bs.getWidth();
			float height = 	bs.getHeight();	
			width *= m_scaleFactor;
			height *= m_scaleFactor;
			m_crap.setBoundingBox( width, height );
		}
		else if ( m_crap.m_state == GranadeCrap.State.Splat )
		{
			m_animationPlayer.select(ANIM_HIT);
		}
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_animationPlayer.getTextureRegion( deltaTime ) );
		
		// draw the fx
		if ( m_crap.m_state == GranadeCrap.State.Splat )
		{
			m_crapHitFx.draw( pos.m_x, pos.m_y, batcher, deltaTime );
		}
	}

}
