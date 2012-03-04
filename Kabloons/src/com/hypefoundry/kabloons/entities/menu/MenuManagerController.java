/**
 * 
 */
package com.hypefoundry.kabloons.entities.menu;


import java.util.List;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author Paksas
 *
 */
public class MenuManagerController extends EntityController implements InputHandler
{
	private MenuManager				m_menuManager;
	private World					m_world;
	private Camera2D				m_camera;
	
	
	private	MenuItem				m_currentlySelectedItem 		= null;
	float 							m_simulationTime;
	
	private Vector3					m_touchPos 						= new Vector3();
	
	public static final Vector3		MENU_RING_CENTER				= new Vector3( 2.4f, 2.0f, 50.0f );
	final float						ITEMS_RING_RADIUS				= 1.6f;
	final float						PI2								= (float)Math.PI * 2.0f;
	final float						RING_ROTATION_SPEED 			= 0.1f * PI2;
	final float						MENU_ITEM_SEARCH_RADIUS 		= 0.5f;
	
	
	/**
	 * Constructor.
	 * 
	 * @param menuEntity
	 * @param world
	 * @param screen
	 * @param camera
	 */
	public MenuManagerController( Entity menuEntity, World world, Screen screen, Camera2D camera ) 
	{
		super( menuEntity );
		
		m_menuManager = (MenuManager)menuEntity;
		m_world = world;
		m_camera = camera;
		
		screen.registerInputHandler( this );
	}


	@Override
	public void update( float deltaTime ) 
	{
		m_simulationTime += deltaTime;
		
		// set the desired positions of the items
		int itemsCount = m_menuManager.m_managedItems.size(); 
		for ( int i = 0; i < itemsCount; ++i )
		{
			MenuItem item = m_menuManager.m_managedItems.get(i);
			
			// set item's desired position 
			calcItemPosition( i, item.m_desiredPosition );
		}
	}
	
	/**
	 * Calculates the position the item should occupy.
	 * 
	 * @param itemIdx
	 * @param outPos
	 */
	private final void calcItemPosition( int itemIdx, Vector3 outPos )
	{
		// calculate the items position based on the current time
		float dAngle = PI2 / (float)m_menuManager.m_managedItems.size();
		float rotationAngle = RING_ROTATION_SPEED * m_simulationTime;
		
		float angle = itemIdx * dAngle + rotationAngle;
		
		outPos.m_x = (float)Math.cos( angle ) * ITEMS_RING_RADIUS;
		outPos.m_y =(float)Math.sin( angle ) * ITEMS_RING_RADIUS;
		outPos.add( MENU_RING_CENTER );
	}


	@Override
	public boolean handleInput( Input input, float deltaTime ) 
	{
		boolean inputHandled = false;
		
		List< TouchEvent > inputEvents = input.getTouchEvents();
		int count = inputEvents.size();
		for ( int i = 0 ; i < count; ++i )
		{				
			TouchEvent lastEvent = inputEvents.get(i);
			if ( lastEvent.type == TouchEvent.TOUCH_DOWN )
			{
				// change the gesture direction from screen to model space
				m_touchPos.set( lastEvent.x, lastEvent.y, 0 );
				m_camera.screenPosToWorld( m_touchPos, m_touchPos );
				
				MenuItem item = (MenuItem)m_world.findNearestEntity( MenuItem.class, MENU_ITEM_SEARCH_RADIUS, m_touchPos );
				if ( item != null )
				{
					// found one
					toggleItemSelection( item );
					break;	
				}
			}
		}

		return inputHandled;
	}
	
	/**
	 * Selects the item, or activates it if the clicked item is the currently selected one.
	 * 
	 * @param item
	 */
	private void toggleItemSelection( MenuItem item ) 
	{
		if ( m_currentlySelectedItem == item )
		{
			// the user clicked the selected item again - activate it then
			m_currentlySelectedItem.activateItem();
		}
		else
		{
			// select a different item
			
			if ( m_currentlySelectedItem != null )
			{
				m_currentlySelectedItem.setSelected( false );
				m_currentlySelectedItem = null;
			}
			
			if ( item != null )
			{
				m_currentlySelectedItem = item;
				m_currentlySelectedItem.setSelected( true );
			}
		}
	}

}
