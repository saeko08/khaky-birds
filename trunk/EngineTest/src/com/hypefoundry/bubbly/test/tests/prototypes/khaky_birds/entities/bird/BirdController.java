/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import java.util.List;

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
	
	private Vector3			m_translation;
	private final int		m_inputSensitivityThreshold = 5;
	
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
		
		m_translation = new Vector3( 0, 0, 0 );
	}

	@Override
	public void update( float deltaTime ) 
	{	
		List< TouchEvent > inputEvents = m_input.getTouchEvents();
		TouchEvent lastEvent = inputEvents.get( inputEvents.size() - 1 );
	
		// decide where to move
		if ( lastEvent.x > m_inputSensitivityThreshold )
		{
			m_bird.jumpToLeftCable();
		}
		else if ( lastEvent.x < m_inputSensitivityThreshold )
		{
			m_bird.jumpToRightCable();
		}
		
		if ( lastEvent.y > m_inputSensitivityThreshold )
		{
			m_bird.moveDownTheCable();
		}
		else if ( lastEvent.y < m_inputSensitivityThreshold )
		{
			m_bird.moveUpTheCable();
		}
	}

}
