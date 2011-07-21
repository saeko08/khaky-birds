/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import java.util.List;

import android.util.Log;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.util.Vector3;


/**
 * Bird entity controller.
 * 
 * @author paksas
 *
 */
public class BirdController extends EntityController 
{
	private Bird			m_bird;
	private Input			m_input;
	
	private Vector3			m_dragStart = new Vector3( 0, 0, 0 );
	private final int		m_inputSensitivityThreshold = 25;
	
	/**
	 * Constructor.
	 * 
	 * @param input			input manager
	 * @param entity
	 */
	public BirdController( Input input, Entity entity ) 
	{
		super( entity );
		
		m_input = input;
		m_bird = (Bird)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{	
		List< TouchEvent > inputEvents = m_input.getTouchEvents();
		for ( TouchEvent lastEvent : inputEvents )
		{	
			if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
			{
				m_dragStart.m_x = lastEvent.x;
				m_dragStart.m_y = lastEvent.y;
	
			}
			if ( lastEvent.type == TouchEvent.TOUCH_UP)
			{
				float  dx, dy; 
				
				dx = lastEvent.x - m_dragStart.m_x;
				dy = lastEvent.y - m_dragStart.m_y;
				
				moveBird(dx, dy);
				
				Log.d( "posChange", dx + ", " + dy );
			}
		}
	}

	/**
	 * Move the bird.
	 * 
	 * @param lastEvent
	 */
	private void moveBird( float dx, float dy ) 
	{	
		// decide where to move
		if ( dx > m_inputSensitivityThreshold )
		{
			m_bird.jumpRight();
			Log.d( "posChange", "jumpRight" );
		}
		else if ( dx < -m_inputSensitivityThreshold )
		{
			m_bird.jumpLeft();
			Log.d( "posChange", "jumpLeft" );
		}
		
		if ( dy > m_inputSensitivityThreshold )
		{
			m_bird.jumpDown();
			Log.d( "posChange", "jumpDown" );
		}
		else if ( dy < -m_inputSensitivityThreshold )
		{
			m_bird.jumpUp();
			Log.d( "posChange", "jumpUp" );
		}
	}

}
