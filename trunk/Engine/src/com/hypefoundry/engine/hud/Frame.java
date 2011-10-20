/**
 * 
 */
package com.hypefoundry.engine.hud;

import com.hypefoundry.engine.hud.HudComposite;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;

/**
 * A frame that can store other HUD elements.
 * 
 * @author Paksas
 *
 */
public class Frame extends HudComposite
{

	@Override
	public void draw( SpriteBatcher batcher, float deltaTime ) 
	{
		m_hud.drawFrame( batcher, m_position.m_x, m_position.m_y, m_width, m_height );
		
		// draw the children
		super.draw( batcher, deltaTime );
	}
}
