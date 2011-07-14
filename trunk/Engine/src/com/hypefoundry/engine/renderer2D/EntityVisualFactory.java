package com.hypefoundry.engine.renderer2D;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.renderer2D.EntityVisual;

/**
 * Creates an instance of a VisualEntity.
 * 
 * @author paksas
 *
 */
public interface EntityVisualFactory 
{
	/**
	 * The factory method.
	 * 
	 * @param parentEntity
	 * @return
	 */
	EntityVisual instantiate( Entity parentEntity );
}
