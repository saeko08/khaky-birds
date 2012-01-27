package com.hypefoundry.khakyBirds.entities.bird;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;


/**
 * Visual representation of a bird entity.
 * 
 * @author paksas
 *
 */
public class BirdVisual extends EntityVisual 
{
	private AnimationPlayer	m_animationPlayer;
	private Bird			m_bird;
	
	private int 			ANIM_IDLE;
	private int 			ANIM_FLY;
	private int 			ANIM_SHIT;
	private int 			ANIM_FLY_SHIT;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public BirdVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);

		m_bird = (Bird)entity;
		
		// load animations
		Animation idleBird = resMgr.getResource( Animation.class, "animations/idleBird.xml");
		Animation flyingBird = resMgr.getResource( Animation.class, "animations/flyingBird.xml");
		Animation shittingBird = resMgr.getResource( Animation.class, "animations/shittingBird.xml");	
		Animation flyingShittingBird = resMgr.getResource( Animation.class, "animations/flyingShittingBird.xml");		
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_IDLE = m_animationPlayer.addAnimation( idleBird );
		ANIM_FLY = m_animationPlayer.addAnimation( flyingBird );
		ANIM_SHIT = m_animationPlayer.addAnimation( shittingBird );
		ANIM_FLY_SHIT = m_animationPlayer.addAnimation( flyingShittingBird );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_bird.getPosition();
		BoundingBox bs = m_bird.getBoundingShape();
		
		if( m_bird.m_state == Bird.State.Shitting )
		{
			m_animationPlayer.select(ANIM_SHIT);
		}
		else if( m_bird.m_state == Bird.State.FlyingShitting )
		{
			m_animationPlayer.select(ANIM_FLY_SHIT);
		}
		else if ( m_bird.m_state == Bird.State.Flying || m_bird.m_state == Bird.State.Landing || m_bird.m_state == Bird.State.Jumping)
		{
			m_animationPlayer.select(ANIM_FLY);
		}
		else
		{
			m_animationPlayer.select(ANIM_IDLE);
		}
		
		batcher.drawSprite( pos, bs, m_bird.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );
	}

}
