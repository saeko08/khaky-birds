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
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;


/**
 * @author Paksas
 *
 */
public class FanVisual extends EntityVisual 
{

	private Fan						m_fan;
	private	AnimationPlayer			m_animPlayer;
	private ParticleSystemPlayer	m_windPlayer;
	private float					m_rotationSpeed = 0.3f;
	
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
		
		// load and setup an animation
		if ( m_fan.m_anim.length() > 0 )
		{
			m_animPlayer = new AnimationPlayer();
			Animation onAnim = resMgr.getResource( Animation.class, m_fan.m_anim );
			m_animPlayer.addAnimation( onAnim );
		}
		
		// load and setup the wind effect
		ParticleSystem windParticleSystem = resMgr.getResource( ParticleSystem.class, m_fan.m_windFx );
		m_windPlayer = new ParticleSystemPlayer( windParticleSystem, true);
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();
		
		if ( m_animPlayer != null )
		{
			// play the animation
			if ( m_rotationSpeed < 1.0f )
			{
				// appear to be starting the fan just after it's been created
				m_rotationSpeed += deltaTime * 0.1f;
			}
			else
			{
				m_rotationSpeed = 1.0f;
			}
			batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( deltaTime * m_rotationSpeed ) );
		}
		
		// play the wind effect
		m_windPlayer.draw( pos.m_x, pos.m_y, batcher, deltaTime * m_rotationSpeed );
	}

}
