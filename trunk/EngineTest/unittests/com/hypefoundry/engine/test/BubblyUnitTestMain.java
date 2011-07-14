package com.hypefoundry.engine.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit tests starter.
 * 
 * @author paksas
 *
 */
public class BubblyUnitTestMain extends TestSuite 
{
	public static Test suite() 
	{
        return new TestSuiteBuilder( BubblyUnitTestMain.class ).includeAllPackagesUnderHere().build();
    }
}
