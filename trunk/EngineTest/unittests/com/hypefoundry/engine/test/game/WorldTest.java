package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.EntityOperation;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.util.BoundingBox;

import android.test.AndroidTestCase;


public class WorldTest extends AndroidTestCase 
{
	// ------------------------------------------------------------------------
	
	private class EntityMock extends Entity
	{
		int				m_collisions;
		
		EntityMock()
		{
			super();
			setBoundingBox( new BoundingBox( -5, -5, -5, 5, 5, 5 ) );
			m_collisions = 0;
		}
		
		@Override
		public void onCollision( Entity colider )
		{
			m_collisions++;
		}
		
		public void reset()
		{
			m_collisions = 0;
		}
	}
	
	// ------------------------------------------------------------------------
	
	private class CollisionReset implements EntityOperation
	{
		public void visit( Entity e )
		{
			if ( e instanceof EntityMock )
			{
				((EntityMock)e).m_collisions = 0;
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	public void testCollisions()
	{
		World world = new World();
		EntityMock e1 = new EntityMock();
		EntityMock e2 = new EntityMock();
		CollisionReset op = new CollisionReset();
		
		world.addEntity( e1 );
		world.addEntity( e2 );
		
		e1.setPosition( 0, 0, 0 );
		e2.setPosition( 15, 15, 0 );
		world.update( 0 );
		assertEquals( 0, e1.m_collisions );
		assertEquals( 0, e2.m_collisions );
		world.executeOperation( op );
		
		e1.setPosition( 2, 2, 0 );
		e2.setPosition( 4, 4, 0 );
		world.update( 0 );
		assertEquals( 1, e1.m_collisions );
		assertEquals( 1, e2.m_collisions );
	}
}

