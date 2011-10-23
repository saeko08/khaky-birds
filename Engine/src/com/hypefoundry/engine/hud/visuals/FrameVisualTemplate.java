/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.hud.HudWidgetVisualTemplate;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;


/**
 * Base frame visual template interface.
 * @author Paksas
 *
 */
public interface FrameVisualTemplate extends HudWidgetVisualTemplate 
{
	/**
	 * Draws the frame visual.
	 * 
	 * @param batcher
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param deltaTime
	 */
	void draw( SpriteBatcher batcher, float x, float y, float width, float height, float deltaTime );
}
