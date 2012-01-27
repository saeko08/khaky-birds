/**
 * 
 */
package com.hypefoundry.engine.hud.widgets.button;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.font.Font;
import com.hypefoundry.engine.renderer2D.font.Text;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * Default button visual template.
 * 
 * @author Paksas
 */
public class DefaultButtonVisualTemplate extends ButtonVisualTemplate 
{	
	private TextureRegion			BUTTON_TOP_LEFT_CORNER;
	private TextureRegion			BUTTON_TOP_RIGHT_CORNER;
	private TextureRegion			BUTTON_BOTTOM_LEFT_CORNER;
	private TextureRegion			BUTTON_BOTTOM_RIGHT_CORNER;
	private TextureRegion			BUTTON_TOP_EDGE;
	private TextureRegion			BUTTON_BOTTOM_EDGE;
	private TextureRegion			BUTTON_LEFT_EDGE;
	private TextureRegion			BUTTON_RIGHT_EDGE;
	private TextureRegion			BUTTON_CENTER;
	
	public float					m_buttonElementSize;
	public Text						m_caption						= new Text();
	
	
	/**
	 * Default constructor.
	 */
	public DefaultButtonVisualTemplate() 
	{
		super( 0 );
	}
	
	/**
	 * Constructor ( if you want to create complex widgets with many embedded buttons that need to be identified when clicked ).
	 * 
	 * @param id
	 */
	DefaultButtonVisualTemplate( int id )
	{
		super( id );
	}
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, loader );
		
		m_buttonElementSize = loader.getFloatValue( "elemSize", 0.0f );
				
		DataLoader elemNode;
		if ( ( elemNode = loader.getChild( "TopLeftCorner" ) ) != null )
		{
			BUTTON_TOP_LEFT_CORNER = new TextureRegion( rs );
			BUTTON_TOP_LEFT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "TopRightCorner" ) ) != null )
		{
			BUTTON_TOP_RIGHT_CORNER = new TextureRegion( rs );
			BUTTON_TOP_RIGHT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomLeftCorner" ) ) != null )
		{
			BUTTON_BOTTOM_LEFT_CORNER = new TextureRegion( rs );
			BUTTON_BOTTOM_LEFT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomRightCorner" ) ) != null )
		{
			BUTTON_BOTTOM_RIGHT_CORNER = new TextureRegion( rs );
			BUTTON_BOTTOM_RIGHT_CORNER.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "TopEdge" ) ) != null )
		{
			BUTTON_TOP_EDGE = new TextureRegion( rs );
			BUTTON_TOP_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "BottomEdge" ) ) != null )
		{
			BUTTON_BOTTOM_EDGE = new TextureRegion( rs );
			BUTTON_BOTTOM_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "LeftEdge" ) ) != null )
		{
			BUTTON_LEFT_EDGE = new TextureRegion( rs );
			BUTTON_LEFT_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "RightEdge" ) ) != null )
		{
			BUTTON_RIGHT_EDGE = new TextureRegion( rs );
			BUTTON_RIGHT_EDGE.deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "Center" ) ) != null )
		{
			BUTTON_CENTER = new TextureRegion( rs );
			BUTTON_CENTER.deserializeCoordinates( elemNode );
		}		
		
		// load the font
		String fontPath = loader.getStringValue( "fontPath" );
		if ( fontPath.length() > 0 )
		{
			Font font = resMgr.getResource( Font.class, fontPath );
			m_caption.setFont( font );
		}
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new ButtonVisual( widget, this );
	}

	@Override
	public void drawButton( SpriteBatcher batcher, float x, float y, float width, float height, float deltaTime, String caption ) 
	{
		float borderWidth = (float)m_buttonElementSize / (float)batcher.m_graphics.getWidth();
		float borderHeight = (float)m_buttonElementSize / (float)batcher.m_graphics.getHeight();
		
		float xs = x + borderWidth;
		float xe = x + width - borderWidth;
		float ys = y + borderHeight;
		float ye = y + height - borderHeight;
		
		// draw the background
		batcher.drawUnalignedSprite( xs, ys, xe - xs, ye - ys, BUTTON_CENTER );
		
		// draw the corners
		batcher.drawUnalignedSprite( xs - borderWidth, 	ys - borderHeight, 	borderWidth, borderHeight, BUTTON_TOP_LEFT_CORNER );
		batcher.drawUnalignedSprite( xe, 				ys - borderHeight, 	borderWidth, borderHeight, BUTTON_TOP_RIGHT_CORNER );
		batcher.drawUnalignedSprite( xs - borderWidth, 	ye, 				borderWidth, borderHeight, BUTTON_BOTTOM_LEFT_CORNER );
		batcher.drawUnalignedSprite( xe, 				ye, 				borderWidth, borderHeight, BUTTON_BOTTOM_RIGHT_CORNER );
		
		// draw the top & bottom corners
		batcher.drawUnalignedSprite( xs, y, xe - xs, borderHeight, BUTTON_TOP_EDGE );
		batcher.drawUnalignedSprite( xs, ye, xe - xs, borderHeight, BUTTON_BOTTOM_EDGE );

		// draw the left & right corners
		batcher.drawUnalignedSprite( x, ys, borderWidth, ye - ys, BUTTON_LEFT_EDGE );
		batcher.drawUnalignedSprite( xe, ys, borderWidth, ye - ys, BUTTON_RIGHT_EDGE );
		
		// draw the caption
		m_caption.setText( caption );
		m_caption.drawCentered( batcher, x + borderWidth, y + borderHeight, width - borderWidth * 2.0f, height - borderHeight * 2.0f );
	}

}
