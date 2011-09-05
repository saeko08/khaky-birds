/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

/**
 * Color representation.
 * 
 * @author paksas
 *
 */
public final class Color 
{
	public static int Red 		= 0;
	public static int Green		= 1;
	public static int Blue 		= 2;
	public static int Alpha		= 3;
	
	public static Color RED		= new Color( 1, 0, 0, 1 );
	public static Color GREEN	= new Color( 0, 1, 0, 1 );
	public static Color BLUE	= new Color( 0, 0, 1, 1 );
	public static Color BLACK	= new Color( 0, 0, 0, 1 );
	public static Color WHITE	= new Color( 1, 1, 1, 1 );
	
	public float m_vals[] = { 0, 0, 0, 1 };
	
	/**
	 * Default constructor.
	 */
	public Color() 
	{
	}
	
	/**
	 * Constructor.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color( float r, float g, float b, float a )
	{
		m_vals[Red] = r;
		m_vals[Green] = g;
		m_vals[Blue] = b;
		m_vals[Alpha] = a;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param rhs
	 */
	public Color( Color rhs )
	{
		m_vals[Red] = rhs.m_vals[Red];
		m_vals[Green] = rhs.m_vals[Green];
		m_vals[Blue] = rhs.m_vals[Blue];
		m_vals[Alpha] = rhs.m_vals[Alpha];
	}
	
	/**
	 * Brightens up the color by the specified factor.
	 * 
	 * @param factor
	 * @return this color instance, allowing for commands chaining
	 */
	public Color brighter( float factor )
	{
		if ( factor < 0 )
		{
			factor = 0;
		} 
		else if ( factor > 1 )
		{
			factor = 1;
		}
		
		// make it brighter but don't change the alpha ( that's why we're only
		// changing the first 3 params )
		for ( byte i = 0; i < 3; ++i )
		{
			m_vals[i] += factor;
			if ( m_vals[i] > 1 )
			{
				m_vals[i] = 1;
			}
		}
		
		return this;
	}
}
