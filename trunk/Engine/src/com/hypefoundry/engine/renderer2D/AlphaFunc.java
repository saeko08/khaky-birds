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
	
}
