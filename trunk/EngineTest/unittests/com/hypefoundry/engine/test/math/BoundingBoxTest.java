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
	    BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    
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
	    BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 50, 150, 150 ), null ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, -50, 50, 50 ), null ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, -50, 50, 50 ), null ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, 50, 50, 150 ), null ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, 0, 50, 50 ), null ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 0, 150, 50), null ) );
	    
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, -50, -1, -1 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 101, 101, 150, 150 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, 0, -1, 100 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 0, -50, 100, -1 ), null ) );
	}
	
	public void testBoxSphereOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    BoundingSphere bs = new BoundingSphere( 0, 0, 50 );
	    
	    assertEquals( true, bb.doesOverlap( bs, null ) );
	    
	    bs.m_center.set( 100, 0, 0 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 100, 0 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 0, 100 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( -49, 0, 0 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, -49, 0 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 0, -49 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    
	    bs.m_center.set( 200, 0, 0 ); assertEquals( false, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 200, 0 ); assertEquals( false, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 0, 200 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( -200, 0, 0 ); assertEquals( false, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, -200, 0 ); assertEquals( false, bb.doesOverlap( bs, null ) );
	    bs.m_center.set( 0, 0, -200 ); assertEquals( true, bb.doesOverlap( bs, null ) );
	    
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, -50, -1, -1 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 101, 101, 150, 150 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, 0, -1, 100 ), null ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 0, -50, 100, -1 ), null ) );
	}
	
	public void testSphereRayOverlap()
	{
		BoundingSphere bs = new BoundingSphere( 0, 0, 10 );
		
		Vector3 intersectPos = new Vector3();
		
		Ray ray = new Ray( -11, 0, 2, 0 ); 
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
		BoundingBox bb = new BoundingBox( 0, 0, 10, 10 );
		
		Ray ray = new Ray( -1, 0, 2, 0 ); 
		assertTrue( bb.doesOverlap( ray, null ) );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap( ray, null ) );
		
		ray.m_origin.set( 1, -1, 0 );
		
		ray.setDirection( 0.5f, 0, 0 ); 
		assertFalse( bb.doesOverlap( ray, null ) );
		
		ray.setDirection( 0, 2, 0 ); 
		assertTrue( bb.doesOverlap( ray, null ) );
	}
	
	public void testRayOverlapRay()
	{
		Ray ray1 = new Ray( 0, 0, 1, 0 );
		Ray ray2 = new Ray( 0, 0, 1, 0 );
		Vector3 intersectPos = new Vector3();
		
		assertFalse( ray1.doesOverlap( ray2, intersectPos ) );
		
		ray2.m_origin.set( 2, 0, 0 );
		assertFalse( ray1.doesOverlap( ray2, intersectPos ) );
		
		ray2.m_origin.set( 0.5f, 0, 0 );
		assertFalse( ray1.doesOverlap( ray2, intersectPos ) );
		
		ray2.m_origin.set( 0, 1, 0 );
		assertFalse( ray1.doesOverlap( ray2, intersectPos ) );
		
		ray2.m_origin.set( 0.5f, 0.3f, 0 );
		ray2.setDirection( 0, -1, 0 );
		assertTrue( ray1.doesOverlap( ray2, intersectPos ) );
		assertTrue( intersectPos.dist( 0.5f, 0, 0 ) < 1e-3 );
		
		ray2.m_origin.set( 0.5f, 0.3f, 1 );
		assertFalse( ray1.doesOverlap( ray2, intersectPos ) );
	}
	
	public void testRayOverlapPoint()
	{
		Ray ray1 = new Ray( 0, 0, 1, 0 );
		
		assertTrue( ray1.doesOverlap( new Vector3( 0, 0, 0 ), null ) );
		assertTrue( ray1.doesOverlap( new Vector3( 0.5f, 0, 0 ), null ) );
		assertTrue( ray1.doesOverlap( new Vector3( 1, 0, 0 ), null ) );
		
		assertFalse( ray1.doesOverlap( new Vector3( 1.1f, 0, 0 ), null ) );
		assertFalse( ray1.doesOverlap( new Vector3( -0.1f, 0, 0 ), null ) );
		assertFalse( ray1.doesOverlap( new Vector3( 0.5f, 1, 0 ), null ) );
		assertFalse( ray1.doesOverlap( new Vector3( 0.5f, 1, 1 ), null ) );
		
		ray1.m_origin.set( 10, 20, 30 );
		ray1.setDirection( 1, 2, 3 );
		assertFalse( ray1.doesOverlap( new Vector3( 0, 0, 0 ), null ) );
		assertTrue( ray1.doesOverlap( new Vector3( 10, 20, 30 ), null ) );
		assertTrue( ray1.doesOverlap( new Vector3( 10.5f, 21, 31.5f ), null ) );
		assertTrue( ray1.doesOverlap( new Vector3( 11, 22, 33 ), null ) );
	}
	
	public void testNearestPoint()
	{
		BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    Vector3 nearestPoint = new Vector3();
	    
	    bb.getNearestPoint( new Vector3( -1.0f, 0.0f, 0.0f ), nearestPoint );
	    assertTrue( nearestPoint.m_x == 0.0f );
	    assertTrue( nearestPoint.m_y == 0.0f );
	    
	    bb.getNearestPoint( new Vector3(  1.0f, 10.0f, 0.0f ), nearestPoint );
	    assertTrue( nearestPoint.m_x == 1.0f );
	    assertTrue( nearestPoint.m_y == 10.0f );
	}
}

