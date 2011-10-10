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
		
		for ( int i = 0; i < strLen; ++i )
		{
			char c = m_text.charAt( i );
			m_regions[i] = m_font != null ? m_font.getCharacter( c ) : null;
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
}
