/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.MainMenu;

/**
 * @author Paksas
 *
 */
public class LevelItem extends Entity 
{
	enum State
	{
		Inactive( "hud/menu/levelSelection/inactiveLevel.xml" ),
		Selected( "hud/menu/levelSelection/selectedLevel.xml" ),
		Deselected( "hud/menu/levelSelection/deselectedLevel.xml" );
		
		public String		m_animPath;
		State( String animPath )
		{
			m_animPath = animPath;
		}
	}

	MainMenu			m_menuScreen;
	int					m_levelIdx;
	BoundingBox			m_clearSkyArea = new BoundingBox();
	
	
	private boolean		m_canBeActivated 	= true;
	State				m_state 			= State.Inactive;
	
	/**
	 * Constructor.
	 * 
	 * @param menuScreen
	 */
	public LevelItem( MainMenu menuScreen )
	{
		m_menuScreen = menuScreen;
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_levelIdx = loader.getIntValue( "levelIdx" );
		m_canBeActivated = loader.getStringValue( "active" ).equalsIgnoreCase( "false" ) == false;
		
		m_clearSkyArea.load( "ClearSkyArea", loader );
		
		DataLoader clearSkyExtensionNode = loader.getChild( "ClearSkyExtension" );
		if ( clearSkyExtensionNode != null )
		{
			float widthExtension = clearSkyExtensionNode.getFloatValue( "width" );
			float heightExtension = clearSkyExtensionNode.getFloatValue( "height" );
			m_clearSkyArea.m_minX -= widthExtension;
			m_clearSkyArea.m_maxX += widthExtension;
			m_clearSkyArea.m_minY -= heightExtension;
			m_clearSkyArea.m_maxY += heightExtension;
		}
	}

	/**
	 * Loads the level associated with the item.
	 */
	public void activate()
	{
		if ( m_state != State.Inactive )
		{
			m_menuScreen.loadLevel( m_levelIdx );
		}
	}

	/**
	 * Sets a new state.
	 * 
	 * @param newState
	 */
	public void setState( State newState ) 
	{
		// ignore all requests unless the level can be activated
		if ( m_canBeActivated )
		{
			m_state = newState;
		}
		
	}
}
