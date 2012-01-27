/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.image;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * @author Paksas
 *
 */
public class ImageVisual extends HudWidgetVisual 
{
	private ImageWidget 			m_widget;
	private ImageVisualTemplate 	m_template;
	
	
	ImageVisual( HudWidget widget, ImageVisualTemplate template )
	{
		super( widget );
		m_widget = (ImageWidget)widget;
		m_template = template;
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		if ( m_widget.m_region != null )
		{
			batcher.drawUnalignedSprite( m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_widget.m_region );
		}
		
		// draw the caption
		if ( m_widget.m_caption.length() > 0 )
		{
			float borderWidth = m_template.m_borderSize / (float)batcher.m_graphics.getWidth();
			float borderHeight = m_template.m_borderSize / (float)batcher.m_graphics.getHeight();
				
			m_template.m_caption.setText( m_widget.m_caption );
			m_template.m_caption.drawCentered( batcher, m_globalPos.m_x + borderWidth, m_globalPos.m_y + borderHeight, m_width - borderWidth * 2.0f, m_height - borderHeight * 2.0f );
		}
	}

	@Override
	public boolean handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// nothing to do here
		return false;
	}

}
