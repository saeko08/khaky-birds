/**
 * 
 */
package com.hypefoundry.engine.hud;

import java.io.IOException;
import java.io.InputStream;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.hud.HudComposite;
import com.hypefoundry.engine.hud.HudElement;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.xml.XMLDataLoader;

/**
 * Defines the HUD contents.
 * 
 * @author Paksas
 *
 */
public class HudLayout extends Resource
{
	HudComposite		m_hudElements;
	Hud					m_hud;
	
	public void setHud( Hud hud )
	{
		m_hud = hud;
		
		if ( m_hudElements != null )
		{
			m_hudElements.setParentHud( hud );
		}
	}

	/**
	 * Clears the existing HUD.
	 */
	public void clear()
	{
		m_hudElements = new HudComposite();
	}
	
	/**
	 * Adds a new HUD element. 
	 * 
	 * @param element
	 */
	public void addElement( HudElement element )
	{
		if ( m_hudElements != null )
		{
			m_hudElements.addElement( element );
		}
	}
	
	/**
	 * Draws the HUD on the screen.
	 * 
	 * @param batcher
	 * @param deltaTime
	 */
	public void draw( SpriteBatcher batcher, float deltaTime )
	{
		if ( m_hudElements != null )
		{
			m_hudElements.draw( batcher, deltaTime );
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Resource implementation
	// ------------------------------------------------------------------------
	@Override
	public void load() 
	{
		// clear the definition, if it's already loaded
		m_hudElements = new HudComposite();
				
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
		DataLoader hudNode = XMLDataLoader.parse( stream, "Layout" );
		if ( hudNode != null )
		{
			m_hudElements.load( hudNode );
			
			if ( m_hud != null )
			{
				m_hudElements.setParentHud( m_hud );
			}
		}
	}

	@Override
	public void release() 
	{
	}
}
