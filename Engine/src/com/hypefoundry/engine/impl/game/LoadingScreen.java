/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class LoadingScreen extends Entity 
{
	LoadingScreenVisual		m_visual;
	
	/**
	 * Default constructor.
	 */
	LoadingScreen()
	{
		// TODO: viewport size config
		setPosition( 2.4f, 4.0f, 0.0f );
		setBoundingBox( new BoundingBox( -2.4f, -4.0f, 2.4f, 4.0f ) );
	}
		
	/**
	 * Begins to fade the loading screen into the image rendered in the background
	 * by the other renderer.
	 */
	public void startFadeIn()
	{
		if ( m_visual != null )
		{
			m_visual.startFadeIn();
		}
	}
	
	/**
	 * Tells if the loading screen is now fully visible. 
	 */
	public boolean hasFadedOut()
	{
		if ( m_visual != null )
		{
			return m_visual.hasFadedOut();
		}
		else
		{
			return false;
		}
	}

	/**
	 * Tells if the background image is now fully visible.
	 */
	public boolean hasFadedIn()
	{
		if ( m_visual != null )
		{
			return m_visual.hasFadedIn();
		}
		else
		{
			return false;
		}
	}

}
