/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.button;

import java.util.List;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.hud.CaptionedHudWidget;
import com.hypefoundry.engine.hud.ClickableHudWidget;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;


/**
 * @author Paksas
 *
 */
public class ButtonVisual extends HudWidgetVisual
{
	private CaptionedHudWidget			m_captionProvider;
	private ClickableHudWidget			m_msgSink;
	private ButtonVisualTemplate		m_template;
	
	private Vector3						m_tmpTouchPos			= new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param widget
	 * @param template
	 */
	ButtonVisual( HudWidget widget, ButtonVisualTemplate template )
	{
		super( widget );
		m_template = template;
		
		if ( widget instanceof ClickableHudWidget )
		{
			m_msgSink = (ClickableHudWidget)widget;
		}
		
		if ( widget instanceof CaptionedHudWidget )
		{
			m_captionProvider = (CaptionedHudWidget)widget;
		}
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		String caption = "";
		if ( m_captionProvider != null )
		{
			caption = m_captionProvider.getCaption();
		}
		m_template.drawButton( batcher, m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, deltaTime, caption );
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
				// notify listeners about it
				if ( m_msgSink != null )
				{
					m_msgSink.onButtonPressed( m_template.m_id );
				}
					
				return true;
			}
		}
		
		return false;
	}
}
