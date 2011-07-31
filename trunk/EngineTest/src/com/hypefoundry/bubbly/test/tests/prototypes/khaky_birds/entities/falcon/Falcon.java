/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crap;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShock;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shockable;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;

/**
 * @author azagor
 *
 */
public class Falcon extends Entity implements Shockable
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
	}
	
	@Override
	public void onCollision( Entity collider ) 
	{
		if ( collider instanceof Prey )
		{
			((Prey)collider).getEaten();
		}
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
		
		m_world.addEntity(m_falcon);
	}

	// ------------------------------------------------------------------------
	// Environment interactions
	// ------------------------------------------------------------------------
	@Override
	public void getShocked() 
	{
		if ( m_isChasing == true )
		{
			m_world.removeEntity( this );
		}
	}

}
