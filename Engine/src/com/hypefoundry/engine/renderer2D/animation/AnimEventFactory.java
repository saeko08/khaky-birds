/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import com.hypefoundry.engine.world.EntityEvent;

/**
 * Factory of animation events.
 * 
 * @author Paksas
 */
public interface AnimEventFactory 
{
	/**
	 * Creates an animation event.
	 * 
	 * @return
	 */
	EntityEvent create();
}
