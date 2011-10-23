/**
 * 
 */
package com.hypefoundry.engine.hud;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public interface HudWidgetVisualTemplate 
{
	/**
	 * Loads the definition of a visual.
	 * 
	 * @param resMgr
	 * @param loader
	 */
	void load( ResourceManager resMgr, DataLoader loader );
	
	
	/**
	 * Instantiates a widget visual.
	 * 
	 * @param renderer
	 * @param widget
	 * @return
	 */
	HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget );
}
