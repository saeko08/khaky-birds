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
}
