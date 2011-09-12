/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import java.util.*;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.util.Arrays;


/**
 * Animation player.
 * 
 * @author Paksas
 *
 */
public class AnimationPlayer 
{
	private int						m_activeAnimationIdx;
	private List< Animation >		m_animations;
	private float					m_animationTime;
	private int						m_lastEventFrameIdx = -1;
	
	/**
	 * Constructor.
	 * 
	 * @param hostEntity		entity to which animation events should be sent
	 */
	public AnimationPlayer()
	{
		m_activeAnimationIdx = 0;
		m_animations = new ArrayList< Animation >();
		m_animationTime = 0;
	}
	
	
	/**
	 * Adds a new animation definition to the player.
	 * 
	 * @param animation
	 * @return		animation index
	 */
	public int addAnimation( Animation animation )
	{
		int idx = m_animations.size();
		
		// add it even if it's a duplicate
		m_animations.add( animation );
		return idx;
	}
	
	/**
	 * Selects an active animation.
	 * 
	 * @param idx	animation index
	 */
	public void select( int idx )
	{
		if ( m_activeAnimationIdx == idx )
		{
			// the animation doesn't really change
			return;
		}
		
		m_activeAnimationIdx = idx;
		if ( m_activeAnimationIdx >= m_animations.size() )
		{
			m_activeAnimationIdx = m_animations.size() - 1;
		}
		if ( m_activeAnimationIdx < 0 )
		{
			m_activeAnimationIdx = 0;
		}
		
		// reset the memorized frame index in which the last event was emitted
		m_lastEventFrameIdx = -1;
	}
	
	/**
	 * Returns a texture region you should draw your geometry with in this animation frame. 
	 * 
	 * @param deltaTime
	 * @return
	 */
	public TextureRegion getTextureRegion( float deltaTime )
	{
		m_animationTime += deltaTime;
		Animation animation = m_animations.get( m_activeAnimationIdx );
		int animIdx = animation.getFrameIdx( m_animationTime );
		
		TextureRegion region = animation.m_regions[animIdx];
		return region;
	}
	
	/**
	 * Alternative version of the method.
	 * It returns a texture region you should draw your geometry with in this animation frame,
	 * but also transmits the animation events the animation contains.
	 *  
	 * 
	 * @param entity
	 * @param deltaTime
	 * @return
	 */
	public TextureRegion getTextureRegion( Entity entity, float deltaTime )
	{
		m_animationTime += deltaTime;
		Animation animation = m_animations.get( m_activeAnimationIdx );
		int frameIdx = animation.getFrameIdx( m_animationTime );
		
		// transmit the events
		if ( frameIdx != m_lastEventFrameIdx )
		{
			if ( animation.transmitAnimEvents( entity, frameIdx ) )
			{
				m_lastEventFrameIdx = frameIdx;
			}
		}
		
		TextureRegion region = animation.m_regions[frameIdx];
		return region;
	}
}

