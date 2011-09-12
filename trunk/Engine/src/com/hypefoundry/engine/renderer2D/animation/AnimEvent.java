/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.EntityEvent;

/**
 * Animation event base class.
 * 
 * @author Paksas
 *
 */
public class AnimEvent implements EntityEvent 
{
	public EntityEvent		m_event;
	
	/**
	 * Sets the event the animation event should convey.
	 * 
	 * @param event
	 */
	public void set( EntityEvent event )
	{
		m_event = event;
	}
	
	@Override
	public void deserialize( DataLoader loader ) {}
}
