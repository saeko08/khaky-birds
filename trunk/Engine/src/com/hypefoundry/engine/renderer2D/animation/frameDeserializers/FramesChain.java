/**
 * 
 */
package com.hypefoundry.engine.renderer2D.animation.frameDeserializers;

import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.animation.Animation;
import com.hypefoundry.engine.renderer2D.animation.FrameDeserializer;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * @author Paksas
 *
 */
public class FramesChain extends FrameDeserializer 
{


	@Override
	public int getFramesCount( DataLoader loader, Animation animation ) 
	{
		int numFrames = loader.getIntValue( "numFrames" );
		return numFrames;
	}


	@Override
	public void deserialize( DataLoader loader, Animation animation ) 
	{
		int numFrames = getFramesCount( loader, animation );
		
		if ( numFrames <= 0 )
		{
			return;
		}
		
		
		int w = loader.getIntValue( "w" );
		int h = loader.getIntValue( "h" );
		int skip = loader.getIntValue( "skip" );
		int vSpacing = loader.getIntValue( "vSpacing" );
		int hSpacing = loader.getIntValue( "hSpacing" );
		int numPerRow = loader.getIntValue( "numPerRow" );
		
		int startX = loader.getIntValue( "x" );
		int startY = loader.getIntValue( "y" );
		
		int x = startX + skip * ( w + hSpacing );
		int y = startY;
		int leftToLoad = numFrames;
		int numRows = ( numFrames / numPerRow ) + 1; 
		int numCols = numPerRow - skip;
		
		for ( int rowIdx = 0; rowIdx < numRows; ++rowIdx )
		{				
			for ( int colIdx = 0; colIdx < numCols; ++colIdx )
			{
				TextureRegion region = new TextureRegion( animation.m_renderState, x, y, w, h );
				animation.appendFrame( region );
				
				x += w + hSpacing;
				leftToLoad--;
				
				if ( leftToLoad <= 0 )
				{
					return;
				}
			}
			
			numCols = numPerRow;
			x = startX;
			y += h + vSpacing;
		}
	}

}
