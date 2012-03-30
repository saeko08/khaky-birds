/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.renderer2D.EntityVisualFactory;

/**
 * @author Paksas
 *
 */
public abstract class LoadingScreenFactory implements EntityVisualFactory 
{

	protected ResourceManager		m_resMgr;
	
	/**
	 * Initializes the resource management system the created visuals should use.
	 * 
	 * @param resMgr
	 */
	public void initialize( ResourceManager resMgr )
	{
		m_resMgr = resMgr;
	}
}
