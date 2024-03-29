package com.hypefoundry.engine.test.game;

import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EntityOperation;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.PhysicalBody;
import com.hypefoundry.engine.physics.PhysicalBodyFactory;
import com.hypefoundry.engine.physics.PhysicsView;
import com.hypefoundry.engine.physics.events.CollisionEvent;


import android.test.AndroidTestCase;



public class PhysicsTests extends AndroidTestCase 
{
	// ------------------------------------------------------------------------
	
	private class EntityMock extends Entity implements EntityEventListener
	{
		int				m_collisions;
			
		EntityMock()
		{
			super();
			
			defineAspect( new DynamicObject( 1.0f, 180.0f ) );	
			setBoundingBox( new BoundingBox( -0.2f, -0.2f, 0.2f, 0.2f ) );
			m_collisions = 0;
		}
			
		@Override
		public void onEvent( EntityEvent event )
		{
			if ( event instanceof CollisionEvent )
			{
				m_collisions++;
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	private class PhysicalBodyMock extends PhysicalBody
	{		
		public PhysicalBodyMock( Entity entity ) 
		{
			super( entity, true );
			EntityMock mock = (EntityMock)entity;
			
			mock.attachEventListener( mock );
		}
		
		@Override
		public void respondToCollision( PhysicalBody collider, Vector3 collisionPoint )
		{
			// nothing to do here
		}
	}
	
	// ------------------------------------------------------------------------
	
	private class BouncyPhysicalBodyMock extends PhysicalBody
	{
		private Vector3		m_alteredVelocity = new Vector3();
		
		
		public BouncyPhysicalBodyMock( Entity entity ) 
		{
			super( entity, new BoundingBox( 0.5f, 0.5f ), true );
			EntityMock mock = (EntityMock)entity;
				
			mock.attachEventListener( mock );
		}	
		
		@Override
		public void respondToCollision( PhysicalBody collider, Vector3 collisionPoint )
		{
			DynamicObject dynObj = m_entity.query( DynamicObject.class );
			if ( dynObj.m_velocity.magSq2D() > 0 )
			{
				m_alteredVelocity.set( collisionPoint ).sub( m_entity.getPosition() );
				dynObj.m_velocity.set( m_alteredVelocity );
			}
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
		world.setSize( 4, 4 );
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
		physics.update( 0 );
		assertEquals( 0, e1.m_collisions );
		assertEquals( 0, e2.m_collisions );
		world.executeOperation( op );
			
		e1.setPosition( 3.5f, 3.5f, 0 );
		e2.setPosition( 3.4f, 3.4f, 0 );
		world.update( 0 );
		physics.update( 0 );
		// not yet - the events will be processed at the beginning of the next frame
		assertEquals( 0, e1.m_collisions );
		assertEquals( 0, e2.m_collisions );
		
		world.update( 0 );
		assertEquals( 1, e1.m_collisions );
		assertEquals( 1, e2.m_collisions );
	}
	
	public void testMovement()
	{
		World world = new World();
		world.setSize( 20, 20 );
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
		
		Vector3 pos = null;
		float dist = 0;
		for ( int i = 1; i < 10; ++i )
		{
			e1.query( DynamicObject.class ).m_velocity.set( 1, 0, 0 );
			e2.query( DynamicObject.class ).m_velocity.set( 0, 1, 0 );
			
			world.update( 1 );
			physics.update( 1 );
			
			pos = e1.getPosition();
			dist = pos.dist( new Vector3( i, 10, 0 ) );
			assertTrue( 0.1f > dist );
			
			pos = e2.getPosition();
			dist = pos.dist( new Vector3( 10, i, 0 ) );
			assertTrue( 0.1f > dist );
		}
	}
	
	public void testBouncingOff()
	{
		World world = new World();
		world.setSize( 4, 4 );
		EntityMock e1 = new EntityMock();
		EntityMock e2 = new EntityMock();
			
		world.addEntity( e1 );
		world.addEntity( e2 );
		world.update( 1 );
		
		PhysicsView physics = new PhysicsView( 2 );
		physics.register( EntityMock.class, new PhysicalBodyFactory() { @Override public PhysicalBody instantiate(Entity parentEntity) { return new BouncyPhysicalBodyMock( parentEntity ); } } );
		world.attachView( physics );
			
		// setup one moving and one static object
		e1.setPosition( 1, 1, 0 );
		e2.setPosition( 3, 3, 0 );
		e1.query( DynamicObject.class ).m_velocity.set( 1.5f, 1.5f, 0 );
		
		physics.update( 1 );
		world.update( 1 );
		assertEquals( 1, e1.m_collisions );
		assertEquals( 1, e2.m_collisions );
	
		// verify that the objects stopped
		assertTrue( e2.getPosition().dist( 3, 3, 0 ) < 1e-3 );
		assertTrue( e1.getPosition().dist( 1.707f, 1.707f, 0 ) < 1e-3 ); // not too acurate, but it's ok for now
	}
}
