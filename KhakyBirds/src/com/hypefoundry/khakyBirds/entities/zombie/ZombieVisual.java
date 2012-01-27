/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.zombie;

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
 * @author azagor
 *
 */
public class ZombieVisual extends EntityVisual 
{

	private AnimationPlayer	m_animationPlayer;
	private Zombie			m_zombie;
	
	private int 			ANIM_OBSERVE;
	private int 			ANIM_WANDER;
	//private int 			ANIM_SHIT;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public ZombieVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);

		m_zombie = (Zombie)entity;
		
		// load animations
		Animation observingZombie = resMgr.getResource( Animation.class, "animations/zombie/observing.xml");
		Animation wanderingZombie = resMgr.getResource( Animation.class, "animations/zombie/wandering.xml");
		//we may add this animation in the future
		//Animation shittedZombie = resMgr.getResource( Animation.class, "animations/zombie/shitted.xml");		
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_OBSERVE = m_animationPlayer.addAnimation( observingZombie );
		ANIM_WANDER = m_animationPlayer.addAnimation( wanderingZombie );
		//ANIM_SHIT = m_animationPlayer.addAnimation( shittedZombie );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_zombie.getPosition();
		BoundingBox bs = m_zombie.getBoundingShape();
		
		if ( m_zombie.m_state == Zombie.State.Wander || m_zombie.m_state == Zombie.State.TurnAround ||m_zombie.m_state == Zombie.State.Chasing )
		{
			m_animationPlayer.select(ANIM_WANDER);
		}
		else
		{
			m_animationPlayer.select(ANIM_OBSERVE);
		}
		
		batcher.drawSprite( pos, bs, m_zombie.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );
	}


}
