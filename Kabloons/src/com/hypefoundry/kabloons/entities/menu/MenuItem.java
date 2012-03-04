/**
 * 
 */
package com.hypefoundry.kabloons.entities.menu;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.kabloons.MainMenu;

/**
 * @author Paksas
 *
 */
public class MenuItem extends Entity 
{
	public enum State
	{
		Selected,
		Deselected
	}
	
	public String				m_path;
	public String				m_menuOption;
	
	public MainMenu				m_menuScreen;
	
	public Vector3				m_desiredPosition = new Vector3();
	
	State						m_state			= State.Deselected;
	MenuItemStateListener		m_stateListener;
	
	/**
	 * Constructor.
	 * 
	 * @param mainMenuScreen
	 */
	public MenuItem( MainMenu mainMenuScreen )
	{
		m_menuScreen = mainMenuScreen;
		
		float maxLinearSpeed = 2.0f;
		float maxAngularSpeed = 0.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxAngularSpeed ) );
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_path = loader.getStringValue( "path" );
		m_menuOption = loader.getStringValue( "menuOption" );
	}
	
	@Override
	public void onAddedToWorld( World world )
	{
		MenuManager manager = (MenuManager) world.findEntity( MenuManager.class );
		manager.addItem( this );
	}

	/**
	 * Changes item selection state.
	 *
	 * @param selected
	 */
	public void setSelected( boolean selected ) 
	{
		m_state = selected ? State.Selected : State.Deselected;
		
		if ( m_stateListener != null )
		{
			// inform the listener about the state change
			m_stateListener.onStateChanged( m_state );
		}
	}
	
	/**
	 * Attaches a listener that will be informed about the entity's state changes.
	 * @param listener
	 */
	void attachStateListener( MenuItemStateListener listener )
	{
		m_stateListener = listener;
		if ( m_stateListener != null )
		{
			// send an initial info about entity's current state
			m_stateListener.onStateChanged( m_state );
		}
	}
	
	/**
	 * Activates the menu action associated with the item
	 */
	public void activateItem() 
	{
		m_menuScreen.onMenuItemClicked( m_menuOption );
		
	}
}
