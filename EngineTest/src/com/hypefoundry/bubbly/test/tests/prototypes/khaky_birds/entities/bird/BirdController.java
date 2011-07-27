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
import com.hypefoundry.engine.math.Vector3;


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
	private float           m_touchTimer = 0.f;
	private Vector3			m_dragStart = new Vector3( 0, 0, 0 );
	private final int		m_inputSensitivityThreshold = 25;
	
	//Probably it would be better to put it in the input controller:
	private boolean 		m_touchingScreen 				= false;
	
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
		
		//counting touching screen time
		if(m_touchingScreen == true)
		{
			m_touchTimer+=deltaTime;
			Log.d( "touchTimer", m_touchTimer + ", ");
			
			if (m_touchTimer > 0.4 && m_bird.crosshairInitialized == false)
			{
				initializeCrosshair();
			}
		}
		
		for ( TouchEvent lastEvent : inputEvents )
		{	
			if ( lastEvent.type == TouchEvent.TOUCH_DOWN)
			{
				m_touchingScreen = true;
				m_dragStart.m_x = lastEvent.x;
				m_dragStart.m_y = lastEvent.y;
				
				//here I had a problem. This updates only once after screen is touched
				//m_touchTimer+=deltaTime;
				//Log.d( "touchTimer", m_touchTimer + ", ");
	
			}
			if ( lastEvent.type == TouchEvent.TOUCH_UP)
			{
				float  dx, dy; 
				m_touchingScreen = false;
				m_touchTimer = 0.f;
				
				dx = lastEvent.x - m_dragStart.m_x;
				dy = lastEvent.y - m_dragStart.m_y;
				
				
				moveBird(dx, dy);
				
				if (m_bird.crosshairInitialized == true)
				{
					initializeShitting();
				}
				
				Log.d( "posChange", dx + ", " + dy );
			}
		}
	}

	private void initializeShitting() 
	{
		
		m_bird.makeShit();
		
	}

	private void initializeCrosshair() 
	{
	
		m_bird.crosshairOn();

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
