package com.hypefoundry.engine.test.util;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.hypefoundry.engine.util.BoundingBox;


public class BoundingBoxTest extends AndroidTestCase
{
	public void testPointOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    
	    assertEquals( true, bb.doesOverlap( 1, 1 ) );
	    assertEquals( true, bb.doesOverlap( 0, 0 ) );
	    assertEquals( true, bb.doesOverlap( 100, 0 ) );
	    assertEquals( true, bb.doesOverlap( 0, 100 ) );
	    assertEquals( true, bb.doesOverlap( 100, 100 ) );
	    
	    assertEquals( false, bb.doesOverlap( -1, -1 ) );
	    assertEquals( false, bb.doesOverlap( -1, 0 ) );
	    assertEquals( false, bb.doesOverlap( 101, 0 ) );
	    assertEquals( false, bb.doesOverlap( 0, 101 ) );
	    assertEquals( false, bb.doesOverlap( 101, 101 ) );
	}
	
	public void testBoxOverlap() 
	{  
	    BoundingBox bb = new BoundingBox( 0, 0, 100, 100 );
	    
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 50, 150, 150 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, -50, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, -50, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 0, 50, 50, 150 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( -50, 0, 50, 50 ) ) );
	    assertEquals( true, bb.doesOverlap( new BoundingBox( 50, 0, 150, 50 ) ) );
	    
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, -50, -1, -1 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 101, 101, 150, 150 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( -50, 0, -1, 100 ) ) );
	    assertEquals( false, bb.doesOverlap( new BoundingBox( 0, -50, 100, -1 ) ) );
	} 
}
