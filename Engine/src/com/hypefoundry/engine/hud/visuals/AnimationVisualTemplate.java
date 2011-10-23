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
 * Animation visual template.
 * 
 * @author Paksas
 */
public class AnimationVisualTemplate implements HudWidgetVisualTemplate 
{
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new AnimationVisual( widget );
	}

}
