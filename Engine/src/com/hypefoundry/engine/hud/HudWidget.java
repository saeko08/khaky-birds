/**
 * 
 */
package com.hypefoundry.engine.hud;


import java.util.List;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;

/**
 * A single hud element.
 * 
 * @author Paksas
 *
 */
public abstract class HudWidget
{
	public Vector3			m_position = new Vector3();
	public float			m_width;
	public float			m_height;
	public String			m_visualName;
	public String			m_id;
	
	public HudWidget 		m_parent;
	public HudLayout		m_layout;
	
	/**
	 * Called when this element gets parented by another one.
	 * 
	 * @param parentWidget
	 * @param layout
	 */
	void initialize( HudWidget parentWidget, HudLayout layout )
	{
		m_parent = parentWidget;
		m_layout = layout;
	}
	
	/**
	 * Loads the HUD layout ( what widgets does it contain and how are they located )
	 * 
	 * @pararm resMgr
	 * @param loader
	 */
	public final void load( ResourceManager resMgr, DataLoader loader )
	{
		m_position.load( "pos", loader );
		m_width = loader.getFloatValue( "width" );
		m_height = loader.getFloatValue( "height" );
		m_visualName = loader.getStringValue( "visualClass" );
		m_id = loader.getStringValue( "id" );
		onLoad( resMgr, loader );
	}
	
	/**
	 * Deserializes element-specific data.
	 * 
	 * @param resMgr
	 * @param loader
	 */
	public abstract void onLoad( ResourceManager resMgr, DataLoader loader );
	
	/**
	 * Gathers elements of the layout in the order of their appearance.
	 * 
	 * @param widgets
	 */
	void gatherWidgets( List< HudWidget > widgets )
	{
		widgets.add( this );
	}
}
