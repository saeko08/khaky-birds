/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingShape;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.AnimationPlayer;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;

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
		Animation regularAnimation = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/pedestrianWalking.xml");
		Animation wipeShitOffAnimation = resMgr.getResource( Animation.class, "khaky_birds_prototype/animations/pedestrianWipeShitOff.xml");
		
		// create an animation player
		m_animationPlayer = new AnimationPlayer();
		ANIM_WALK = m_animationPlayer.addAnimation( regularAnimation );
		ANIM_WIPE_SHIT_OFF = m_animationPlayer.addAnimation( wipeShitOffAnimation );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		Vector3 pos = m_pedestrian.getPosition();
		BoundingShape bs = m_pedestrian.getBoundingShape();
	
		// select an animation appropriate to the state the pedestrian's in
		if ( m_pedestrian.m_hitWithShit == false )
		{
			m_animationPlayer.select( ANIM_WALK );
		}
		else
		{
			m_animationPlayer.select( ANIM_WIPE_SHIT_OFF );
		}
		
		// draw the pedestrian
		batcher.drawSprite( pos.m_x, pos.m_y, bs.getWidth(), bs.getHeight(), m_pedestrian.m_facing, m_animationPlayer.getTextureRegion( deltaTime ) );	
	}
}
