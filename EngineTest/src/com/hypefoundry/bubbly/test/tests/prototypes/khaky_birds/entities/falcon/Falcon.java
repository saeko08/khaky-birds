/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
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
public class Falcon extends Entity implements EntityEventListener
{
	public boolean   m_flyingFromLeft 			 = true;
	public boolean   m_isChasing 			     = false;
	private World 	m_world    				     = null;
	private Random m_randStartSideX              = new Random();
	private Random m_randStartPosY               = new Random();
	private Random m_randChasingChance           = new Random();
	private Falcon m_falcon						 = null;
	private Bird m_bird                          = null;

	
	public Falcon()
	{
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		
		// define events the entity responds to
		registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
				
		// register events listeners
		attachEventListener( this );
		
		// add movement capabilities
		final float maxLinearSpeed = 1.0f;
		final float maxRotationSpeed = 180.0f;
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
		m_bird = (Bird) m_world.findEntity(Bird.class);
		
		int startSideX = m_randStartSideX.nextInt(2);
		int startPosY = m_randStartPosY.nextInt(481);
		int chasingChance = m_randChasingChance.nextInt(3);
		
		if (startSideX == 0 )
		{
			m_flyingFromLeft = true;
			
			setPosition( -10, startPosY, 1 );
			
			if (m_bird!=null)
			{
				if (chasingChance == 0 )
				{
					m_isChasing = true;
				}
			}
		}
		else
		{
			m_flyingFromLeft = false;
			
			setPosition( 330, startPosY, 1 );
			
			if (m_bird!=null)
			{
				if (chasingChance == 0 )
				{
					m_isChasing = true;
				}
			}
		}
	}
	
	@Override
	public void onRemovedFromWorld( World hostWorld ) 
	{
		m_falcon = new Falcon();
		
		m_world.addEntity( m_falcon );
	}

	// ------------------------------------------------------------------------
	// Environment interactions
	// ------------------------------------------------------------------------	
	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof Shocked && m_isChasing == true )
		{
			// when the falcon gets shocked, it dies
			m_world.removeEntity( this );
		}
		else if ( event instanceof CollisionEvent )
		{
			// if it collides with another entity, it attempts eating it
			Entity collider = ( (CollisionEvent)event ).m_collider;
			collider.sendEvent( Eaten.class );

		}
	}

}
