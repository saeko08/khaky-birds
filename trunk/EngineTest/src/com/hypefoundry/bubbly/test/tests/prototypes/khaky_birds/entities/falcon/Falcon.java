/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird.State;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shocked;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventException;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.CollisionEvent;

/**
 * @author azagor
 *
 */
public class Falcon extends Entity
{
	public boolean   m_flyingFromLeft 			 = true;
	public boolean   m_isChasing 			     = false;
	public World 	m_world    				     = null;
	private Random m_randStartSideX              = new Random();
	private Random m_randStartPosY               = new Random();

	
	public enum State
	{
		Chasing,
		Hunting,
	};
	
	public State				m_state;
	
	public Falcon()
	{
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -1f, 0.2f, 0.2f, 1f ) );	// TODO: config
		
		// define events the entity responds to
		registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
				
		m_state = State.Hunting; 
		
		// add movement capabilities
		final float maxLinearSpeed = 1.0f;
		final float maxRotationSpeed = 720.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
	
	}
	
	/**
	 * Setting starting position of falcon
	 *
	 */
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		
		float worldWidth = m_world.getWidth();
		float worldHeight = m_world.getHeight();
		
		int startSideX = m_randStartSideX.nextInt(2);
		int startPosY = m_randStartPosY.nextInt((int) worldHeight);
		
		
		if (startSideX == 0 )
		{
			m_flyingFromLeft = true;
			
			setPosition( -2, startPosY, -1);
			this.rotate(0);
		}
		else
		{
			m_flyingFromLeft = false;
			
			setPosition( worldWidth + 2, startPosY, -1 );
			this.rotate(-180);
		}
	}
	

}
