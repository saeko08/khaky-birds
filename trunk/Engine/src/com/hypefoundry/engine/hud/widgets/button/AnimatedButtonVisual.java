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
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;

/**
 * @author Paksas
 *
 */
public class AnimatedButtonVisual extends HudWidgetVisual 
{
	private ClickableHudWidget				m_msgSink;
	private CaptionedHudWidget				m_captionProvider;
	
	private AnimatedButtonVisualTemplate	m_template;
	
	private AnimationPlayer					m_player;
	private Vector3							m_tmpTouchPos			= new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param widget
	 * @param template
	 */
	AnimatedButtonVisual( HudWidget widget, AnimatedButtonVisualTemplate template )
	{
		super( widget );
		
		if ( widget instanceof ClickableHudWidget )
		{
			m_msgSink = (ClickableHudWidget)widget;
		}
		
		if ( widget instanceof CaptionedHudWidget )
		{
			m_captionProvider = (CaptionedHudWidget)widget;
		}
		
		m_template = template;
		
		m_player = new AnimationPlayer();
		m_player.addAnimation( m_template.m_animation );
	}
	
	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		batcher.drawUnalignedSprite( m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, m_player.getTextureRegion( deltaTime ) );
		
		// draw the caption
		if ( m_captionProvider != null )
		{
			String caption = m_captionProvider.getCaption();
			
			float borderWidth = m_template.m_borderSize / (float)batcher.m_graphics.getWidth();
			float borderHeight = m_template.m_borderSize / (float)batcher.m_graphics.getHeight();
			
			// draw the caption
			m_template.m_caption.setText( caption );
			m_template.m_caption.drawCentered( batcher, m_globalPos.m_x + borderWidth, m_globalPos.m_y + borderHeight, m_width - borderWidth * 2.0f, m_height - borderHeight * 2.0f );
		}
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
			
			if ( m_bb.doesOverlap( m_tmpTouchPos, null ) )
			{
				if( event.type == Input.TouchEvent.TOUCH_DOWN || event.type == Input.TouchEvent.TOUCH_DOUBLE_TAP )
				{					
					// notify listeners about it
					if ( m_msgSink != null )
					{
						m_msgSink.onButtonPressed(0);
					}
					
					return true;
				}
			}
		}
		
		return false;
	}

}
