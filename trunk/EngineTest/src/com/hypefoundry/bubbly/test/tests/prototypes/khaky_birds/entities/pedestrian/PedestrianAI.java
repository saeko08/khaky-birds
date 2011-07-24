/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import android.util.Log;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.util.BoundingBox;
import com.hypefoundry.engine.util.Vector3;


/**
 * AI of a pedestrian.
 * 
 * @author paksas
 *
 */
public class PedestrianAI extends EntityController
{
	private World 				m_world;
	private Pedestrian			m_pedestrian;
	private float 				m_wait 			= 0.f;
	
	private Vector3 			m_velocity;
	private final float			m_speed 		= 25.0f;

	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param pedestrian			controlled pedestrian
	 */
	public PedestrianAI( World world, Entity pedestrian )
	{
		super( pedestrian );
		
		m_world = world;
		m_pedestrian = (Pedestrian)pedestrian;
		
		m_velocity = new Vector3( 0, 0, 0 );
	}
	
	@Override
	public void update( float deltaTime ) 
	{
		// TODO: create steering behaviors library
		
		//pedestrian stops moving for a while
		if (m_pedestrian.isMoving == false)
		{
			m_wait =+ deltaTime;
			
			Log.d( "wait", m_wait + ", ");
			
			if (m_wait > 0.2)
			{
				m_pedestrian.isMoving = true;
				m_wait = 0;
			}
			
		}
		else 
		{
			// wander around aimlessly
			m_velocity.scale( m_pedestrian.m_direction, m_speed * deltaTime );
			m_pedestrian.translate( m_velocity );
			
			// if the pedestrian got outside the screen, change its movement direction
			BoundingBox bb = m_pedestrian.getWorldBounds();
			if ( bb.m_minX < 0 || bb.m_maxX >= m_world.getWidth() )
			{
				m_pedestrian.m_direction.m_x = -m_pedestrian.m_direction.m_x; 
			}
			if ( bb.m_minY < 0 || bb.m_maxY >= m_world.getHeight() )
			{
				m_pedestrian.m_direction.m_y = -m_pedestrian.m_direction.m_y; 
			}
		}
	}

}
