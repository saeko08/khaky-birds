/**
 * 
 */
package com.hypefoundry.kabloons.entities.menu;

import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.kabloons.entities.menu.MenuItem.State;

/**
 * @author Paksas
 *
 */
public class MenuItemController extends FiniteStateMachine implements MenuItemStateListener
{
	private	MenuItem				m_menuItem;
	private SteeringBehaviors		m_sb;
	
	
	// ------------------------------------------------------------------------
	// States
	// ------------------------------------------------------------------------
	
	/**
	 * Item is deselected.
	 * 
	 * @author Paksas
	 */
	class Deselected extends FSMState
	{		
		@Override
		public void activate()
		{
			// lower the movement speed limit
			m_menuItem.query( DynamicObject.class ).m_linearSpeed = 2.0f;
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_sb.begin().arrive( m_menuItem.m_desiredPosition, 0.3f );
		}
		
	}
	
	/**
	 * Item is selected.
	 * 
	 * @author Paksas
	 */
	class Selected extends FSMState
	{		
		@Override
		public void activate()
		{
			// raise the movement speed limit
			m_menuItem.query( DynamicObject.class ).m_linearSpeed = 5.0f;

			// go to the center of the screen and stay there
			m_sb.begin().arrive( MenuManagerController.MENU_RING_CENTER, 0.3f ); 
		}
		
		@Override
		public void deactivate()
		{
			m_sb.clear();
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param menuItemEntity
	 */
	public MenuItemController( Entity menuItemEntity ) 
	{
		super( menuItemEntity );
		
		m_menuItem = (MenuItem)menuItemEntity;
		
		// attach the state listener to the entity - we'll use it to keep monitoring
		// the external state change requests that come from the menu manager.
		m_menuItem.attachStateListener( this );
		
		m_sb = new SteeringBehaviors( m_menuItem );
		
		// register states
		register( new Selected() );
		register( new Deselected() );
		begin( Deselected.class );
	}

	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update( deltaTime );
	}

	@Override
	public void onStateChanged( State newState ) 
	{
		// switch the controller's state according to the state of the entity
		switch( newState )
		{
			case Deselected:
			{
				this.transitionTo( Deselected.class );
				break;
			}
			
			case Selected:
			{
				this.transitionTo( Selected.class );
				break;
			}
		}
	}
}
