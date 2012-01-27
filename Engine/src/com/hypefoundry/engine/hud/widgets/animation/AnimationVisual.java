/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.animation;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.AnimationPlayer;

/**
 * @author Paksas
 *
 */
public class AnimationVisual extends HudWidgetVisual 
{
	private AnimationWidget		m_widget;
	private AnimationPlayer		m_animationPlayer;
	
	
	AnimationVisual( HudWidget widget )
	{
		super( widget );
		
		m_widget = (AnimationWidget)widget;
		
		m_animationPlayer = new AnimationPlayer();
		int animIdx = m_animationPlayer.addAnimation( m_widget.m_animation );
		m_animationPlayer.select( animIdx );
	}

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		if ( m_animationPlayer != null )
		{
			TextureRegion region = m_animationPlayer.getTextureRegion( deltaTime );
			batcher.drawUnalignedSprite( m_globalPos.m_x, m_globalPos.m_y, m_width, m_height, region );
		}
	}

	@Override
	public boolean handleInput( Input input, HudRenderer renderer, float deltaTime ) 
	{
		// nothing to do here
		return false;
	}

}
