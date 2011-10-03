/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.perkPedestrian;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author azagor
 *
 */
public class PerkPedestrianVisual extends EntityVisual 
{
	private PerkPedestrian			m_perkPedestrian;
	private AnimationPlayer		m_animationPlayer;

	private int 				ANIM_WALK;
	private int 				ANIM_WIPE_SHIT_OFF;
	private int 				ANIM_OBSERVE;
	private int 				ANIM_SHOOT;
	private int 				ANIM_AIM;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public PerkPedestrianVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_perkPedestrian = (PerkPedestrian)entity;
		
		// load animations
		Animation walking = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/perkPedestrian/walking.xml");
		Animation wipeShitOff= resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/perkPedestrian/wipeShitOff.xml");
		Animation observing = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/perkPedestrian/observing.xml");
		Animation shooting = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/perkPedestrian/shooting.xml");
		Animation aiming = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/perkPedestrian/aiming.xml");
		
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_WALK = m_animationPlayer.addAnimation(walking );
		ANIM_WIPE_SHIT_OFF = m_animationPlayer.addAnimation( wipeShitOff );
		ANIM_OBSERVE = m_animationPlayer.addAnimation( observing );
		ANIM_SHOOT = m_animationPlayer.addAnimation( shooting );
		ANIM_AIM = m_animationPlayer.addAnimation( aiming );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_perkPedestrian.getPosition();
		BoundingBox bs = m_perkPedestrian.getBoundingShape();
	
		// select an animation appropriate to the state the pedestrian's in
		if ( m_perkPedestrian.m_hitWithShit == true  ||  m_perkPedestrian.m_state == PerkPedestrian.State.Shitted)
		{
			m_animationPlayer.select( ANIM_WIPE_SHIT_OFF );
			
		}
		else if (m_perkPedestrian.m_state == PerkPedestrian.State.Observe ||  m_perkPedestrian.m_state == PerkPedestrian.State.Eaten)
		{
			m_animationPlayer.select( ANIM_OBSERVE );
		}
		else if (m_perkPedestrian.m_state == PerkPedestrian.State.Shooting)
		{
			m_animationPlayer.select( ANIM_SHOOT );
		}
		else if (m_perkPedestrian.m_state == PerkPedestrian.State.Aiming)
		{
			m_animationPlayer.select( ANIM_AIM );
		}
		else
		{
			m_animationPlayer.select( ANIM_WALK );
		}
		
		// draw the pedestrian
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_perkPedestrian.getFacing(), m_animationPlayer.getTextureRegion(m_perkPedestrian, deltaTime ) );
	}

}
