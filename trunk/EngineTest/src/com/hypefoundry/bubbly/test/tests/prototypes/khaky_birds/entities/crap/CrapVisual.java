/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;

/**
 * Crap visualisation
 * @author azagor
 *
 */
public class CrapVisual extends EntityVisual 
{
	private AnimationPlayer	m_animationPlayer;
	
	private int 			ANIM_FALL;
	private int 			ANIM_HIT;
	private Crap            m_crap;
	float                  m_scaleFactor = 1.f;
	float                  m_scaleCounter = 0.1f;

	/**
	 * Constructor.
	 * 
	 * @param resMgr 
	 * @param entity
	 */
	public CrapVisual( ResourceManager resMgr, Entity entity ) 
	{
		super(entity);
		m_crap = (Crap)entity;
		
		// load animations
			Animation fallingShit = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/fallingShit.xml");
			Animation hittingShit = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/hittingShit.xml");	
			
			// create an animation player
			m_animationPlayer = new AnimationPlayer();
			ANIM_FALL = m_animationPlayer.addAnimation( fallingShit );
			ANIM_HIT = m_animationPlayer.addAnimation( hittingShit );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_crap.getPosition();
		BoundingShape bs = m_crap.getBoundingShape();
		
		float width;
		float hight;
		
		width =	bs.getWidth();
		hight = bs.getHeight();
		
		
		if( m_crap.m_state == Crap.State.Falling || m_crap.m_state == Crap.State.Hitting )
		{
			m_animationPlayer.select(ANIM_FALL);
			
			if (m_scaleFactor > 0)
			{
				m_scaleFactor = m_scaleFactor - m_scaleCounter;
			}
			if (m_scaleFactor <= 0.3f)
			{
				m_crap.canHit();
			}
			
			
			width = width *  m_scaleFactor;
			hight = hight *  m_scaleFactor;
			
		}
		else if ( m_crap.m_state == Crap.State.Splat )
		{
			m_animationPlayer.select(ANIM_HIT);
		}
		
		batcher.drawSprite( pos.m_x, pos.m_y, width, hight, m_animationPlayer.getTextureRegion( deltaTime ) );

	}

}
