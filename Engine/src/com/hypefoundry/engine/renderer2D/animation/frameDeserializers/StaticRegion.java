/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation.frameDeserializers;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.FrameDeserializer;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Deserializes frames composed of a single texture region.
 * 
 * @author Paksas
 */
public class StaticRegion extends FrameDeserializer 
{

	@Override
	public void deserialize( DataLoader loader, Animation animation ) 
	{
		TextureRegion region = new TextureRegion( animation.m_renderState );
		region.deserializeCoordinates( loader );
		animation.appendFrame( region );
	}

	@Override
	public int getFramesCount( DataLoader loader, Animation animation ) 
	{
		return 1;
	}
}
