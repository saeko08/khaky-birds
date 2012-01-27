/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.animation;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class AnimationWidget extends HudWidget 
{
	public Animation			m_animation;
	
	
	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		String path = loader.getStringValue( "path" );
		m_animation = resMgr.getResource( Animation.class, path );
	}
}
