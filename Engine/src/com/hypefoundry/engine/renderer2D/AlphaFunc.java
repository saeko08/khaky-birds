/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

/**
 * Available alpha blending functions.
 * 
 * @author Paksas
 *
 */
public enum AlphaFunc 
{
	AF_Zero( GL10.GL_ZERO ),
	AF_One( GL10.GL_ONE ),
	AF_Src_Alpha( GL10.GL_SRC_ALPHA ),
	AF_Dest_Alpha( GL10.GL_DST_ALPHA ),
	AF_Src_Color( GL10.GL_SRC_COLOR ),
	AF_Dest_Color( GL10.GL_DST_COLOR ),
	AF_One_Minus_Src_Alpha( GL10.GL_ONE_MINUS_SRC_ALPHA ),
	AF_One_Minus_Dest_Alpha( GL10.GL_ONE_MINUS_DST_ALPHA ),
	AF_One_Minus_Src_Color( GL10.GL_ONE_MINUS_SRC_COLOR ),
	AF_One_Minus_Dest_Color( GL10.GL_ONE_MINUS_DST_COLOR );
	
	public final int 		m_glValue;
	
	/**
	 * Constructor.
	 * 
	 * @param glValue
	 */
	private AlphaFunc( int glValue )
	{
		m_glValue = glValue;
	}
	
	/**
	 * Returns an OpenGL blend function corresponding to the specified engine blend function.
	 *  
	 * @param func
	 * @return
	 */
	static AlphaFunc fromString( String funcStr )
	{
		if ( funcStr.equalsIgnoreCase( "zero" ) )
		{
			return AF_Zero;
		}
		else if ( funcStr.equalsIgnoreCase( "one" ) )
		{
			return AF_One;
		}
		else if ( funcStr.equalsIgnoreCase( "srcAlpha" ) )
		{
			return AF_Src_Alpha;
		}
		else if ( funcStr.equalsIgnoreCase( "destAlpha" ) )
		{
			return AF_Dest_Alpha;
		}
		else if ( funcStr.equalsIgnoreCase( "srcColor" ) )
		{
			return AF_Src_Color;
		}
		else if ( funcStr.equalsIgnoreCase( "destColor" ) )
		{
			return AF_Dest_Color;
		}
		else if ( funcStr.equalsIgnoreCase( "oneMinusSrcAlpha" ) )
		{
			return AF_One_Minus_Src_Alpha;
		}
		else if ( funcStr.equalsIgnoreCase( "oneMinusDestAlpha" ) )
		{
			return AF_One_Minus_Dest_Alpha;
		}
		else if ( funcStr.equalsIgnoreCase( "oneMinusSrcColor" ) )
		{
			return AF_One_Minus_Src_Color;
		}
		else if ( funcStr.equalsIgnoreCase( "oneMinusDestColor" ) )
		{
			return AF_One_Minus_Dest_Color;
		}
		else
		{
			return AF_Zero;
		}
	}
}
