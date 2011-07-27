/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import java.util.Random;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.math.Vector3;

/**
 * @author azagor
 *
 */
public class Falcon extends Entity 
{
	public boolean   m_flyingFromLeft 			 = true;
	private World 	m_world    				     = null;
	private Random m_randStartSideX              = new Random();
	private Random m_randStartPosY               = new Random();
	private Falcon m_falcon						 = null;

	@Override
	public void onCollision(Entity colider) 
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * Setting starting position of falcon
	 *
	 */
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		int startSideX = m_randStartSideX.nextInt(2);
		int startPosY = m_randStartPosY.nextInt(481);
		
		if (startSideX == 0 )
		{
			m_flyingFromLeft = true;
			
			setPosition( -10, startPosY, 1 );
		}
		else
		{
			m_flyingFromLeft = false;
			
			setPosition( 330, startPosY, 1 );
		}
	}
	
	@Override
	public void onRemovedFromWorld( World hostWorld ) 
	{
		m_falcon =new Falcon();
		
		m_world.addEntity(m_falcon);
	}

}
