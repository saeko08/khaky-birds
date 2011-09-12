package com.hypefoundry.engine.test.game;


import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.locomotion.SteeringBehaviors;
import com.hypefoundry.engine.world.Entity;

import android.test.AndroidTestCase;



public class SteeringBehaviorTest extends AndroidTestCase 
{
	class ImmobileEntityMock extends Entity
	{
		public ImmobileEntityMock()
		{
		}
	}
	
	// ------------------------------------------------------------------------
	
	class MobileMock extends Entity
	{
		public MobileMock()
		{
			this( 1.0f, 180.0f );
		}
		
		public MobileMock( float maxLinearSpeed, float maxRotationSpeed )
		{
			defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		}
	}
	
	// ------------------------------------------------------------------------
	
	public void testNoMovementCapabilities()
	{
		ImmobileEntityMock entity = new ImmobileEntityMock();		
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		
		// at this point the entity doesn't have the movement capabilities, so it won't budge
		sb.seek( new Vector3( 10.0f, 0.0f, 0.0f ) );
		try
		{
			sb.update( 0 );
		}
		catch( Exception ex )
		{
			assertTrue( false );
		}
		
	}
	
	public void testMovementExecution()
	{
		MobileMock entity = new MobileMock();		
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		
		// at this point the entity doesn't have the movement capabilities, so it won't budge
		sb.seek( new Vector3( 10.0f, 0.0f, 0.0f ) );
		sb.update( 0 );
		
		DynamicObject movable = entity.query( DynamicObject.class );
		assertTrue( movable.m_velocity.distSq( 1.0f, 0.0f, 0.0f ) < 1e-3 );
	}
	
	public void testLookAt()
	{
		MobileMock entity = new MobileMock( 1.0f, 720.0f );		
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		DynamicObject movable = entity.query( DynamicObject.class );
		
		//  look back
		sb.lookAt( new Vector3( -1.0f, 0.0f, 0.0f ) );
		
		// nothing happens without a simulation
		assertTrue( entity.getFacing() == 0.0f );
		
		// so simulate the dynamics then - in 0.1s the body should rotate 72 degrees given its rotation speed
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getFacing() == 72.0f );
		
		// and so it goes
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getFacing() == 144.0f );
		
		// however there's no risk of overshooting
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getFacing() == 180.0f );
	}
	
	public void testLookAtOtherSide()
	{
		MobileMock entity = new MobileMock( 1.0f, 720.0f );		
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		DynamicObject movable = entity.query( DynamicObject.class );
		
		//  look back
		sb.lookAt( new Vector3( 1, 0, 0 ).rotateZ( 359 ) );
		
		// nothing happens without a simulation
		assertTrue( entity.getFacing() == 0.0f );
		
		// so simulate the dynamics then - in 0.1s the body should rotate 72 degrees given its rotation speed
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( Math.abs( entity.getFacing() - 359.0f ) < 1e-3 );
		
		// however there's no risk of overshooting
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( Math.abs( entity.getFacing() - 359.0f ) < 1e-3 );
	}
	
	public void testLookAtInverse()
	{
		MobileMock entity = new MobileMock( 1.0f, 720.0f );
		entity.setFacing( 359.0f );
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		DynamicObject movable = entity.query( DynamicObject.class );
		
		//  look back
		sb.lookAt( new Vector3( 1, 0, 0 ) );
		
		// nothing happens without a simulation
		assertTrue( entity.getFacing() == 359.0f );
		
		// so simulate the dynamics then - in 0.1s the body should rotate 72 degrees given its rotation speed
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( Math.abs( entity.getFacing() ) < 1e-3 );
		
		// however there's no risk of overshooting
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( Math.abs( entity.getFacing() ) < 1e-3 );
	}
	
	public void testArrive()
	{
		MobileMock entity = new MobileMock( 10.0f, 0.0f );		
		SteeringBehaviors sb = new SteeringBehaviors( entity );
		DynamicObject movable = entity.query( DynamicObject.class );
		
		//  look back
		sb.arrive( new Vector3( 10.0f, 0.0f, 0.0f ), 2.0f );
		
		// so simulate the dynamics then - in 0.1s the body should move by 1m
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getPosition().distSq( 1.0f, 0.0f, 0.0f ) < 1e-3 );
		
		// we're almost there
		sb.update( 0.7f );
		movable.simulate( 0.7f, entity );
		assertTrue( entity.getPosition().distSq( 8.0f, 0.0f, 0.0f ) < 1e-3 );
		
		// ... aaaaand start breaking
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getPosition().distSq( 9.0f, 0.0f, 0.0f ) < 1e-3 );
		
		// ... keep breaking
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getPosition().distSq( 9.5f, 0.0f, 0.0f ) < 1e-3 );
		
		// ... keep breaking
		sb.update( 0.1f );
		movable.simulate( 0.1f, entity );
		assertTrue( entity.getPosition().distSq( 9.75f, 0.0f, 0.0f ) < 1e-3 );
	}
}
