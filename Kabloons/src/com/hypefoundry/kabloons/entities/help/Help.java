/**
 * 
 */
package com.hypefoundry.kabloons.entities.help;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class Help extends Entity 
{
	String 				m_imagePath;
	boolean				m_isVisible 		= false;
	
	HelpController		m_listener			= null;
	
	
	/**
	 * Toggles visibility of the help element.
	 */
	public void toggleVisible()
	{
		m_isVisible = !m_isVisible;
		
		if ( m_listener != null )
		{
			m_listener.onHelpVisibilityChanged();
		}
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_imagePath = loader.getStringValue( "path" );
	}
}
