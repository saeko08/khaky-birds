package com.hypefoundry.engine.util;

import java.util.Comparator;

/**
 * 
 */

/**
 * A set of algorithms operating on arrays.
 * 
 * @author Paksas
 *
 */
public final class Arrays 
{
	/**
	 * A quicksort operation.
	 * 
	 * @param array
	 * @param size
	 * @param comparator
	 */
	public static < T > void quickSort( T[] array, int size, Comparator< T > comparator )
	{
		if ( size == 0 )
		{
			return;
		}
		
		recursiveQuickSort( array, comparator, 0, size - 1 );
	}
	
	/**
	 * Single layer of recursion of the quick sort algorithm.
	 *  
	 * @param array
	 * @param comparator
	 * @param startIdx
	 * @param endIdx
	 */
	private static < T > void recursiveQuickSort( T[] array, Comparator< T > comparator, int startIdx, int endIdx )
	{
		// pick a division element
		int divIdx = ( endIdx + startIdx ) / 2;
		T divElem = array[ divIdx ];
		
		// swap the elements on both sides of a division elem
		// so that we have only smaller elems to the left, 
		// and only larger elems to the right:
		//
		// SSSSSSS DIV LLLLLLLLL
		T tmp = null;
		int i = startIdx;
		int j = endIdx;
		do
		{
			// find first larger elem to the left of the division elem
			while( comparator.compare( array[i], divElem ) < 0 )
			{
				++i;
			}
			
			// find first smaller elem to the right of the division elem
			while( comparator.compare( array[j], divElem ) > 0 )
			{
				--j;
			}
			
			// swap them
			if ( i <= j )
			{
				tmp = array[j];
				array[j] = array[i];
				array[i] = tmp;
				++i;
				--j;
			}
		} while( i <= j );
		
		// go deeper...
		if ( startIdx < j )
		{
			// ...to the right
			recursiveQuickSort( array, comparator, startIdx, j );
		}
		if ( i < endIdx )
		{
			// ...to the left
			recursiveQuickSort( array, comparator, i, endIdx );
		}
	}

	/**
	 * Returns the textual representation of a string.
	 * 
	 * @param array
	 * @return
	 */
	public static < T > String toString( T[] array ) 
	{
		StringBuilder str = new StringBuilder();
		str.append( "[" );
		
		for( int i = 0; i < array.length; ++i )
		{
			str.append( array[i] != null ? array[i].toString() : "null" );
			if ( i < array.length - 1 )
			{
				str.append( ", " );
			}
		}
		str.append( "]" );
		
		return str.toString();
	}
}
