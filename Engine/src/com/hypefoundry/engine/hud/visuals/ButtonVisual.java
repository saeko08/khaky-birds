/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import java.util.List;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.widgets.ButtonState;
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
	
	private ButtonState					m_state					= ButtonState.RELEASED;
	
	private final float					m_pressTimerCooldown	= 0.5f;
	private float						m_pressTimer			= 0;
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
		m_template.drawButton( batcher, m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_state, caption );
	}
	
	@Override
	public void handleInput( Input input, HudRenderer renderer, float deltaTime )
	{
		m_state = ButtonState.RELEASED;

		if ( m_pressTimer > 0 )
		{
			m_pressTimer -= deltaTime;
		}
		
		// check if the cursor is over the button
		List< TouchEvent > events = input.getTouchEvents();
		int eventsCount = events.size();
		TouchEvent event;
		for ( int i = 0; i < eventsCount; ++i )
		{
			event = events.get( i );	
			renderer.getTouchPos( event.x, event.y, m_tmpTouchPos );
			
			if ( m_bb.doesOverlap( m_tmpTouchPos, null ) )
			{
				if( event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DOUBLE_TAP )
				{
					m_pressTimer = m_pressTimerCooldown;
					m_state = ButtonState.PRESSED;
					
					// notify listeners about it
					if ( m_msgSink != null )
					{
						m_msgSink.onButtonPressed( m_template.m_id );
					}
					
					break;
				}
			}
		}
		
		if ( m_pressTimer <= 0 && input.isTouchDown( 0 ) )
		{
			// check if the cursor is hovering over the button
			int touchX = input.getTouchX( 0 );
			int touchY = input.getTouchY( 0 );
			renderer.getTouchPos( touchX, touchY, m_tmpTouchPos );
			
			if ( m_bb.doesOverlap( m_tmpTouchPos, null ) )
			{
				m_state = ButtonState.HIGHLIGHTED;
			}
		}
	}

}
