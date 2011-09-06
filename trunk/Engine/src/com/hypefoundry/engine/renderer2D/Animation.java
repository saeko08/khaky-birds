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
 * Keyframed texture animation.
 * 
 * @author Paksas
 */
public class Animation extends Resource
{
	private float				m_frameDuration;
	private TextureRegion[]		m_regions;
	
	/**
	 * Default constructor.
	 */
	public Animation()
	{
		m_frameDuration = 0.0f;
		m_regions = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param frameDuration
	 * @param regions
	 */
	public Animation( float frameDuration, TextureRegion[] regions )
	{
		m_frameDuration = frameDuration;
		m_regions = regions;
	}
	
	/**
	 * Returns a texture regions that should be used to display
	 * an animation frame that corresponds to the specified animation time.
	 * 
	 * @param animationTime
	 * @return
	 */
	public TextureRegion animate( float animationTime )
	{
		// get the frame index
		int animIdx = (int)( animationTime / m_frameDuration ) % m_regions.length;
		return m_regions[animIdx];
	}
	
	@Override
	public void load() 
	{
		if ( m_regions != null )
		{
			// animation is already loaded
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
		DataLoader animNode = XMLDataLoader.parse( stream, "Animation" );
		if ( animNode != null )
		{
			m_frameDuration = animNode.getFloatValue( "frameDuration" );
			String atlasName = animNode.getStringValue( "atlasName" );
			Texture atlas = m_resMgr.getResource( Texture.class, atlasName );
			
			m_regions = new TextureRegion[ animNode.getChildrenCount( "TextureRegion" ) ];
			int i = 0;
			for ( DataLoader regionNode = animNode.getChild( "TextureRegion" ); regionNode != null; regionNode = regionNode.getSibling(), ++i )
			{
				m_regions[i] = new TextureRegion( atlas );
				m_regions[i].deserializeCoordinates( regionNode );
				m_regions[i].m_renderState.deserialize( m_resMgr, regionNode );
			}
		}
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
}
