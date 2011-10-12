/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.pedestrian;

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
 * Visual representation of a pedestrian entity.
 * 
 * @author paksas
 *
 */
public class PedestrianVisual extends EntityVisual 
{
	private Pedestrian			m_pedestrian;
	private AnimationPlayer		m_animationPlayer;

	private int 				ANIM_WALK;
	private int 				ANIM_WIPE_SHIT_OFF;
	private int 				ANIM_OBSERVE;
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param entity
	 */
	public PedestrianVisual( ResourceManager resMgr, Entity entity ) 
	{
		super( entity );
		
		m_pedestrian = (Pedestrian)entity;
		
		// load animations
		Animation regularAnimation = resMgr.getResource( Animation.class, "animations/pedestrian/walking.xml");
		Animation wipeShitOffAnimation = resMgr.getResource( Animation.class, "animations/pedestrian/wipeShitOff.xml");
		Animation observing = resMgr.getResource( Animation.class, "animations/pedestrian/observing.xml");
		
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_WALK = m_animationPlayer.addAnimation( regularAnimation );
		ANIM_WIPE_SHIT_OFF = m_animationPlayer.addAnimation( wipeShitOffAnimation );
		ANIM_OBSERVE = m_animationPlayer.addAnimation( observing );
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		BoundingBox bs = m_pedestrian.getBoundingShape();
	
		// select an animation appropriate to the state the pedestrian's in
		if ( m_pedestrian.m_hitWithShit == true  ||  m_pedestrian.m_state == Pedestrian.State.Shitted)
		{
			m_animationPlayer.select( ANIM_WIPE_SHIT_OFF );
			
		}
		else if (m_pedestrian.m_state == Pedestrian.State.Observe ||  m_pedestrian.m_state == Pedestrian.State.Eaten)
		{
			m_animationPlayer.select( ANIM_OBSERVE );
		}
		else
		{
			m_animationPlayer.select( ANIM_WALK );
		}
		
		// draw the pedestrian
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pedestrian.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );	
	}
}
