/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.renderer2D.AlphaFunc;
import com.hypefoundry.engine.renderer2D.AlphaOp;
import com.hypefoundry.engine.util.serialization.DataLoader;


/**
 * Render state.
 * @author Paksas
 *
 */
public class RenderState 
{	
	private AlphaOp			m_alphaOperation = AlphaOp.AO_None;
	private AlphaFunc		m_srcAlphaFunc = AlphaFunc.AF_One;
	private AlphaFunc		m_destAlphaFunc = AlphaFunc.AF_Zero;
	private float			m_lineWidth = 1.0f;
	public Texture			m_texture = null;
	
	/**
	 * Compares two render states.
	 * 
	 * @param rhs
	 * @return
	 */
	public boolean equals( RenderState rhs )
	{
		if ( m_texture != rhs.m_texture )
		{
			return false;
		}
		
		if ( m_alphaOperation.equals( rhs.m_alphaOperation ) )
		{
			return false;
		}
		
		if ( m_srcAlphaFunc.equals( rhs.m_srcAlphaFunc ) )
		{
			return false;
		}
		
		if ( m_destAlphaFunc.equals( rhs.m_destAlphaFunc ) )
		{
			return false;
		}
		
		if ( m_lineWidth != rhs.m_lineWidth )
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Copies the settings from another render state.
	 * 
	 * @param rhs
	 */
	public void set( RenderState rhs )
	{
		m_texture = rhs.m_texture;
		m_alphaOperation = rhs.m_alphaOperation;
		m_srcAlphaFunc = rhs.m_srcAlphaFunc;
		m_destAlphaFunc = rhs.m_destAlphaFunc;
		m_lineWidth = rhs.m_lineWidth;
	}
	
	/**
	 * Loads render state settings from a loader.
	 * 
	 * @param resMgr
	 * @param loader
	 */
	public void deserialize( ResourceManager resMgr, DataLoader loader )
	{
		// load texture
		String atlasName = loader.getStringValue( "atlasName" );
		if ( atlasName.length() > 0 )
		{
			m_texture = resMgr.getResource( Texture.class, atlasName );
		}
		
		// read alpha operation
		String alphaOp = loader.getStringValue( "alphaOp" );
		if ( alphaOp.equalsIgnoreCase( "none" ) )
		{
			m_alphaOperation = AlphaOp.AO_None;
		}
		else if ( alphaOp.equalsIgnoreCase( "test" ) )
		{
			m_alphaOperation = AlphaOp.AO_Test;
		}
		else if ( alphaOp.equalsIgnoreCase( "blend" ) )
		{
			m_alphaOperation = AlphaOp.AO_Blend;
		}
		
		// read alpha functions
		String srcAlphaFunc = loader.getStringValue( "srcAlphaFunc" );
		String destAlphaFunc = loader.getStringValue( "destAlphaFunc" );
		m_srcAlphaFunc = AlphaFunc.fromString( srcAlphaFunc );
		m_destAlphaFunc = AlphaFunc.fromString( destAlphaFunc );
		
		// read line width
		m_lineWidth = loader.getFloatValue( "lineWidth" );
	}
	
	/**
	 * Binds the render state to the device
	 * @param gl
	 */
	public void bind( GL10 gl )
	{
		// bind the new texture, if one's set
		if ( m_texture != null )
		{
			gl.glEnable( GL10.GL_TEXTURE_2D );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
			m_texture.bind();
		}
		else
		{
			gl.glDisable( GL10.GL_TEXTURE_2D );
		}
		
		// set the desired line width
		gl.glLineWidth( m_lineWidth );
		
		// set the desired alpha operation
		switch( m_alphaOperation )
		{
			case AO_None:
			{
				gl.glDisable( GL10.GL_ALPHA_TEST ); 
				gl.glDisable( GL10.GL_BLEND ); 
				break;
			}
			
			case AO_Test:
			{
				gl.glDisable( GL10.GL_BLEND );
				gl.glEnable( GL10.GL_ALPHA_TEST );
				gl.glAlphaFunc( GL10.GL_GREATER, 0.9f );
				break;
			}
			
			case AO_Blend:
			{
				gl.glDisable( GL10.GL_ALPHA_TEST );
				gl.glEnable( GL10.GL_BLEND );
				
				gl.glBlendFunc( m_srcAlphaFunc.m_glValue, m_destAlphaFunc.m_glValue );
				
				break;
			}
		}
	}
	
	/**
	 * Toggles texturing.
	 * 
	 * @param enable
	 * @return
	 */
	public RenderState disableTexturing()
	{
		if ( m_texture != null )
		{
			m_texture = null;
		}
		
		return this;
	}
	
	/**
	 * Sets a texture for the batch rendering
	 * 
	 * @param texture
	 */
	public RenderState setTexture( Texture texture ) 
	{
		if ( m_texture != texture )
		{
			m_texture = texture;
		}
		
		return this;
	}
	
	/**
	 * Sets the line width.
	 * 
	 * @param width
	 * @return
	 */
	public RenderState setLineWidth( float width )
	{
		if ( width != m_lineWidth )
		{
			m_lineWidth = width;
		}
		
		return this;
	}
	
	/**
	 * Makes the renderer perform alpha tests.
	 * 
	 * @return
	 */
	public RenderState enableAlphaTest()
	{
		if ( m_alphaOperation != AlphaOp.AO_Test )
		{
			m_alphaOperation = AlphaOp.AO_Test;
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
	public RenderState enableAlphaBlending( AlphaFunc srcFunc, AlphaFunc destFunc )
	{
		if ( m_alphaOperation != AlphaOp.AO_Blend || m_srcAlphaFunc != srcFunc || m_destAlphaFunc != destFunc )
		{
			m_alphaOperation = AlphaOp.AO_Blend;
			m_srcAlphaFunc = srcFunc;
			m_destAlphaFunc = destFunc;
		}
		
		return this;
	}
	
	/**
	 * Disables alpha operation.
	 * 
	 * @return
	 */
	public RenderState disableAlphaOp( )
	{
		if ( m_alphaOperation != AlphaOp.AO_None )
		{
			m_alphaOperation = AlphaOp.AO_None;
		}
		
		return this;
	}
}
