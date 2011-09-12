package com.hypefoundry.engine.util;

import java.util.Comparator;

import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleAffector;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleEmitter;

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
	
	/**
	 * Appends a vector to an array, resizing it.
	 * 
	 * @param array
	 * @return resized array
	 */
	public static Vector3[] append( Vector3[] array, Vector3 obj )
	{
		Vector3[] newArray = null;
		if ( array == null )
		{
			newArray = new Vector3[1];
			newArray[0] = obj;
		}
		else
		{
			newArray = new Vector3[ array.length + 1 ];
			for ( int i = 0; i < array.length; ++i )
			{
				newArray[i] = array[i];
			}
			newArray[array.length] = obj;
		}
		
		return newArray;
	}
	
	/**
	 * Appends a float to an array, resizing it.
	 * 
	 * @param array
	 * @return resized array
	 */
	public static float[] append( float[] array, float val )
	{
		float[] newArray = null;
		if ( array == null )
		{
			newArray = new float[1];
			newArray[0] = val;
		}
		else
		{
			newArray = new float[ array.length + 1 ];
			for ( int i = 0; i < array.length; ++i )
			{
				newArray[i] = array[i];
			}
			newArray[array.length] = val;
		}
		
		return newArray;
	}
	
	/**
	 * Appends an int to an array, resizing it.
	 * 
	 * @param array
	 * @return resized array
	 */
	public static int[] append( int[] array, int val )
	{
		int[] newArray = null;
		if ( array == null )
		{
			newArray = new int[1];
			newArray[0] = val;
		}
		else
		{
			newArray = new int[ array.length + 1 ];
			for ( int i = 0; i < array.length; ++i )
			{
				newArray[i] = array[i];
			}
			newArray[array.length] = val;
		}
		
		return newArray;
	}

	/**
	 * Appends a particle emitter to an array, resizing it.
	 * 
	 * @param array
	 * @return resized array
	 */
	public static ParticleEmitter[] append( ParticleEmitter[] array, ParticleEmitter obj ) 
	{
		ParticleEmitter[] newArray = null;
		if ( array == null )
		{
			newArray = new ParticleEmitter[1];
			newArray[0] = obj;
		}
		else
		{
			newArray = new ParticleEmitter[ array.length + 1 ];
			for ( int i = 0; i < array.length; ++i )
			{
				newArray[i] = array[i];
			}
			newArray[array.length] = obj;
		}
		
		return newArray;
	}
	
	/**
	 * Appends a particle emitter to an array, resizing it.
	 * 
	 * @param array
	 * @return resized array
	 */
	public static ParticleAffector[] append( ParticleAffector[] array, ParticleAffector obj ) 
	{
		ParticleAffector[] newArray = null;
		if ( array == null )
		{
			newArray = new ParticleAffector[1];
			newArray[0] = obj;
		}
		else
		{
			newArray = new ParticleAffector[ array.length + 1 ];
			for ( int i = 0; i < array.length; ++i )
			{
				newArray[i] = array[i];
			}
			newArray[array.length] = obj;
		}
		
		return newArray;
	}

}
