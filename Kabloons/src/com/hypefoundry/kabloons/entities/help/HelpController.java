/**
 * 
 */
package com.hypefoundry.kabloons.entities.help;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class HelpController extends EntityController
{
	private Help		m_help;
	private float		m_lifeTimer;
	
	/**
	 * Constructor.
	 * 
	 * @param helpEntity
	 */
	public HelpController( Entity helpEntity ) 
	{
		super( helpEntity );
		
		m_help = (Help)helpEntity;
		m_lifeTimer = 0.0f;
		
		m_help.m_listener = this;
	}

	@Override
	public void update( float deltaTime ) 
	{
		if ( m_lifeTimer > 0 )
		{
			m_lifeTimer -= deltaTime;
			
			if ( m_lifeTimer <= 0 )
			{
				// hide the help image
				m_help.m_isVisible = false;
			}
		}
	}
	
	/**
	 * Called when help visibility changes.
	 */
	void onHelpVisibilityChanged()
	{
		if ( m_help.m_isVisible )
		{
			// show the help for 5 seconds, and then hide it automatically
			m_lifeTimer = 5.0f;
		}
		else
		{
			m_lifeTimer = 0.0f;
		}
	}
}
