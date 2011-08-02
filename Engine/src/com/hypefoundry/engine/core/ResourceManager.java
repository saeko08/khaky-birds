/**
 * 
 */
package com.hypefoundry.engine.core;

import java.util.*;

import com.hypefoundry.engine.game.Game;

/**
 * Resources manager interface.
 * 
 * @author Paksas
 *
 */
public class ResourceManager 
{
	private Game 			m_game;
	
	List< Resource >		m_resources;
	boolean					m_isActive;				
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 */
	public ResourceManager( Game game )
	{
		m_game = game;
		
		m_resources = new ArrayList< Resource >();
		m_isActive = false;
	}
	
	/**
	 * Returns a resource of the specified type.
	 * 
	 * @param type
	 * @param assetPath
	 * @return
	 */
	public < T extends Resource > T getResource( Class< T > type, String assetPath )
	{
		// try finding an existing resource that matches the name and the type
		for ( Resource res : m_resources )
		{
			String resAssetPath = res.getAssetPath();
			if ( type.isInstance( res ) && resAssetPath.equals( assetPath ) )
			{
				// we found it
				return (T)res;
			}
		}
		
		// the resource wasn't found - instantiate it
		T newResource = null;
		try 
		{
			newResource = type.newInstance();
		} 
		catch (InstantiationException e) 
		{
			return null;
		} 
		catch (IllegalAccessException e) 
		{
			return null;
		}
		
		if ( newResource != null )
		{
			newResource.initialize( m_game, assetPath );
			if ( m_isActive )
			{
				// load the resource, if the resources manager is active
				newResource.load();
			}
			
			m_resources.add( newResource );
		}
		return newResource;
	}

	/**
	 * Loads all registered resources after they have been unloaded.
	 */
	public void loadResources() 
	{
		m_isActive = true;
		for ( Resource res : m_resources )
		{
			res.load();
		}
		
	}

	/**
	 * Releases all registered resources, keeping track of them however
	 * for future reloads.
	 */
	public void releaseResources() 
	{
		for ( Resource res : m_resources )
		{
			res.release();
		}
		m_isActive = false;
	}
}
