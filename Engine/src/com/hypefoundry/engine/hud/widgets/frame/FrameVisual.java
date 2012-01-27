/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.frame;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * Frame visual.
 * @author Paksas
 *
 */
public class FrameVisual extends HudWidgetVisual 
{
	private FrameVisualTemplate 			m_template;
	
	
	/**
	 * Constructor.
	 * 
	 * @param widget
	 * @param template
	 */
	FrameVisual( HudWidget widget, FrameVisualTemplate template )
	{
		super( widget );
		m_template = template;
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		m_template.draw( batcher, m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, deltaTime );
	}

	@Override
	public boolean handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// nothing to do here
		return false;
	}
}
