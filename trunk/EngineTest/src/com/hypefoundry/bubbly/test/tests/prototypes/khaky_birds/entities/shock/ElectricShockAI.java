/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock;

import com.hypefoundry.engine.controllers.EntityController;
import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.World;


/**
 * Electric shock entity controller.
 * 
 * @author paksas
 *
 */
public class ElectricShockAI extends EntityController 
{
	private World 				m_world;
	private ElectricShock		m_shock;
	private final float			m_moveSpeed = 30.0f;
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param entity
	 */
	public ElectricShockAI( World world, Entity entity ) 
	{
		super(entity);
		
		m_world = world;
		m_shock = (ElectricShock)entity;
	}

	@Override
	public void update( float deltaTime ) 
	{
		// start moving the shock up the screen
		float translation = m_moveSpeed * deltaTime;
		m_shock.translate( 0, -translation, 0  );
		
		// once it goes out of the view scope, destroy it
		float bottomPos = m_shock.getWorldBounds().m_maxY;
		if ( bottomPos <= 0.0f )
		{
			// ok - we can remove the shock - it went out of the visibility scope
			m_world.removeEntity( m_shock );
		}
	}

}
