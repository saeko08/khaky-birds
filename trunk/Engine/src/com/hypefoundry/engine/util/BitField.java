/**
 * 
 */
package com.hypefoundry.engine.util;

/**
 * A bit field.
 * 
 * @author Paksas
 *
 */
public final class BitField 
{
	public static final int MAX_ELEMENTS = 512;
	public static final int CHUNK_CAPACITY = 32;
	public static final int CHUNKS_COUNT = 16;
	
	public int[] 		m_bits = new int[CHUNKS_COUNT];
	
	/**
	 * Sets the specified bit to the specified value
	 * 
	 * @param idx			bit index
	 * @param enable		flag
	 */
	public void set( short idx, boolean enable )
	{
		byte chunkIdx = (byte) ( idx >> 5 );
		byte fieldIdx = (byte) ( idx - ( chunkIdx << 5 ) );
		
		if ( enable )
		{
			m_bits[chunkIdx] |= 1 << fieldIdx;
		}
		else
		{
			m_bits[chunkIdx] &= ~(1 << fieldIdx);
		}
	}
	
	/**
	 * Returns the value of the specified bit
	 * 
	 * @param idx			bit index
	 * @return
	 */
	public boolean isSet( short idx )
	{
		byte chunkIdx = (byte) ( idx >> 5 );
		byte fieldIdx = (byte) ( idx - ( chunkIdx << 5 ) );
		
		int key = 1 << fieldIdx;
		return ( m_bits[chunkIdx]& key ) == key;
	}
	
	/**
	 * A bitwise OR operation.
	 * 
	 * @param rhs		other bit field
	 */
	public void or( BitField rhs )
	{
		for ( byte i = 0; i < CHUNKS_COUNT; ++i )
		{
			m_bits[i] |= rhs.m_bits[i];
		}
	}
	
	/**
	 * A bitwise ANd operation.
	 * 
	 * @param rhs		other bit field
	 */
	public void and( BitField rhs )
	{
		for ( byte i = 0; i < CHUNKS_COUNT; ++i )
		{
			m_bits[i] &= rhs.m_bits[i];
		}
	}
	
	/**
	 * Fills the entire bit field with ones.
	 */
	public void ones()
	{
		for ( byte i = 0; i < CHUNKS_COUNT; ++i )
		{
			m_bits[i] = 1;
		}
	}
	
	/**
	 * Fills the entire bit field with zeroes.
	 */
	public void zeroes()
	{
		for ( byte i = 0; i < CHUNKS_COUNT; ++i )
		{
			m_bits[i] = 0;
		}
	}
}
