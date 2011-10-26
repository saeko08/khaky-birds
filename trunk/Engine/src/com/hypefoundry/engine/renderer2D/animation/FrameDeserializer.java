/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation;

import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Deserializes a frame node.
 * 
 * @author Paksas
 */
public abstract class FrameDeserializer 
{
	/**
	 * Returns the number of frames this deserializer will create. 
	 * 
	 * @param loader
	 * @param animation
	 */
	abstract public int getFramesCount( DataLoader loader, Animation animation );
	
	/**
	 * Frame-specific deserialization routine.
	 * 
	 * @param loader
	 * @param animation
	 */
	abstract public void deserialize( DataLoader loader, Animation animation );
}
