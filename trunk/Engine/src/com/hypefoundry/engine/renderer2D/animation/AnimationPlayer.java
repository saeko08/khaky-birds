/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import java.util.*;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.world.Entity;


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
		float prevAnimTime = m_animationTime;
		m_animationTime += deltaTime;
		Animation animation = m_animations.get( m_activeAnimationIdx );
		int frameIdx = animation.getFrameIdx( m_animationTime );
		
		// transmit the events
		boolean loopSkipped = ( frameIdx < m_lastEventFrameIdx ) || ( m_animationTime - prevAnimTime >= animation.getDuration() );
		if ( loopSkipped )
		{
			for ( int i = m_lastEventFrameIdx + 1; i < animation.m_regions.length; ++i )
			{
				animation.transmitAnimEvents( entity, i );
			}
			
			int endIdx = ( m_lastEventFrameIdx < frameIdx ) ? m_lastEventFrameIdx : frameIdx;
			for ( int i = 0; i <= endIdx; ++i )
			{
				animation.transmitAnimEvents( entity, i );
			}
		}
		else
		{
			for ( int i = m_lastEventFrameIdx + 1; i <= frameIdx; ++i )
			{
				animation.transmitAnimEvents( entity, i );
			}
		}
		m_lastEventFrameIdx = frameIdx;

		// select the texture region
		TextureRegion region = animation.m_regions[frameIdx];
		return region;
	}


	/**
	 * Sets new value of the animation timeline.
	 * 
	 * @param time
	 */
	public void setTime( float time ) 
	{
		m_animationTime = time;
	}
}

