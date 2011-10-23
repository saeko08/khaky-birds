/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.widgets.ImageWidget;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * @author Paksas
 *
 */
public class ImageVisual extends HudWidgetVisual 
{
	private ImageWidget 		m_widget;
	
	
	ImageVisual( HudWidget widget )
	{
		super( widget );
		m_widget = (ImageWidget)widget;
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		if ( m_widget.m_region != null )
		{
			batcher.drawUnalignedSprite( m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_widget.m_region );
		}
	}

	@Override
	public void handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// nothing to do here
	}

}
