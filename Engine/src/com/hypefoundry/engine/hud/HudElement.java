/**
 * 
 */
package com.hypefoundry.engine.hud;

import com.hypefoundry.engine.hud.Hud;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A single hud element.
 * 
 * @author Paksas
 *
 */
public abstract class HudElement 
{
	public Vector3			m_position = new Vector3();
	public float			m_width;
	public float			m_height;
	
	Hud						m_hud;
	
	
	/**
	 * Draws the HUD element.
	 * 
	 * @param batcher
	 * @param camera
	 * @param deltaTime
	 */
	public abstract void draw( SpriteBatcher batcher, float deltaTime );
	
	/**
	 * Sets the parent HUD instance.
	 * 
	 * @param hud
	 */
	void setParentHud( Hud hud )
	{
		m_hud = hud;
	}
	
	/**
	 * Loads the HUD layout ( what widgets does it contain and how are they located )
	 * 
	 * @pararm resMgr
	 * @param loader
	 */
	public final void load( DataLoader loader )
	{
		m_position.load( "pos", loader );
		m_width = loader.getFloatValue( "width" );
		m_height = loader.getFloatValue( "height" );
		
		onLoad( loader );
	}
	
	/**
	 * Deserializes element-specific data.
	 * 
	 * @param resMgr
	 * @param loader
	 */
	public abstract void onLoad( DataLoader loader );
}
