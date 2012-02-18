package com.hypefoundry.engine.core;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.hypefoundry.engine.core.GLGraphics;

/**
 * A texture resource.
 * 
 * @author paksas
 */
public class Texture extends Resource
{
	private float			m_width 			= 0;
	private float			m_height 			= 0;
	
	private GLGraphics 		m_graphics			= null;
	private FileIO 			m_fileIO			= null;
	private int 			m_textureId;
	private int 			m_minFilter;
	private int 			m_magFilter;
		
	/**
	 * Returns width of the texture.
	 * @return
	 */
	public final float getWidth()
	{
		return m_width;
	}
	
	/**
	 * Returns height of the texture.
	 * @return
	 */
	public final float getHeight()
	{
		return m_height;
	}
	
	@Override
	public void load() 
	{
		m_graphics = m_game.getGraphics();
		m_fileIO = m_game.getFileIO();
		
		GL10 gl = m_graphics.getGL();
		
		int[] textureIds = new int[1];
		gl.glGenTextures( 1, textureIds, 0 );
		
		m_textureId = textureIds[0];
		InputStream in = null;
		try 
		{
			// load the bitmap and bind it to the texture
			in = m_fileIO.readAsset( m_assetPath );
			Bitmap bitmap = BitmapFactory.decodeStream( in );
			gl.glBindTexture( GL10.GL_TEXTURE_2D, m_textureId );
			GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bitmap, 0 );
			
			m_width = bitmap.getWidth();
			m_height = bitmap.getHeight();
			
			// set the filtering
			setFilters( GL10.GL_NEAREST, GL10.GL_NEAREST );
			
			// unbind the current texture from the GL context
			gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
		} 
		catch( IOException e ) 
		{
			throw new RuntimeException( "Couldn't load texture '" + m_assetPath + "'", e );
		} 
		finally 
		{
			// cleanup
			if( in != null )
			{
				try 
				{ 
					in.close(); 
				} 
				catch (IOException e) { }
			}
		}
	}

	
	@Override
	public void release() 
	{
		GL10 gl = m_graphics.getGL();
		gl.glBindTexture( GL10.GL_TEXTURE_2D, m_textureId );
		int[] textureIds = { m_textureId };
		gl.glDeleteTextures( 1, textureIds, 0 );
		gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
		
		m_graphics = null;
		m_fileIO = null;
	}
	
	/**
	 * Sets the texture filtering mode.
	 * 
	 * @param minFilter
	 * @param magFilter
	 */
	public void setFilters( int minFilter, int magFilter ) 
	{
		m_minFilter = minFilter;
		m_magFilter = magFilter;
		
		if ( m_graphics != null )
		{
			GL10 gl = m_graphics.getGL();
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter );
		}
	}
	
	/**
	 * Bind the texture to the GL context for rendering
	 */
	public void bind() 
	{
		if ( m_graphics != null )
		{
			GL10 gl = m_graphics.getGL();
			gl.glBindTexture( GL10.GL_TEXTURE_2D, m_textureId );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, m_minFilter );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, m_magFilter );
		}
	}
}
