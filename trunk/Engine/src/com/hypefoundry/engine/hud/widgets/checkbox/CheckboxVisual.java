/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.checkbox;

import java.util.List;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.hud.ClickableHudWidget;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * A visual instance responsible for drawing a checkbox widget
 * in its respective state.
 * 
 * @author Paksas
 */
public class CheckboxVisual extends HudWidgetVisual 
{
	private CheckboxWidget						m_widget;
	private CustomCheckboxVisualTemplate 		m_customCheckboxVisualTemplate;
	
	private ClickableHudWidget					m_msgSink;
	
	private Vector3								m_tmpTouchPos						= new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param widget
	 * @param customCheckboxVisualTemplate
	 */
	public CheckboxVisual( HudWidget widget, CustomCheckboxVisualTemplate customCheckboxVisualTemplate ) 
	{
		super( widget );
		
		m_widget = (CheckboxWidget)widget;
		m_customCheckboxVisualTemplate = customCheckboxVisualTemplate;
		
		if ( widget instanceof ClickableHudWidget )
		{
			m_msgSink = (ClickableHudWidget)widget;
		}
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		m_customCheckboxVisualTemplate.draw( batcher, m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_widget.m_state );
	}

	@Override
	public boolean handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// check if the cursor is over the button
		List< TouchEvent > events = input.getTouchEvents();
		int eventsCount = events.size();
		TouchEvent event;
		for ( int i = 0; i < eventsCount; ++i )
		{
			event = events.get( i );	
			renderer.getTouchPos( event.x, event.y, m_tmpTouchPos );
			
			if ( m_bb.doesOverlap( m_tmpTouchPos, null ) && ( event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DOUBLE_TAP ) )
			{
				m_widget.toggleState();
				
				// notify listeners about it
				if ( m_msgSink != null )
				{
					m_msgSink.onButtonPressed(0);
				}
				
				return true;
			}
		}
		
		return false;
	}

}
