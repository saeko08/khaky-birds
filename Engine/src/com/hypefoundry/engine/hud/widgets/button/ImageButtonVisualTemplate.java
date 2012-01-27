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
 * A frame visual template that allows you to substitute a custom bitmap for a default button border.
 * 
 * @author Paksas
 */
public class ImageButtonVisualTemplate extends ButtonVisualTemplate 
{
	private TextureRegion		m_textureRegion;
	private float				m_borderSize;
	public Text					m_caption	= new Text();
	
	
	/**
	 * Default constructor.
	 */
	public ImageButtonVisualTemplate() 
	{
		super( 0 );
	}
	
	/**
	 * Constructor ( if you want to create complex widgets with many embedded buttons that need to be identified when clicked ).
	 * 
	 * @param id
	 */
	public ImageButtonVisualTemplate( int id )
	{
		super( id );
	}
	
	@Override
	public void load( ResourceManager resMgr, DataLoader loader ) 
	{
		RenderState rs = new RenderState();
		rs.deserialize( resMgr, loader );
		
		String texturePath = loader.getStringValue( "path" );
		m_textureRegion = resMgr.getResource( TextureRegion.class, texturePath );
			
		m_borderSize = loader.getFloatValue( "borderSize", 0.0f );
		
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
		if ( m_textureRegion != null )
		{
			batcher.drawUnalignedSprite( x, y, width, height, m_textureRegion );
			
			float borderWidth = m_borderSize / (float)batcher.m_graphics.getWidth();
			float borderHeight = m_borderSize / (float)batcher.m_graphics.getHeight();
			
			// draw the caption
			m_caption.setText( caption );
			m_caption.drawCentered( batcher, x + borderWidth, y + borderHeight, width - borderWidth * 2.0f, height - borderHeight * 2.0f );
		}
	}

}
