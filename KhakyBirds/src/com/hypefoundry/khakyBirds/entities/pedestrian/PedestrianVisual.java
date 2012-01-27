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
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		Pedestrian.Animation[] nodes = Pedestrian.Animation.values();
		for( int i = 0; i < nodes.length; ++i )
		{
			String path = m_pedestrian.m_animPaths[i];
			Animation anim = resMgr.getResource( Animation.class, path );
			m_animationPlayer.addAnimation( anim );
		}
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		BoundingBox bs = m_pedestrian.getBoundingShape();
	
		// select an animation appropriate to the state the pedestrian's in
		if ( m_pedestrian.m_hitWithShit == true  ||  m_pedestrian.m_state == Pedestrian.State.Shitted)
		{
			m_animationPlayer.select( Pedestrian.Animation.ANIM_WIPE_SHIT_OFF.m_id );
			
		}
		else if (m_pedestrian.m_state == Pedestrian.State.Observe ||  m_pedestrian.m_state == Pedestrian.State.Eaten)
		{
			m_animationPlayer.select( Pedestrian.Animation.ANIM_OBSERVE.m_id );
		}
		else
		{
			m_animationPlayer.select( Pedestrian.Animation.ANIM_WALK.m_id );
		}
	
		
		// draw the pedestrian
		batcher.drawSprite( pos, bs, m_pedestrian.getFacing(), m_animationPlayer.getTextureRegion( deltaTime ) );	
	}
}
