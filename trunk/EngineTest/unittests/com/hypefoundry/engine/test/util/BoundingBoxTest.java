package com.hypefoundry.engine.test.util;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.BoundingSphere;


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
}
