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
	
	public Texture 			m_texture;
	
	/**
	 * Default constructor.
	 */
	public TextureRegion()
	{
		m_u1 = 0;
		m_v1 = 0;
		m_u2 = 0;
		m_v2 = 0;	
		m_texture = null;
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
	public TextureRegion( Texture texture, float x, float y, float width, float height ) 
	{
		if ( texture != null )
		{
			m_u1 = x / texture.getWidth();
			m_v1 = y / texture.getHeight();
			m_u2 = m_u1 + width / texture.getWidth();
			m_v2 = m_v1 + height / texture.getHeight();
		}
		else
		{
			m_u1 = 0;
			m_v1 = 0;
			m_u2 = 0;
			m_v2 = 0;
		}
		
		m_texture = texture;
	}
	
	@Override
	public void load() 
	{
		if ( m_texture != null )
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
			String atlasName = texNode.getStringValue( "atlasName" );
			m_texture = m_resMgr.getResource( Texture.class, atlasName );
			
			float x = texNode.getFloatValue( "x" );
			float y = texNode.getFloatValue( "y" );
			float width = texNode.getFloatValue( "w" );
			float height = texNode.getFloatValue( "h" );
			
			m_u1 = x / m_texture.getWidth();
			m_v1 = y / m_texture.getHeight();
			m_u2 = m_u1 + width / m_texture.getWidth();
			m_v2 = m_v1 + height / m_texture.getHeight();
		}
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
}
