/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.falcon;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;

/**
 * Visual representation of a falcon entity.
 * @author azagor
 *
 */
public class FalconVisual extends EntityVisual 
{
	private AnimationPlayer	    m_animationPlayer;
	private Falcon				m_falcon;
	private int 			    ANIM_FLY;
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public FalconVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);
		m_falcon = (Falcon)entity;
		
		// load animations
		Animation FlyingFalcon = resMgr.getResource( Animation.class, "animations/FlyingFalcon.xml");
		
		// create an animation player
			m_animationPlayer = new AnimationPlayer();
			ANIM_FLY = m_animationPlayer.addAnimation( FlyingFalcon );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_falcon.getPosition();
		BoundingBox bs = m_falcon.getBoundingShape();
		
		m_animationPlayer.select(ANIM_FLY);
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_falcon.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );
	}

}
