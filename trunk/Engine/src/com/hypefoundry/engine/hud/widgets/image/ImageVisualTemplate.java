/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.image;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.renderer2D.font.Font;
import com.hypefoundry.engine.renderer2D.font.Text;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Image visual template.
 * 
 * @author Paksas
 */
public class ImageVisualTemplate implements HudWidgetVisualTemplate 
{
	float			m_borderSize;
	Text			m_caption	= new Text();
	
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		m_borderSize = loader.getFloatValue( "border", 0.0f );
		
		// load the font
		String fontPath = loader.getStringValue( "fontPath" );
		if ( fontPath.length() > 0 )
		{
			Font font = resMgr.getResource( Font.class, fontPath );
			m_caption.setFont( font );
		}
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new ImageVisual( widget, this );
	}
}
