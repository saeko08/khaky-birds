package com.hypefoundry.engine.test.util;

import java.util.Comparator;

import com.hypefoundry.engine.util.Arrays;
import android.test.AndroidTestCase;


public class ArraysTests extends AndroidTestCase 
{
	Comparator< Integer >		m_intComparator = new Comparator< Integer >() {
		@Override
		public int compare( Integer arg0, Integer arg1 ) 
		{
			return arg0 - arg1;
		}	
	};
	
	public void testQuickSort()
	{
		Integer[] arr = { 5, 8, 2, 1, 9, 0 };
		Arrays.quickSort( arr, arr.length, m_intComparator );
		assertEquals( "[0, 1, 2, 5, 8, 9]", Arrays.toString( arr ) );
		
		arr = new Integer[]{ 0 };
		Arrays.quickSort( arr, arr.length, m_intComparator );
		assertEquals( "[0]", Arrays.toString( arr ) );
		
		arr = new Integer[]{ 0, 0 };
		Arrays.quickSort( arr, arr.length, m_intComparator );
		assertEquals( "[0, 0]", Arrays.toString( arr ) );
		
		arr = new Integer[]{ 3, 2, 1, 0 };
		Arrays.quickSort( arr, arr.length, m_intComparator );
		assertEquals( "[0, 1, 2, 3]", Arrays.toString( arr ) );
	}
}
