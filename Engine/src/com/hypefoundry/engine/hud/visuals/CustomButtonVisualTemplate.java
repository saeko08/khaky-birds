/**
 * 
 */
package com.hypefoundry.engine.hud.visuals;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.hud.HudRenderer;
import com.hypefoundry.engine.hud.HudWidget;
import com.hypefoundry.engine.hud.HudWidgetVisual;
import com.hypefoundry.engine.hud.widgets.ButtonState;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.font.Font;
import com.hypefoundry.engine.renderer2D.font.Text;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A frame visual template that allows you to substitute a custom bitmap for a default button border.
 * 
 * @author Paksas
 */
public class CustomButtonVisualTemplate extends ButtonVisualTemplate 
{
	private TextureRegion[]		m_regions	= new TextureRegion[3];
	private float				m_borderSize;
	public Text					m_caption	= new Text();
	
	
	/**
	 * Default constructor.
	 */
	public CustomButtonVisualTemplate() 
	{
		super( 0 );
	}
	
	/**
	 * Constructor ( if you want to create complex widgets with many embedded buttons that need to be identified when clicked ).
	 * 
	 * @param id
	 */
	CustomButtonVisualTemplate( int id )
	{
		super( id );
	}
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, loader );
		
		DataLoader elemNode;
	
		if ( ( elemNode = loader.getChild( "Released" ) ) != null )
		{
			m_regions[ ButtonState.RELEASED.m_value ] = new TextureRegion( rs );
			m_regions[ ButtonState.RELEASED.m_value ].deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "Highlighed" ) ) != null )
		{
			m_regions[ ButtonState.HIGHLIGHTED.m_value ] = new TextureRegion( rs );
			m_regions[ ButtonState.HIGHLIGHTED.m_value ].deserializeCoordinates( elemNode );
		}
		
		if ( ( elemNode = loader.getChild( "Pressed" ) ) != null )
		{
			m_regions[ ButtonState.PRESSED.m_value ] = new TextureRegion( rs );
			m_regions[ ButtonState.PRESSED.m_value ].deserializeCoordinates( elemNode );
		}
			
		m_borderSize = loader.getFloatValue( "borderSize" );
		
		// load the font
		String fontPath = loader.getStringValue( "fontPath" );
		Font font = resMgr.getResource( Font.class, fontPath );
		m_caption.setFont( font );
	}

	@Override
	public HudWidgetVisual instantiate( HudRenderer renderer, HudWidget widget ) 
	{
		return new ButtonVisual( widget, this );
	}

	@Override
	public void drawButton( SpriteBatcher batcher, float x, float y, float width, float height, ButtonState state, String caption ) 
	{
		if ( m_regions[ state.m_value ] != null )
		{
			batcher.drawUnalignedSprite( x, y, width, height, m_regions[ state.m_value ] );
			
			float borderWidth = m_borderSize / (float)batcher.m_graphics.getWidth();
			float borderHeight = m_borderSize / (float)batcher.m_graphics.getHeight();
			
			// draw the caption
			m_caption.setText( caption );
			m_caption.drawCentered( batcher, x + borderWidth, y + borderHeight, width - borderWidth * 2.0f, height - borderHeight * 2.0f );
		}

	}

}
