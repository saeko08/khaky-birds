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
	private Vector3				m_glyphSizeScreen	= new Vector3();
	private Vector3				m_glyphSizeWorld	= new Vector3();
	private float				m_length;
	
	
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
			m_font.getGlyphDimensions( m_glyphSizeScreen ); 
		}
		
		// acquire the texture regions from which the text will be concatenated
		for ( int i = 0; i < strLen; ++i )
		{
			char c = m_text.charAt( i );
			m_regions[i] = m_font != null ? m_font.getCharacter( c ) : null;
		}
		
		// calculate the length ( in pixels ) the text will occupy
		m_length = strLen * m_glyphSizeScreen.m_x;
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
		
		camera.screenDimToWorld( m_glyphSizeScreen, m_glyphSizeWorld ); 

		for ( int i = 0; i < m_regions.length; ++i )
		{
			if ( m_regions[i] != null )
			{
				batcher.drawSprite( worldX, worldY, m_glyphSizeWorld.m_x, m_glyphSizeWorld.m_y, m_regions[i] );
			}
			worldX += m_glyphSizeWorld.m_x;
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
		
		// calculate the maximum number of characters that we'll be able to write
		int maxCharacters = m_font.getDesiredCharactersCount( screenWidth, batcher.m_graphics.getWidth() );
		int charactersCount = m_regions.length;
		if ( maxCharacters < charactersCount )
		{
			charactersCount = maxCharacters;
		}
		
		// calculate the text dimensions
		float glyphWidth = m_glyphSizeScreen.m_x / (float)batcher.m_graphics.getWidth();
		float glyphHeight = m_glyphSizeScreen.m_y / (float)batcher.m_graphics.getHeight();
		
		float textHalfWidth = glyphWidth * charactersCount * 0.5f;
		float textHalfHeight = glyphHeight * 0.5f;

		for ( int i = 0; i < charactersCount; ++i )
		{
			if ( m_regions[i] != null )
			{
				batcher.drawUnalignedSprite( screenX + ( screenWidth * 0.5f - textHalfWidth ), screenY + ( screenHeight * 0.5f - textHalfHeight ), glyphWidth, glyphHeight, m_regions[i] );
			}
			screenX += glyphWidth;
		}
	}
	
	/**
	 * Returns the text length.
	 * 
	 * @return
	 */
	public float length()
	{
		return m_length;
	}
}
