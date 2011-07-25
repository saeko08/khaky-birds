/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;

/**
 * Controls crap drop
 * 
 * @author azagor
 *
 */
public class CrapAI extends EntityController 
{
	private final float			m_fallingSpeed = 80.0f;
	private Crap				m_crap;
	private World 				m_world;

	/**
	 * Constructor
	 * @param entity
	 */
	public CrapAI(World world,Entity entity) 
	{
		super(entity);
		
		m_crap = (Crap)entity;
		m_world = world;
	}

	@Override
	public void update(float deltaTime) 
	{
		// start moving the crap up the screen
		float translation = m_fallingSpeed * deltaTime;
		m_crap.translate( 0, 0, +translation  );
		
		// once it goes out of the view scope, destroy it
		float bottomPos = m_crap.getWorldBounds().m_maxZ;
		
		if ( bottomPos >= 100.0f )
		{
			// ok - we can remove the crap - it went out of the visibility scope
			m_world.removeEntity( m_crap );
		}
	}

}
