/**
 * 
 */
package com.hypefoundry.engine.renderer2D.font;

import java.io.IOException;
import java.io.InputStream;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;


/**
 * A graphical font resource.
 * 
 * @author Paksas
 *
 */
public class Font extends Resource
{
	public static class TextMetrics
	{
		public int 		m_charactersCount;
		public float 	m_actualTextWidth;
		
		void reset()
		{
			m_charactersCount = 0;
			m_actualTextWidth = 0;
		}
	}
	
	public 			int							m_whitespaceWidth = 15;	
	private 		boolean						m_loaded 	= false;
	private final 	TextureRegion[]				m_regions 	= new TextureRegion[256];
	
	private final	int[]						m_defaultCharMapping = 
		{
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '!', '?', ',', '.', '-', '+', '#', '@',
			'$', '%', '^', '&', '*', '(', ')', '=', '[', ']', '{', '}', '\\', '/',
			'<', '>', ':', ';',
		};
	
	/**
	 * Constructor.
	 */
	public Font() 
	{
	}

	/**
	 * Returns a texture region with the specified character glyph.
	 * 
	 * @param characterIdx
	 * @return
	 */
	public TextureRegion getCharacter( char characterIdx ) 
	{
		return m_regions[ characterIdx ];
	}
	
	/**
	 * Returns the dimensions of the specified text written using this font
	 *  
	 * @param outGlyphScreenDim
	 */
	public void calculateTextDimensions( String text, Vector3 outScreenDim ) 
	{
		outScreenDim.m_x = 0;
		outScreenDim.m_y = 0;
		
		int strLen = text.length();
		for ( int i = 0; i < strLen; ++i )
		{
			char c = text.charAt( i );
			if ( m_regions[c] != null )
			{
				outScreenDim.m_x += m_regions[c].widthInPixels();
			
				float height = m_regions[c].heightInPixels();
				if ( outScreenDim.m_y < height )
				{
					outScreenDim.m_y = height;
				}
			}
			else
			{
				outScreenDim.m_x += m_whitespaceWidth;
			}
		}
	}
	
	/**
	 * Returns the number of characters that will fit the specified number of pixels.
	 * 
	 * @param textFieldWidthWorld
	 * @param viewportWidth
	 * @return
	 */
	public void getDesiredCharactersCount( String text, float textFieldWidthWorld, float viewportWidth, TextMetrics outMetrics )
	{
		outMetrics.reset();
		
		int strLen = text.length();
		for ( int i = 0; i < strLen; ++i )
		{
			float newViewportWidth = outMetrics.m_actualTextWidth;
			
			char c = text.charAt( i );
			TextureRegion glyph = m_regions[c];
			if ( glyph != null )
			{
				float widthInPixels = glyph.widthInPixels();
				newViewportWidth += widthInPixels / viewportWidth;
			}
			else
			{
				newViewportWidth += m_whitespaceWidth * viewportWidth;
			}
						
			if ( newViewportWidth > textFieldWidthWorld )
			{
				// ok - that's far enough - stop here
				break;
			}
			else
			{
				outMetrics.m_actualTextWidth = newViewportWidth;
				outMetrics.m_charactersCount++;
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// Resource implementation
	// ------------------------------------------------------------------------

	@Override
	public void load() 
	{
		if ( m_loaded )
		{
			// font is already loaded
			return;
		}
		
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
		DataLoader fontNode = XMLDataLoader.parse( stream, "Font" );
		if ( fontNode != null )
		{
			// create a render state
			RenderState renderState = new RenderState();
			renderState.deserialize( m_resMgr, fontNode );
			
			// read the font description
			m_whitespaceWidth = fontNode.getIntValue( "whitespaceWidth", 15 );
			String size = fontNode.getStringValue( "size" );
			if ( size.equals( "fixed" ) )
			{
				parseConstantSizeGlyph( fontNode, renderState );
			}
			else
			{
				parseUserDefinedGlyph( fontNode, renderState );
			}
			
					
			// set the loaded flag indicating that the font is already loaded
			m_loaded = true;
		}
	}
	
	private void parseUserDefinedGlyph( DataLoader fontNode, RenderState renderState )
	{
		for ( DataLoader glyphNode = fontNode.getChild( "Glyph" ); glyphNode != null; glyphNode = glyphNode.getSibling() )
		{
			char character = glyphNode.getStringValue( "char" ).charAt(0);
									
			m_regions[character] = new TextureRegion( renderState );
			m_regions[character].deserializeCoordinates( glyphNode );
		}
	}

	private void parseConstantSizeGlyph( DataLoader fontNode, RenderState renderState )
	{
		int cellSpacing = fontNode.getIntValue( "cellSpacing" );
		int atlasWidth = fontNode.getIntValue( "atlasWidth" );
		int atlasHeight = fontNode.getIntValue( "atlasHeight" );
		int	glyphWidth = fontNode.getIntValue( "cellWidth" );
		int	glyphHeight = fontNode.getIntValue( "cellHeight" );
		
		int cellsPerRow = atlasWidth / ( glyphWidth + cellSpacing );
		int cellsPerCol = atlasHeight / ( glyphHeight + cellSpacing );
		
		
		// load the texture regions
		boolean allCharactersLoaded = false;
		TextureRegion texReg;
		for ( int y = 0; y < cellsPerCol; ++y )
		{
			for ( int x = 0; x < cellsPerRow; ++x )
			{
				int addr = y * cellsPerRow + x;
				if ( addr >= m_defaultCharMapping.length )
				{
					// make sure we exit from the outer loop as well
					allCharactersLoaded = true;
					break;
				}
				
				int texRegIdx = m_defaultCharMapping[addr];
				
				texReg = new TextureRegion( renderState, x * ( glyphWidth + cellSpacing ), y * ( glyphHeight + cellSpacing ), glyphWidth, glyphHeight );
				m_regions[texRegIdx] = texReg;
			}
			
			if ( allCharactersLoaded )
			{
				break;
			}
		}
	}

	@Override
	public void release() 
	{
		// nothing to do here
	}

}
