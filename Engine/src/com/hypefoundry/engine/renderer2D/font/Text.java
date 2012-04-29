/**
 * 
 */
package com.hypefoundry.engine.renderer2D.font;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;


/**
 * A text written in a specific font visible on the screen.
 * 
 * @author Paksas
 *
 */
public class Text 
{
	private String				m_text				= "";
	private Font				m_font;
	private TextureRegion[]		m_regions;
	private Vector3				m_textSizeScreen	= new Vector3();
	private Vector3				m_glyphSizeScreen	= new Vector3();
	private Vector3				m_glyphSizeWorld	= new Vector3();
	private Font.TextMetrics	m_textMetrics		= new Font.TextMetrics();
	
	/**
	 * Default constructor.
	 */
	public Text()
	{
	}
	
	/**
	 * Constructor.
	 * 
	 * @param font
	 * @param text
	 */
	public Text( Font font, String text )
	{
		m_font = font;
		m_text = text != null ? text : "";
		initialize();
	}
	
	/**
	 * Sets the font in which the text should be displayed.
	 * 
	 * @param font
	 */
	public void setFont( Font font )
	{
		if ( m_font == font )
		{
			// nothing changes - don't reinitialize if it's not necessary
			return;
		}
		
		m_font = font;
		initialize();
	}
	
	/**
	 * Sets the displayed text.
	 * 
	 * @param text
	 */
	public void setText( String text )
	{
		if ( ( m_text.length() == 0 && ( text == null || text.length() == 0 ) ) || m_text.equals( text ) )
		{
			// nothing changes - don't reinitialize if it's not necessary
			return;
		}
		
		m_text = text != null ? text : "";
		initialize();
	}
	
	/**
	 * Initializes the texture regions used to display the text.
	 */
	private void initialize()
	{
		int strLen = m_text.length();
		m_regions = new TextureRegion[strLen];
		
		if ( m_font != null )
		{
			m_font.calculateTextDimensions( m_text, m_textSizeScreen ); 

			// acquire the texture regions from which the text will be concatenated
			for ( int i = 0; i < strLen; ++i )
			{
				char c = m_text.charAt( i );
				m_regions[i] = m_font.getCharacter( c );
			}
		}
	}
	
	/**
	 * Draws the text on the screen in the specified WORLD coordinates.
	 * 
	 * @param batcher
	 * @param camera
	 * @param worldX
	 * @param worldY
	 */
	public void draw( SpriteBatcher batcher, Camera2D camera, float worldX, float worldY )
	{
		if ( m_font == null || m_regions.length == 0 )
		{
			// no text or no font set - nothing to draw then
			return;
		}
		
		m_glyphSizeScreen.set( m_font.m_whitespaceWidth, 0, 0 );
		camera.screenDimToWorld( m_glyphSizeScreen, m_glyphSizeWorld );
		float whitespaceWidth = m_glyphSizeWorld.m_x;
		
		for ( int i = 0; i < m_regions.length; ++i )
		{
			if ( m_regions[i] != null )
			{
				m_glyphSizeScreen.set( m_regions[i].width(), m_regions[i].height(), 0 );
				camera.screenDimToWorld( m_glyphSizeScreen, m_glyphSizeWorld ); 
				
				batcher.drawSprite( worldX, worldY, m_glyphSizeWorld.m_x, m_glyphSizeWorld.m_y, m_regions[i] );
				
				worldX += m_glyphSizeWorld.m_x;
			}
			else
			{
				worldX += whitespaceWidth;
			}
		}
	}
	
	/**
	 * Draws the text on the screen in the specified SCREEN coordinates, centered in the specified box.
	 * 
	 * @param batcher
	 * @param screenX
	 * @param screenY
	 * @param screenWidth		how wide is the text field
	 * @param screenHeight		how high is the text field
	 */
	public void drawCentered( SpriteBatcher batcher, float screenX, float screenY, float screenWidth, float screenHeight )
	{
		if ( m_font == null || m_regions.length == 0 )
		{
			// no text or no font set - nothing to draw then
			return;
		}
		
		// viewport dimensions
		Vector3 viewportDim = batcher.m_graphics.getViewportDimensions();
		float viewportWidth = viewportDim.m_x;
		float viewportHeight = viewportDim.m_y;
		
		// calculate the maximum number of characters that we'll be able to write
		m_font.getDesiredCharactersCount( m_text, screenWidth, viewportWidth, m_textMetrics );
		
		// calculate the text dimensions
		float whitespaceWidth = m_font.m_whitespaceWidth / viewportWidth;
		float textHeightWorld = m_textSizeScreen.m_y / viewportHeight;

		for ( int i = 0; i < m_textMetrics.m_charactersCount; ++i )
		{
			if ( m_regions[i] != null )
			{
				float glyphViewportWidth = m_regions[i].widthInPixels() / viewportWidth;
				float glyphViewportHeight = m_regions[i].heightInPixels() / viewportHeight;
				
				float left = screenX + ( screenWidth - m_textMetrics.m_actualTextWidth ) * 0.5f;
				float top = screenY + ( screenHeight - textHeightWorld ) * 0.5f;
				
				batcher.drawUnalignedSprite( left, top, glyphViewportWidth, glyphViewportHeight, m_regions[i] );
				
				screenX += glyphViewportWidth;
			}
			else
			{
				screenX += whitespaceWidth;
			}
		}
	}
	
	/**
	 * Returns the text length.
	 * 
	 * @return
	 */
	public float length()
	{
		return m_textSizeScreen.m_x;
	}
}
