/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.decoration;

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

/**
 * @author Paksas
 *
 */
public class AnimatedDecorationVisual extends EntityVisual
{
	private AnimationPlayer			m_player = new AnimationPlayer();
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public AnimatedDecorationVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		AnimatedDecoration decoration = (AnimatedDecoration)entity;
		
		Animation animation = resMgr.getResource( Animation.class, decoration.m_animationPath );
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
	}

}
