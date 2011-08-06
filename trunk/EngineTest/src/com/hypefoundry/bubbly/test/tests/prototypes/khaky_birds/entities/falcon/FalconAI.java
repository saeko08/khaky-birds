/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird.Bird;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.Vector3;

/**
 * @author azagor
 *
 */
public class FalconAI extends EntityController 
{

	private World 				m_world;
	private Falcon				m_falcon;
	private float 				m_speed         = 55.f;
	private float 				m_speedChaser   = 40.f;
	private Bird                m_bird          = null;
	
	private Vector3 			m_direction = new Vector3();
	
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param falcon			controlled falcon
	 */
	public FalconAI( World world, Entity falcon )
	{
		super( falcon );
		m_world = world;
		m_falcon = (Falcon)falcon;
		m_bird = (Bird) m_world.findEntity(Bird.class);
		
		
	}

	/* (non-Javadoc)
	 * @see com.hypefoundry.engine.game.Updatable#update(float)
	 */
	@Override
	public void update(float deltaTime) 
	{
		// start moving the falcon
		
		if (m_falcon.m_isChasing == true)
		{
			if ( m_bird != null )
			{
				m_direction.set( m_bird.getPosition() ); // direction = birdPos 
				m_direction.sub( m_falcon.getPosition() ); // direction -= falconPos
				m_direction.normalize2D();
				
				float speedForThisFrame = m_speedChaser * deltaTime;
				m_direction.scale( speedForThisFrame );
				
				m_falcon.translate( m_direction );
			}
		}
		
		else if (m_falcon.m_flyingFromLeft == true)
		{
			float translation = m_speed * deltaTime;
			m_falcon.translate( +translation, 0, 0  );
			
			// once it goes out of the view scope, destroy it
			float borderPos = m_falcon.getWorldBounds().m_minX;
			
			
			if ( borderPos >= 330.0f)
			{
				// ok - we can remove the falcon - it went out of the visibility scope
				m_world.removeEntity( m_falcon );
			}
		}
		else
		{
			float translation = m_speed * deltaTime;
			m_falcon.translate( -translation, 0, 0  );
			
			// once it goes out of the view scope, destroy it
			float borderPos = m_falcon.getWorldBounds().m_maxX;
			
			if ( borderPos <= -10.f )
			{
				// ok - we can remove the falcon - it went out of the visibility scope
				m_world.removeEntity( m_falcon );
				
			}
		}
				

	}

}
