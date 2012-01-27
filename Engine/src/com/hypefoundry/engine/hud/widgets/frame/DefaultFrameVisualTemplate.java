/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.frame;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * @author Paksas
 *
 */
public class DefaultFrameVisualTemplate implements FrameVisualTemplate
{
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
	
	
	@Override
	public void draw( SpriteBatcher batcher, float x, float y, float width, float height, float deltaTime ) 
	{
		float borderWidth = (float)m_frameElementSize / (float)batcher.m_graphics.getWidth();
		float borderHeight = (float)m_frameElementSize / (float)batcher.m_graphics.getHeight();
		
		float xs = x + borderWidth;
		float xe = x + width - borderWidth;
		float ys = y + borderHeight;
		float ye = y + height - borderHeight;
		
		// draw the background
		batcher.drawUnalignedSprite( xs, ys, xe - xs, ye - ys, FRAME_CENTER );
		
		// draw the corners
		batcher.drawUnalignedSprite( xs - borderWidth, 	ys - borderHeight, 	borderWidth, borderHeight, FRAME_TOP_LEFT_CORNER );
		batcher.drawUnalignedSprite( xe, 				ys - borderHeight, 	borderWidth, borderHeight, FRAME_TOP_RIGHT_CORNER );
		batcher.drawUnalignedSprite( xs - borderWidth, 	ye, 				borderWidth, borderHeight, FRAME_BOTTOM_LEFT_CORNER );
		batcher.drawUnalignedSprite( xe, 				ye, 				borderWidth, borderHeight, FRAME_BOTTOM_RIGHT_CORNER );
		
		// draw the top & bottom corners
		batcher.drawUnalignedSprite( xs, y, xe - xs, borderHeight, FRAME_TOP_EDGE );
		batcher.drawUnalignedSprite( xs, ye, xe - xs, borderHeight, FRAME_BOTTOM_EDGE );

		// draw the left & right corners
		batcher.drawUnalignedSprite( x, ys, borderWidth, ye - ys, FRAME_LEFT_EDGE );
		batcher.drawUnalignedSprite( xe, ys, borderWidth, ye - ys, FRAME_RIGHT_EDGE );
	}

	@Override
	public void load( ResourceManager resMgr, DataLoader loader )
	{
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, loader );
		
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
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget )
	{
		return new FrameVisual( widget, this );
	}
}
