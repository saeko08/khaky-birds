/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.world.serialization.xml.XMLWorldFileLoader;


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
		// XMLDataLoader loader = XMLDataLoader.parse( stream, "Animation" );
	}
	
	@Override
	public void release() 
	{
		// nothing to do here
	}
}
