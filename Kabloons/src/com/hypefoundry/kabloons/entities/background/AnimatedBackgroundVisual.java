/**
 * 
 */
package com.hypefoundry.kabloons.entities.background;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author Paksas
 *
 */
public class AnimatedBackgroundVisual extends EntityVisual 
{
	private AnimationPlayer			m_player = new AnimationPlayer();
	private float					m_lifeTimer;
	private boolean					m_lifeTimerEnabled;
	private World					m_world;
	private boolean					m_initialUpdate = true;
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param resMgr
	 * @param entity
	 */
	public AnimatedBackgroundVisual( World world, ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		m_world = world;
		
		AnimatedBackground decoration = (AnimatedBackground)entity;
		
		Animation animation = resMgr.getResource( Animation.class, decoration.m_path );
		
		if ( animation.isLooped() == false )
		{
			m_lifeTimer = animation.getDuration();
			m_lifeTimerEnabled = true;
		}
		else
		{
			m_lifeTimerEnabled = false;
		}
		
		int animIdx = m_player.addAnimation( animation );
		m_player.select( animIdx );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_entity.getPosition();
		BoundingBox bs = m_entity.getBoundingShape();

		TextureRegion region = m_player.getTextureRegion( deltaTime );
		batcher.drawSprite( pos, bs, region );
		
		// decrease the life timer if applicable
		if ( m_lifeTimerEnabled && !m_initialUpdate )
		{
			m_lifeTimer -= deltaTime;
			if ( m_lifeTimer <= 0.0f )
			{
				// the animation has ended, we no longer need to display it
				m_world.removeEntity( m_entity );
			}
		}
		
		m_initialUpdate = false;
	}
}
