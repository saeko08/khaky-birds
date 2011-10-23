/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Image visual template.
 * 
 * @author Paksas
 */
public class ImageVisualTemplate implements HudWidgetVisualTemplate 
{
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new ImageVisual( widget );
	}
}
