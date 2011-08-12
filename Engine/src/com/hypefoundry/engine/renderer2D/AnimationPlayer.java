/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import java.util.*;


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
	
	/**
	 * Constructor.
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
		m_activeAnimationIdx = idx;
		if ( m_activeAnimationIdx >= m_animations.size() )
		{
			m_activeAnimationIdx = m_animations.size() - 1;
		}
		if ( m_activeAnimationIdx < 0 )
		{
			m_activeAnimationIdx = 0;
		}
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
		
		TextureRegion region = m_animations.get( m_activeAnimationIdx ).animate( m_animationTime );
		return region;
	}
}

