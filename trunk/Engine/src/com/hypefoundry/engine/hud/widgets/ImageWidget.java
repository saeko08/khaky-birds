/**
 * 
 */
package com.hypefoundry.engine.hud.widgets;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class ImageWidget extends HudWidget 
{
	public TextureRegion			m_region;
	

	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		String path = loader.getStringValue( "path" );
		m_region = resMgr.getResource( TextureRegion.class, path );
	}

}
