/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import java.io.IOException;
import java.io.InputStream;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;

/**
 * A region in an atlas that specifies a texture.
 * 
 * @author paksas
 *
 */
public class TextureRegion extends Resource
{
	public float 			m_u1, m_v1;
	public float 			m_u2, m_v2;
	
	public RenderState		m_renderState = new RenderState();
	
	/**
	 * Default constructor.
	 */
	public TextureRegion()
	{
		m_u1 = 0;
		m_v1 = 0;
		m_u2 = 0;
		m_v2 = 0;	
	}
	
	/**
	 * Constructor.
	 * 
	 * * @param renderState
	 */
	public TextureRegion( RenderState renderState )
	{
		m_u1 = 0;
		m_v1 = 0;
		m_u2 = 0;
		m_v2 = 0;	
		m_renderState = renderState;
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TextureRegion( RenderState renderState, float x, float y, float width, float height ) 
	{
		if ( renderState != null )
		{
			m_u1 = x / renderState.m_texture.getWidth();
			m_v1 = y / renderState.m_texture.getHeight();
			m_u2 = m_u1 + width / renderState.m_texture.getWidth();
			m_v2 = m_v1 + height / renderState.m_texture.getHeight();
		}
		else
		{
			m_u1 = 0;
			m_v1 = 0;
			m_u2 = 0;
			m_v2 = 0;
		}
		
		m_renderState = renderState;
	}
	
	// ------------------------------------------------------------------------
	// Resource implementation
	// ------------------------------------------------------------------------
	/**
	 * Loads texel coordinates from a data loader.
	 * 
	 * @param loader
	 */
	public void deserializeCoordinates( DataLoader loader )
	{	
		float x = loader.getFloatValue( "x" );
		float y = loader.getFloatValue( "y" );
		float width = loader.getFloatValue( "w" );
		float height = loader.getFloatValue( "h" );
		
		m_u1 = x / m_renderState.m_texture.getWidth();
		m_v1 = y / m_renderState.m_texture.getHeight();
		m_u2 = m_u1 + width / m_renderState.m_texture.getWidth();
		m_v2 = m_v1 + height / m_renderState.m_texture.getHeight();
	}
	
	
	@Override
	public void load() 
	{
		if ( m_renderState.m_texture != null )
		{
			// texture is already loaded
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
		DataLoader texNode = XMLDataLoader.parse( stream, "TextureRegion" );
		if ( texNode != null )
		{
			m_renderState.deserialize( m_resMgr, texNode );
			deserializeCoordinates( texNode );
		}
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
}
