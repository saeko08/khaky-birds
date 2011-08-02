/**
 * 
 */
package com.hypefoundry.engine.core;

import com.hypefoundry.engine.game.Game;

/**
 * Resource base class.
 * 
 * @author Paksas
 *
 */
public abstract class Resource 
{
	protected 	Game			m_game;
	protected 	String			m_assetPath;
	
	/**
	 * Default constructor required by the ResourceManager
	 */
	public Resource()
	{
		m_game = null;
		m_assetPath = "";
	}
	
	/**
	 * Initializes the resource. Used exclusively by ResourceManager.
	 * 
	 * @param game
	 * @param assetPath
	 */
	void initialize( Game game, String assetPath )
	{
		m_game = game;
		m_assetPath = assetPath;
	}
	
	/**
	 * Returns the path to the asset.
	 * @return
	 */
	public final String getAssetPath() 
	{
		return m_assetPath;
	}
	
	/**
	 * Loads the resource.
	 */
	public abstract void load();
	
	/**
	 * Releases the memory used by the resource.
	 */
	public abstract void release();
	
}
