/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.widgets.CounterWidget;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * Counter visual.
 * 
 * @author Paksas
 */
public class CounterVisual extends HudWidgetVisual
{
	private CounterWidget				m_widget;
	private CounterVisualTemplate 		m_template;
	
	CounterVisual( HudWidget widget, CounterVisualTemplate template )
	{
		super( widget );
		m_widget = (CounterWidget)widget;
		m_template = template;
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		m_template.draw( batcher, m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_widget.getValue() );
	}

	@Override
	public void handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// nothing to do here
	}
}
