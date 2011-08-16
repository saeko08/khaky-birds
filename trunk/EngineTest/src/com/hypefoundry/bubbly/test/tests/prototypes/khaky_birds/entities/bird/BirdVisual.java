package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

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
 * Visual representation of a bird entity.
 * 
 * @author paksas
 *
 */
public class BirdVisual extends EntityVisual 
{
	private AnimationPlayer	m_animationPlayer;
	private Bird			m_bird;
	
	private int 			ANIM_JUMP;
	private int 			ANIM_FLY;
	private int 			ANIM_SHIT;
	
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
		Animation jumpingBird = resMgr.getResource( Animation.class, "khaky_birds_prototype/jumpingBird.xml");
		Animation flyingBird = resMgr.getResource( Animation.class, "khaky_birds_prototype/flyingBird.xml");
		Animation shittingBird = resMgr.getResource( Animation.class, "khaky_birds_prototype/shittingBird.xml");		
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_JUMP = m_animationPlayer.addAnimation( jumpingBird );
		ANIM_FLY = m_animationPlayer.addAnimation( flyingBird );
		ANIM_SHIT = m_animationPlayer.addAnimation( shittingBird );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_bird.getPosition();
		BoundingShape bs = m_bird.getBoundingShape();
		
		if( m_bird.m_state == Bird.State.Shitting )
		{
			m_animationPlayer.select(ANIM_SHIT);
		}
		else if ( m_bird.m_state == Bird.State.Flying || m_bird.m_state == Bird.State.Landing )
		{
			m_animationPlayer.select(ANIM_FLY);
		}
		else
		{
			m_animationPlayer.select(ANIM_JUMP);
		}
		
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_bird.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );
	}

}
