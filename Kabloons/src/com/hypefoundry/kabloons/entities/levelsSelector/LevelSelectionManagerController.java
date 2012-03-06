/**
 * 
 */
package com.hypefoundry.kabloons.entities.levelsSelector;

import java.util.*;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.world.WorldView;


/**
 * @author Paksas
 */
public class LevelSelectionManagerController extends EntityController implements WorldView, InputHandler
{
	private LevelSelectionManager		m_levelSelectionManager;
	private World						m_world;
	private Camera2D					m_camera;

	private FogOfWar					m_fogOfWar					= null;
	private List< LevelItem >			m_managedLevels 			= new ArrayList< LevelItem >();
	private int 						m_largestLevelIdx 			= 0;
	private LevelItem					m_selectedLevel 			= null;	
	private Vector3						m_touchPos 					= new Vector3();

	
	final float							LEVEL_ITEM_SEARCH_RADIUS 	= 0.5f;
	
	/**
	 * Constructor.
	 * 
	 * @param levelSelectionManagerEntity
	 * @param world
	 * @param screen
	 * @param camera
	 */
	public LevelSelectionManagerController( Entity levelSelectionManagerEntity, World world, Screen screen, Camera2D camera ) 
	{
		super( levelSelectionManagerEntity );
		
		m_levelSelectionManager = (LevelSelectionManager)levelSelectionManagerEntity;
		m_world = world;
		m_camera = camera;
		
		// register self as an input handler
		screen.registerInputHandler( this );
		
		// attach self to world to poll the existing level pins
		world.attachView( this );
	}
	
	/**
	 * Toggles level selection, or activates the level if it's already selected.
	 * 
	 * @param level
	 */
	private void toggleLevelSelection( LevelItem level )
	{
		// only the cleared levels can be selected
		if ( m_levelSelectionManager.canBeSelected( level ) == false )
		{
			return;
		}
		
		if ( level.m_state == LevelItem.State.Selected )
		{
			level.activate();
			return;
		}
		
		if ( m_selectedLevel != null )
		{
			m_selectedLevel.setState( LevelItem.State.Deselected );
		}
		
		m_selectedLevel = level;
		m_selectedLevel.setState( LevelItem.State.Selected );
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
		if ( entity instanceof LevelItem)
		{
			LevelItem item = (LevelItem)entity;
			registerLevelItem( item );
		}
		else if ( entity instanceof FogOfWar )
		{
			FogOfWar effect = (FogOfWar)entity;
			registerFogOfWar( effect );
		}
		
	}
	
	/**
	 * Registers a new level item.
	 * 
	 * @param item
	 */
	private void registerLevelItem( LevelItem item )
	{
		m_managedLevels.add( item );
		boolean canLevelBeSelected = m_levelSelectionManager.canBeSelected( item );
		
		// set the levels state
		int availableLevelsCount = m_levelSelectionManager.m_availableLevels.length;
		for ( int j = 0; j < availableLevelsCount; ++j )
		{
			if ( m_levelSelectionManager.m_availableLevels[j] == item.m_levelIdx )
			{
				item.setState( LevelItem.State.Deselected );
				
				// memorize the level with the largest number - we'll set it as the selected one by default
				if ( item.m_levelIdx > m_largestLevelIdx && canLevelBeSelected )
				{
					m_largestLevelIdx = item.m_levelIdx;
					toggleLevelSelection( item );
				}
			}
		}
		
		// if the fog of war effect is already registered, set the clear sky area with it, providing
		// the level can be selected of course
		if ( m_fogOfWar != null && canLevelBeSelected )
		{
			m_fogOfWar.addClearSkyArea( item.m_clearSkyArea );
		}
	}
	
	/**
	 * Registers the fog of war effect.
	 * 
	 * @param item
	 */
	private void registerFogOfWar( FogOfWar effect )
	{
		if ( m_fogOfWar != null || effect == null )
		{
			// fog of war is already registered or this isn't a valid fog of war effect at all
			return;
		}
		
		m_fogOfWar = effect;
		
		// go through the registered items and register the clear sky areas of the selectable ones
		int count = m_managedLevels.size();
		for ( int i = 0; i < count; ++i )
		{
			LevelItem item = m_managedLevels.get(i);
			boolean canLevelBeSelected = m_levelSelectionManager.canBeSelected( item );
			if ( canLevelBeSelected )
			{
				m_fogOfWar.addClearSkyArea( item.m_clearSkyArea );
			}
		}
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
				
				LevelItem item = (LevelItem)m_world.findNearestEntity( LevelItem.class, LEVEL_ITEM_SEARCH_RADIUS, m_touchPos );
				if ( item != null )
				{
					// found one
					toggleLevelSelection( item );
					break;	
				}
			}
		}

		return inputHandled;
	}

	@Override
	public void onAttached( World world ) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetached( World world ) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onEntityRemoved( Entity entity ) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update( float deltaTime ) 
	{
		// TODO Auto-generated method stub

	}
}
