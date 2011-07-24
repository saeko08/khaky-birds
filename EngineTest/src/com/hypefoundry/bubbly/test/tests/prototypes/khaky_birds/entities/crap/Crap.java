package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.CableProvider;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.ElectricShock;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.util.Vector3;

/**
 * Crap a bird makes.
 * 
 * @author azagor
 *
 */
public class Crap extends Entity 
{
	private World 	m_world    			 = null;
	public boolean pedestrianHit         = false;
	private Bird   m_bird 				 = null;
	
	/**
	 * Constructor.
	 */
	public Crap()
	{
		setPosition( 0, 0, 0 );
	}

	@Override
	public void onCollision( Entity colider ) 
	{
		if ( Pedestrian.class.isInstance(colider))
		{
			pedestrianHit = true;
			
		}
	}
	
	/**
	 * Setting starting position of crap
	 *
	 */
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		m_bird = (Bird) hostWorld.findEntity( Bird.class );
		
		if ( m_bird != null )
		{
			Vector3 pos = m_bird.getPosition();
			float x = pos.m_x;
			float y = pos.m_y;
			
			setPosition( x, y + 26, 1 );
		}
	}
	

}
