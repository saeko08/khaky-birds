package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.game.Entity;
import com.hypefoundry.engine.game.EntityOperation;
import com.hypefoundry.engine.game.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.physics.PhysicalBodyFactory;
import com.hypefoundry.engine.physics.PhysicsView;


import android.test.AndroidTestCase;



public class PhysicsTests extends AndroidTestCase 
{
	// ------------------------------------------------------------------------
	
	private class EntityMock extends Entity
	{
		int				m_collisions;
			
		EntityMock()
		{
			super();
			
			defineAspect( new DynamicObject() );
			
			setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.2f, 0.2f, 0.2f, 0.2f ) );
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
	
	private class PhysicalBodyMock extends PhysicalBody
	{

		public PhysicalBodyMock( Entity entity ) 
		{
			super(entity);
		}
		
		@Override
		public void respondToCollision( PhysicalBody collider )
		{
			// nothing to do here
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
		World world = new World( 4, 4 );
		EntityMock e1 = new EntityMock();
		EntityMock e2 = new EntityMock();
		CollisionReset op = new CollisionReset();
			
		world.addEntity( e1 );
		world.addEntity( e2 );
		world.update( 1 );
		
		PhysicsView physics = new PhysicsView( 2 );
		physics.register( EntityMock.class, new PhysicalBodyFactory() { @Override public PhysicalBody instantiate(Entity parentEntity) { return new PhysicalBodyMock( parentEntity ); } } );
		world.attachView( physics );
			
		e1.setPosition( 1, 1, 0 );
		e2.setPosition( 3, 3, 0 );
		world.update( 0 );
		physics.simulate( 0 );
		assertEquals( 0, e1.m_collisions );
		assertEquals( 0, e2.m_collisions );
		world.executeOperation( op );
			
		e1.setPosition( 3.5f, 3.5f, 0 );
		e2.setPosition( 3.4f, 3.4f, 0 );
		world.update( 0 );
		physics.simulate( 0 );
		assertEquals( 1, e1.m_collisions );
		assertEquals( 1, e2.m_collisions );
	}
	
	public void testMovement()
	{
		World world = new World( 20, 20 );
		EntityMock e1 = new EntityMock();
		EntityMock e2 = new EntityMock();
			
		world.addEntity( e1 );
		world.addEntity( e2 );
		world.update( 1 );
		
		PhysicsView physics = new PhysicsView( 2 );
		physics.register( EntityMock.class, new PhysicalBodyFactory() { @Override public PhysicalBody instantiate(Entity parentEntity) { return new PhysicalBodyMock( parentEntity ); } } );
		world.attachView( physics );

		e1.setPosition( 0, 10, 0 );
		e2.setPosition( 10, 0, 0 );
		e1.query( DynamicObject.class ).m_velocity.set( 1, 0, 0 );
		e2.query( DynamicObject.class ).m_velocity.set( 0, 1, 0 );
		
		for ( int i = 0; i < 10; ++i )
		{
			world.update( 1 );
			physics.simulate( 1 );
			assertTrue( 0.1f < e1.getPosition().dist( new Vector3( i, 10, 0 ) ) );
			assertTrue( 0.1f < e1.getPosition().dist( new Vector3( 10, i, 0 ) ) );
		}
	}
}
