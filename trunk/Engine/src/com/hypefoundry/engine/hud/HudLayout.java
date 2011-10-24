/**
 * 
 */
package com.hypefoundry.engine.hud;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.hypefoundry.engine.core.Resource;
import com.hypefoundry.engine.hud.HudComposite;
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
	private HudComposite			m_hudElements;
	private HudRenderer				m_renderer;
	
	private	List< ButtonListener >	m_buttonListeners = new ArrayList< ButtonListener >();
	
	// ------------------------------------------------------------------------
	// Renderer management
	// ------------------------------------------------------------------------
	
	/**
	 * Attaches a renderer.
	 * 
	 * @param renderer
	 */
	public void attachRenderer( HudRenderer renderer )
	{
		if ( renderer == null )
		{
			return;
		}
		
		// make sure we inform the old renderer that we no longer want to use it
		detachRenderer( renderer );
		
		// set the new renderer
		m_renderer = renderer;
		
		if ( m_hudElements != null )
		{
			// inform it about the elements of the layout
			ArrayList< HudWidget > widgets = new ArrayList< HudWidget >();
			m_hudElements.gatherWidgets( widgets );
			
			m_renderer.onLayoutLoaded( widgets );
		}
	}
	
	/**
	 * Detaches a renderer.
	 * 
	 * @param renderer
	 */
	public void detachRenderer( HudRenderer renderer )
	{
		if ( renderer != null && m_renderer == renderer )
		{
			m_renderer.onLayoutReleased();
			m_renderer = null;
		}
	}
	
	// ------------------------------------------------------------------------
	// Widget listeners
	// ------------------------------------------------------------------------
		
	/**
	 * Attaches a button listener.
	 * 
	 * @param listener
	 */
	public void attachButtonListener( ButtonListener listener )
	{
		if ( listener != null )
		{
			m_buttonListeners.add( listener );
		}
	}
	
	/**
	 * Detaches a button listener.
	 * 
	 * @param listener
	 */
	public void detachButtonListener( ButtonListener listener )
	{
		if ( listener != null )
		{
			m_buttonListeners.remove( listener );
		} 
	}
	
	/**
	 * Looks for a widget with the specified ID in the current layout.
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public < T extends HudWidget > T getWidget( Class< T > type, String id )
	{
		ArrayList< HudWidget > widgets = new ArrayList< HudWidget >();
		m_hudElements.gatherWidgets( widgets );
		
		int count = widgets.size();
		for ( int i = 0; i < count; ++i )
		{
			HudWidget widget = widgets.get(i);
			if ( widget.m_id.equals( id ) && type.isInstance( widget ) )
			{
				return (T)widget;
			}
		}
		
		return null;
	}
	
	// ------------------------------------------------------------------------
	// Notifications
	// ------------------------------------------------------------------------
	
	/**
	 * Called when a button is pressed.
	 * 
	 * @param id
	 */
	public void onButtonPressed( String id )
	{
		int count = m_buttonListeners.size();
		for ( int i = 0; i < count; ++i )
		{
			m_buttonListeners.get(i).onButtonPressed( id );
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
			m_hudElements.load( m_resMgr, hudNode );
			m_hudElements.initialize( null, this );
			
			// inform the renderer about the layout elements
			if ( m_renderer != null )
			{
				ArrayList< HudWidget > widgets = new ArrayList< HudWidget >();
				m_hudElements.gatherWidgets( widgets );
						
				m_renderer.onLayoutLoaded( widgets );
			}
		}
	}

	@Override
	public void release() 
	{
		if ( m_renderer != null )
		{
			m_renderer.onLayoutReleased();
		}
	}
}
