/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public abstract class LoadingScreenVisual extends EntityVisual 
{

	/**
	 * Constructor.
	 * 
	 * @param entity
	 */
	public LoadingScreenVisual( Entity entity ) 
	{
		super(entity);
		((LoadingScreen)entity).m_visual = this;
	}

	/**
	 * Begins to fade the loading screen into the image rendered in the background
	 * by the other renderer.
	 */
	public abstract void startFadeIn();
	
	/**
	 * Tells if the loading screen is now fully visible. 
	 */
	public abstract boolean hasFadedOut();

	/**
	 * Tells if the background image is now fully visible.
	 */
	public abstract boolean hasFadedIn();
}

