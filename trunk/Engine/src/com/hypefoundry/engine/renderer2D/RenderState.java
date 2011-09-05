/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.Texture;


enum AlphaFunc
{
	AF_Zero,
	AF_One,
	AF_Src_Alpha,
	AF_Dest_Alpha,
	AF_Src_Color,
	AF_Dest_Color,
	AF_One_Minus_Src_Alpha,
	AF_One_Minus_Dest_Alpha,
	AF_One_Minus_Src_Color,
	AF_One_Minus_Dest_Color,
}

/**
 * Render state.
 * @author Paksas
 *
 */
class RenderState 
{
	private enum AlphaOp
	{
		AO_None,
		AO_Test,
		AO_Blend
	}
	
	private GL10			m_gl;
	private SpriteBatcher 	m_batcher;
	
	private boolean			m_dirty;
	

	private AlphaOp			m_alphaOperation;
	private AlphaFunc		m_srcAlphaFunc;
	private AlphaFunc		m_destAlphaFunc;
	private float			m_lineWidth;
	private Texture			m_currentTexture = null;
	
	/**
	 * Constructor.
	 * @param gl
	 */
	public RenderState( GL10 gl, SpriteBatcher batcher )
	{
		m_gl = gl;
		m_batcher = batcher;
	}
	
	/**
	 * Concludes state changes.
	 */
	void end()
	{
		if ( m_dirty )
		{
			m_batcher.flush();
			
			// bind the new texture, if one's set
			if ( m_currentTexture != null )
			{
				m_gl.glEnable( GL10.GL_TEXTURE_2D );
				m_currentTexture.bind();
			}
			else
			{
				m_gl.glDisable( GL10.GL_TEXTURE_2D );
			}
			
			// set the desired line width
			m_gl.glLineWidth( m_lineWidth );
			
			// set the desired alpha operation
			switch( m_alphaOperation )
			{
				case AO_None:
				{
					m_gl.glDisable( GL10.GL_ALPHA_TEST ); 
					m_gl.glDisable( GL10.GL_BLEND ); 
					break;
				}
				
				case AO_Test:
				{
					m_gl.glDisable( GL10.GL_BLEND );
					m_gl.glEnable( GL10.GL_ALPHA_TEST );
					m_gl.glAlphaFunc( GL10.GL_GREATER, 0.9f );
					break;
				}
				
				case AO_Blend:
				{
					m_gl.glDisable( GL10.GL_ALPHA_TEST );
					m_gl.glEnable( GL10.GL_BLEND );
					
					int glSrcFunc = translateBlendFunc( m_srcAlphaFunc );
					int glDestFunc = translateBlendFunc( m_destAlphaFunc );
					
					m_gl.glBlendFunc( glSrcFunc, glDestFunc );
					
					break;
				}
			}
			
			// reset the dirty flag
			m_dirty = false;
		}
	}
	
	/**
	 * Toggles texturing.
	 * 
	 * @param enable
	 * @return
	 */
	RenderState disableTexturing()
	{
		if ( m_currentTexture != null )
		{
			m_currentTexture = null;
			m_dirty = true;
		}
		
		return this;
	}
	
	/**
	 * Sets a texture for the batch rendering
	 * 
	 * @param texture
	 */
	RenderState setTexture( Texture texture ) 
	{
		if ( m_currentTexture != texture )
		{
			m_currentTexture = texture;
		}
		
		return this;
	}
	
	/**
	 * Sets the line width.
	 * 
	 * @param width
	 * @return
	 */
	RenderState setLineWidth( float width )
	{
		if ( width != m_lineWidth )
		{
			m_lineWidth = width;
			m_dirty = true;
		}
		
		return this;
	}
	
	/**
	 * Makes the renderer perform alpha tests.
	 * 
	 * @return
	 */
	RenderState enableAlphaTest()
	{
		if ( m_alphaOperation != AlphaOp.AO_Test )
		{
			m_alphaOperation = AlphaOp.AO_Test;
			m_dirty = true;
		}
		
		return this;
	}
	
	/**
	 * Enables alpha blending.
	 * 
	 * @param srcFunc
	 * @param destFunc
	 * @return
	 */
	RenderState enableAlphaBlending( AlphaFunc srcFunc, AlphaFunc destFunc )
	{
		if ( m_alphaOperation != AlphaOp.AO_Blend || m_srcAlphaFunc != srcFunc || m_destAlphaFunc != destFunc )
		{
			m_alphaOperation = AlphaOp.AO_Blend;
			m_srcAlphaFunc = srcFunc;
			m_destAlphaFunc = destFunc;
			m_dirty = true;
		}
		
		return this;
	}
	
	/**
	 * Returns an OpenGL blend function corresponding to the specified engine blend function.
	 *  
	 * @param func
	 * @return
	 */
	private int translateBlendFunc( AlphaFunc func )
	{
		switch( func )
		{
		case AF_Zero:					return GL10.GL_ZERO;
		case AF_One:					return GL10.GL_ONE;
		case AF_Src_Alpha: 				return GL10.GL_SRC_ALPHA;
		case AF_Dest_Alpha: 			return GL10.GL_DST_ALPHA;
		case AF_Src_Color: 				return GL10.GL_SRC_COLOR;
		case AF_Dest_Color: 			return GL10.GL_DST_COLOR;
		case AF_One_Minus_Src_Alpha: 	return GL10.GL_ONE_MINUS_SRC_ALPHA;
		case AF_One_Minus_Dest_Alpha:	return GL10.GL_ONE_MINUS_DST_ALPHA;
		case AF_One_Minus_Src_Color: 	return GL10.GL_ONE_MINUS_SRC_COLOR;
		case AF_One_Minus_Dest_Color:	return GL10.GL_ONE_MINUS_DST_COLOR;
		}
		
		return GL10.GL_ZERO;
	}
	
	/**
	 * Disables alpha operation.
	 * 
	 * @return
	 */
	RenderState disableAlphaOp( )
	{
		if ( m_alphaOperation != AlphaOp.AO_None )
		{
			m_alphaOperation = AlphaOp.AO_None;
			m_dirty = true;
		}
		
		return this;
	}
}
