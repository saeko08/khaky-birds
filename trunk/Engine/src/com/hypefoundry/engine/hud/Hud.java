/**
 * 
 */
package com.hypefoundry.engine.hud;

import java.io.IOException;
import java.io.InputStream;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;

/**
 * Hud display.
 * 
 * @author Paksas
 *
 */
public class Hud extends Resource
{
	// --------------------------------------------------------------------
	// Frame elements
	// --------------------------------------------------------------------
	private TextureRegion			FRAME_TOP_LEFT_CORNER;
	private TextureRegion			FRAME_TOP_RIGHT_CORNER;
	private TextureRegion			FRAME_BOTTOM_LEFT_CORNER;
	private TextureRegion			FRAME_BOTTOM_RIGHT_CORNER;
	private TextureRegion			FRAME_TOP_EDGE;
	private TextureRegion			FRAME_BOTTOM_EDGE;
	private TextureRegion			FRAME_LEFT_EDGE;
	private TextureRegion			FRAME_RIGHT_EDGE;
	private TextureRegion			FRAME_CENTER;

	private float					m_frameElementSize;
	
	// ------------------------------------------------------------------------
	// Drawing different shapes
	// ------------------------------------------------------------------------
	/**
	 * Draws the frame itself.
	 * 
	 * @param batcher
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	void drawFrame( SpriteBatcher batcher, float x, float y, float width, float height )
	{
		float xs = x + m_frameElementSize;
		float xe = x + width - m_frameElementSize;
		float ys = y + m_frameElementSize;
		float ye = y + height - m_frameElementSize;
		int horizElementsCount = (int)( ( width - 2 * m_frameElementSize ) / m_frameElementSize );
		int vertElementsCount = (int)( ( height - 2 * m_frameElementSize ) / m_frameElementSize );
		
		// draw the background
		batcher.drawSprite( xs, ys, xe - xs, ye - ys, FRAME_CENTER );
		
		// draw the corners
		batcher.drawSprite( xs - m_frameElementSize, 	ys - m_frameElementSize, 	m_frameElementSize, m_frameElementSize, FRAME_TOP_LEFT_CORNER );
		batcher.drawSprite( xe, 						ys - m_frameElementSize, 	m_frameElementSize, m_frameElementSize, FRAME_TOP_RIGHT_CORNER );
		batcher.drawSprite( xs - m_frameElementSize, 	ye, 						m_frameElementSize, m_frameElementSize, FRAME_BOTTOM_LEFT_CORNER );
		batcher.drawSprite( xe, 						ye, 						m_frameElementSize, m_frameElementSize, FRAME_BOTTOM_RIGHT_CORNER );
		
		// draw the top & bottom corners
		float edgeX = xs;
		for( int i = 0; i < horizElementsCount; ++i, edgeX += m_frameElementSize )
		{
			batcher.drawSprite( edgeX, ys, m_frameElementSize, m_frameElementSize, FRAME_TOP_EDGE );
			batcher.drawSprite( edgeX, ye, m_frameElementSize, m_frameElementSize, FRAME_BOTTOM_EDGE );
		}
		
		// draw the left & right corners
		float edgeY = ys;
		for( int i = 0; i < vertElementsCount; ++i, edgeY += m_frameElementSize )
		{
			batcher.drawSprite( xs, edgeY, m_frameElementSize, m_frameElementSize, FRAME_LEFT_EDGE );
			batcher.drawSprite( xe, edgeY, m_frameElementSize, m_frameElementSize, FRAME_RIGHT_EDGE );
		}
	}
	
	// ------------------------------------------------------------------------
	// Resource implementation
	// ------------------------------------------------------------------------
	@Override
	public void load() 
	{
		InputStream stream = null;
		try 
		{
			stream = m_game.getFileIO().readAsset( m_assetPath );
		} 
		catch ( IOException e ) 
		{
			throw new RuntimeException( e );
		}
		
		// parse the animation data
		DataLoader hudNode = XMLDataLoader.parse( stream, "HUD" );
		if ( hudNode != null )
		{
			DataLoader frameNode = hudNode.getChild( "Frame" );
			if ( frameNode != null )
			{
				loadFrameDefinition( frameNode );
			}
			
			
		}
	}

	/**
	 * Loads a frame definition.
	 * @param loader
	 */
	private void loadFrameDefinition( DataLoader loader )
	{
		RenderState rs = new RenderState();
		rs.deserialize( m_resMgr, loader );
		
		m_frameElementSize = loader.getFloatValue( "elemSize" );
		
		DataLoader elemNode;
		
		if ( ( elemNode = loader.getChild( "TopLeftCorner" ) ) != null )
		{
			FRAME_TOP_LEFT_CORNER = new TextureRegion( rs );
			FRAME_TOP_LEFT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "TopRightCorner" ) ) != null )
		{
			FRAME_TOP_RIGHT_CORNER = new TextureRegion( rs );
			FRAME_TOP_RIGHT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomLeftCorner" ) ) != null )
		{
			FRAME_BOTTOM_LEFT_CORNER = new TextureRegion( rs );
			FRAME_BOTTOM_LEFT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomRightCorner" ) ) != null )
		{
			FRAME_BOTTOM_RIGHT_CORNER = new TextureRegion( rs );
			FRAME_BOTTOM_RIGHT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "TopEdge" ) ) != null )
		{
			FRAME_TOP_EDGE = new TextureRegion( rs );
			FRAME_TOP_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomEdge" ) ) != null )
		{
			FRAME_BOTTOM_EDGE = new TextureRegion( rs );
			FRAME_BOTTOM_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "LeftEdge" ) ) != null )
		{
			FRAME_LEFT_EDGE = new TextureRegion( rs );
			FRAME_LEFT_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "RightEdge" ) ) != null )
		{
			FRAME_RIGHT_EDGE = new TextureRegion( rs );
			FRAME_RIGHT_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "Center" ) ) != null )
		{
			FRAME_CENTER = new TextureRegion( rs );
			FRAME_CENTER.deserializeCoordinates( elemNode );
		}		
	}

	@Override
	public void release() 
	{
	}
}
