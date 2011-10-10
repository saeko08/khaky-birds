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
	private 		boolean						m_loaded 	= false;
	private final 	TextureRegion[]				m_regions 	= new TextureRegion[256];
	private			int							m_glyphWidth;	// width in pixels
	private			int							m_glyphHeight;	// height in pixels
	
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
	 * Returns the glyph dimensions ( in the screen space )
	 *  
	 * @param outGlyphScreenDim
	 */
	public void getGlyphDimensions( Vector3 outGlyphScreenDim ) 
	{
		outGlyphScreenDim.set( m_glyphWidth, m_glyphHeight, 0 );
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
			// read the font description
			m_glyphWidth = fontNode.getIntValue( "cellWidth" );
			m_glyphHeight = fontNode.getIntValue( "cellHeight" );
			int cellSpacing = fontNode.getIntValue( "cellSpacing" );
			int atlasWidth = fontNode.getIntValue( "atlasWidth" );
			int atlasHeight = fontNode.getIntValue( "atlasHeight" );
			
			int cellsPerRow = atlasWidth / ( m_glyphWidth + cellSpacing );
			int cellsPerCol = atlasHeight / ( m_glyphHeight + cellSpacing );
			
			// create a render state
			RenderState renderState = new RenderState();
			renderState.deserialize( m_resMgr, fontNode );
			
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
					
					texReg = new TextureRegion( renderState, x * ( m_glyphWidth + cellSpacing ), y * ( m_glyphHeight + cellSpacing ), m_glyphWidth, m_glyphHeight );
					m_regions[texRegIdx] = texReg;
				}
				
				if ( allCharactersLoaded )
				{
					break;
				}
			}
			
					
			// set the loaded flag indicating that the font is already loaded
			m_loaded = true;
		}
	}


	@Override
	public void release() 
	{
		// nothing to do here
	}

}
