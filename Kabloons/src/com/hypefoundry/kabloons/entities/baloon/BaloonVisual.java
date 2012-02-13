/**
 * 
 */
package com.hypefoundry.kabloons.entities.baloon;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class BaloonVisual extends EntityVisual 
{
	private Baloon				m_baloon;
	
	private	AnimationPlayer		m_animPlayer;
	private int					m_floatUpAnim;
	private int					m_floatLeftAnim;
	private int					m_floatRightAnim;
	
	private DynamicObject		m_dynObj;
	
	
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param baloonEntity
	 */
	public BaloonVisual( ResourceManager resMgr, Entity baloonEntity ) 
	{
		super( baloonEntity );
		
		m_baloon = (Baloon)baloonEntity;
		
		m_animPlayer = new AnimationPlayer();
		Animation floatingUpAnim = resMgr.getResource( Animation.class, m_baloon.m_floatingUpAnim );
		Animation floatingLeftAnim = resMgr.getResource( Animation.class, m_baloon.m_floatingLeftAnim );
		Animation floatingRightAnim = resMgr.getResource( Animation.class, m_baloon.m_floatingRightAnim );
		m_floatUpAnim = m_animPlayer.addAnimation( floatingUpAnim );
		m_floatRightAnim = m_animPlayer.addAnimation( floatingRightAnim );
		m_floatLeftAnim =  m_animPlayer.addAnimation( floatingLeftAnim );
		
		m_dynObj = m_baloon.query( DynamicObject.class );	
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{
		if( m_baloon.m_state == Baloon.State.Flying )
		{
			Vector3 pos = m_entity.getPosition();
			BoundingBox bs = m_entity.getBoundingShape();
			
			// select an animation appropriate to the current velocity
			final Vector3 bodyVel = m_dynObj.getCurrentVelocity();
			if ( bodyVel.m_x < -0.01f )
			{
				m_animPlayer.select( m_floatLeftAnim );
			}
			else if ( bodyVel.m_x > 0.01f )
			{
				m_animPlayer.select( m_floatRightAnim );
			}
			else
			{
				m_animPlayer.select( m_floatUpAnim );
			}
			
			batcher.drawSprite( pos, bs, m_animPlayer.getTextureRegion( deltaTime ) );
		}
	}

}
