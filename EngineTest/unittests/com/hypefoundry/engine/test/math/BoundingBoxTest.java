package com.hypefoundry.engine.test.math;

import android.test.AndroidTestCase;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;
import com.hypefoundry.engine.math.Ray;
import com.hypefoundry.engine.math.Vector3;


public class BoundingBoxTest extends AndroidTestCase
{
	public void testPointOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 0, 100, 100, 100 );
	    
	    assertEquals( true, bb.doesOverlap( 1, 1, 1 ) );
	    assertEquals( true, bb.doesOverlap( 0, 0, 0 ) );
	    assertEquals( true, bb.doesOverlap( 100, 0, 0 ) );
	    assertEquals( true, bb.doesOverlap( 0, 100, 0 ) );
	    assertEquals( true, bb.doesOverlap( 0, 100, 100 ) );
	    assertEquals( true, bb.doesOverlap( 100, 100, 100 ) );
	    
	    assertEquals( false, bb.doesOverlap( -1, -1, -1 ) );
	    assertEquals( false, bb.doesOverlap( -1, 0, 0 ) );
	    assertEquals( false, bb.doesOverlap( 101, 0, 0 ) );
	    assertEquals( false, bb.doesOverlap( 0, 101, 0 ) );
	    assertEquals( false, bb.doesOverlap( 0, 101, 101 ) );
	    assertEquals( false, bb.doesOverlap( 101, 101, 101 ) );
	}
	
	public void testBoxOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 0, 100, 100, 100 );
	    
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 50, 50, 150, 150, 150 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, -50, -50, 50, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, -50, 0, 50, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, 50, 0, 50, 150, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, 0, 0, 50, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 0, 0, 150, 50, 50 ) ) );
	    
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, -50, -50, -1, -1, -1 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 101, 101, 101, 150, 150, 150 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, 0, 0, -1, 100, 100 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 0, -50, 0, 100, -1, 100 ) ) );
	}
	
	public void testBoxSphereOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 0, 100, 100, 100 );
	    BoundingSphere bs = new BoundingSphere( 0, 0, 0, 50 );
	    
	    assertEquals( true, bb.doesOverlap( bs ) );
	    
	    bs.m_center.set( 100, 0, 0 ); assertEquals( true, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 100, 0 ); assertEquals( true, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 0, 100 ); assertEquals( true, bb.doesOverlap( bs ) );
	    bs.m_center.set( -49, 0, 0 ); assertEquals( true, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, -49, 0 ); assertEquals( true, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 0, -49 ); assertEquals( true, bb.doesOverlap( bs ) );
	    
	    bs.m_center.set( 200, 0, 0 ); assertEquals( false, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 200, 0 ); assertEquals( false, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 0, 200 ); assertEquals( false, bb.doesOverlap( bs ) );
	    bs.m_center.set( -200, 0, 0 ); assertEquals( false, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, -200, 0 ); assertEquals( false, bb.doesOverlap( bs ) );
	    bs.m_center.set( 0, 0, -200 ); assertEquals( false, bb.doesOverlap( bs ) );
	    
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, -50, -50, -1, -1, -1 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 101, 101, 101, 150, 150, 150 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, 0, 0, -1, 100, 100 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 0, -50, 0, 100, -1, 100 ) ) );
	}
	
	public void testSphereRayOverlap()
	{
		BoundingSphere bs = new BoundingSphere( 0, 0, 0, 10 );
		
		Vector3 intersectPos = new Vector3();
		
		Ray ray = new Ray( -11, 0, 0, 2, 0, 0 ); 
		assertTrue( bs.doesOverlap( ray, intersectPos ) );
		assertTrue( intersectPos.dist( -10, 0, 0 ) < 1e-3 );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bs.doesOverlap( ray, intersectPos ) );
		
		ray.setDirection( 0, 1, 0 ); 
		assertFalse( bs.doesOverlap( ray, intersectPos ) );
		
		ray.setDirection( -5, 0, 0 ); 
		assertFalse( bs.doesOverlap( ray, intersectPos ) );
		
		ray.setDirection( 10, 2, 0 ); 
		assertTrue( bs.doesOverlap( ray, intersectPos ) );
		assertTrue( intersectPos.dist( -9.9979f, 0.2f, 0 ) < 1e-3 );
		
		ray.m_origin.set( 0, -15, 0 );
		
		ray.setDirection( 10, 2, 0 );
		assertFalse( bs.doesOverlap( ray, intersectPos ) );
		
		ray.setDirection( 0, 4.9f, 0 ); 
		assertFalse( bs.doesOverlap( ray, intersectPos ) );
		
		ray.setDirection( 0, 5.0f, 0 ); 
		assertTrue( bs.doesOverlap( ray, intersectPos ) );
		assertTrue( intersectPos.dist( 0, -10, 0 ) < 1e-3 );
	}
	
	public void testBoxRayOverlap()
	{
		BoundingBox bb = new BoundingBox( 0, 0, 0, 10, 10, 10 );
		
		Ray ray = new Ray( -1, 0, 0, 2, 0, 0 ); 
		assertTrue( bb.doesOverlap( ray, null ) );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap( ray, null ) );
		
		ray.m_origin.set( 1, -1, 0 );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap( ray, null ) );
		
		ray.setDirection( 0, 2, 0 ); 
		assertTrue( bb.doesOverlap( ray, null ) );
	}
	
	public void testBoxRayOverlap2D()
	{
		BoundingBox bb = new BoundingBox( 0, 0, 0, 10, 10, 10 );
		
		Ray ray = new Ray( -1, 0, 0, 2, 0, 100 ); 
		assertTrue( bb.doesOverlap2D( ray, null ) );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap2D( ray, null ) );
		
		ray.m_origin.set( 1, -1, 100 );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap2D( ray, null ) );
		
		ray.setDirection( 0, 2, 0 ); 
		assertTrue( bb.doesOverlap2D( ray, null ) );
	}
}

