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
			float maxLinearSpeed = 1.0f;
			float maxRotationSpeed = 180.0f;
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
			sb.update();
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
		sb.update();
		
		DynamicObject movable = entity.query( DynamicObject.class );
		assertTrue( movable.m_velocity.distSq( 1.0f, 0.0f, 0.0f ) < 1e-3 );
	}
}
