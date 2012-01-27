/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.image;

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
	public String					m_caption;
	

	@Override
	public void onLoad( ResourceManager resMgr, DataLoader loader ) 
	{
		String path = loader.getStringValue( "path" );
		if ( path.length() > 0 )
		{
			m_region = resMgr.getResource( TextureRegion.class, path );
		}
		
		m_caption = loader.getStringValue( "caption" );
	}

}
