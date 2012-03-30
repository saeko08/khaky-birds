/**
 * 
 */
package com.hypefoundry.engine.impl.game;

import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.EntityVisual;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
class NullLoadingScreenFactory extends LoadingScreenFactory  
{
	private class NullEntityVisual extends LoadingScreenVisual
	{

		public NullEntityVisual( Entity entity ) 
		{
			super( entity );
		}

		@Override
		public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
		{
		}
		
		@Override
		public void startFadeIn() 
		{
		}

		@Override
		public boolean hasFadedOut() 
		{
			return true;
		}

		@Override
		public boolean hasFadedIn() 
		{
			return true;
		}		
	}
	
	
	@Override
	public EntityVisual instantiate( Entity parentEntity ) 
	{
		return new NullEntityVisual( parentEntity );
	}
}
