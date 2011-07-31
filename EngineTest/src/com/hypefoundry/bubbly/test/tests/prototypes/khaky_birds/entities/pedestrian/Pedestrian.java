package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crap;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;


/**
 * A pedestrian populating the game world.
 * 
 * It's a walking target for the bird, and shitting on him
 * will earn us some points. 
 * 
 * @author paksas
 *
 */
public class Pedestrian extends Entity 
{
	Vector3 			m_direction;
	public boolean      hitWithShit = false;
	public boolean      isMoving    = true;

	
	
	/**
	 * Constructor.
	 *
	 * @param spawnAreaWidth
	 * @param spawnAreaHeight
	 */
	public Pedestrian( float spawnAreaWidth, float spawnAreaHeight )
	{
		// initialize random position
		float x, y;
		x = (float) Math.random() * spawnAreaWidth;
		y = (float) Math.random() * spawnAreaHeight;
		setPosition( x, y, 80 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		
		// set initial movement direction
		m_direction = new Vector3( (float)Math.random(), (float)Math.random(), 0);
		m_direction.normalize();
	}
	
	@Override
	public void onCollision( Entity colider ) 
	{
		if ( Crap.class.isInstance(colider))
		{
			hitWithShit = true;
			stopMoving();
		}
	}

	/**
	 *Makes pedestrian stops for a while
	 * 
	 *
	 */
	public void stopMoving() 
	{
		 isMoving = false;
	}
	
	/**
	 *pedestrian starts moving
	 * 
	 *
	 */
	public void startMoving() 
	{
		isMoving = true;
	}

}
