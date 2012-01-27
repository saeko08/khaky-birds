/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.button;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.font.Font;
import com.hypefoundry.engine.renderer2D.font.Text;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A visual template that allows you to create buttons with animations and textures ( no text though ).
 * 
 * @author Paksas
 */
public class AnimatedButtonVisualTemplate implements HudWidgetVisualTemplate 
{
	Animation					m_animation;
	float						m_borderSize;
	Text						m_caption	= new Text();
	
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		String animPath = loader.getStringValue( "path" );
		m_animation = resMgr.getResource( Animation.class, animPath );
		
		m_borderSize = loader.getFloatValue( "borderSize", 0.0f );
		
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
		return new AnimatedButtonVisual( widget, this );
	}

}

