package com.hypefoundry.engine.impl.openGL;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.hypefoundry.engine.core.FileIO;

/**
 * A texture resource.
 * 
 * @author paksas
 */
public class Texture 
{
	private float			m_width;
	private float			m_height;
	
	private GLGraphics 		m_graphics;
	private FileIO 			m_fileIO;
	private String 			m_fileName;
	private int 			m_textureId;
	private int 			m_minFilter;
	private int 			m_magFilter;
	
	/**
	 * Constructor.
	 * 
	 * @param glGame
	 * @param fileName			texture file path
	 */
	public Texture( GLGame glGame, String fileName ) 
	{
		m_graphics = glGame.getGLGraphics();
		m_fileIO = glGame.getFileIO();
		m_fileName = fileName;
		m_width = 0;
		m_height = 0;
		load();
	}
	
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
	
	/**
	 * Loads the texture into memory.
	 */
	private void load() 
	{
		GL10 gl = m_graphics.getGL();
		
		int[] textureIds = new int[1];
		gl.glGenTextures( 1, textureIds, 0 );
		
		m_textureId = textureIds[0];
		InputStream in = null;
		try 
		{
			// load the bitmap and bind it to the texture
			in = m_fileIO.readAsset( m_fileName );
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
			throw new RuntimeException( "Couldn't load texture '" + m_fileName + "'", e );
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
	
	/**
	 * Reload the texture after the GL context's been lost.
	 */
	public void reload() 
	{
		load();
		bind();
		setFilters( m_minFilter, m_magFilter );
		m_graphics.getGL().glBindTexture( GL10.GL_TEXTURE_2D, 0 );
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
		
		GL10 gl = m_graphics.getGL();
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter );
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter );
	}
	
	/**
	 * Bind the texture to the GL context for rendering
	 */
	public void bind() 
	{
		GL10 gl = m_graphics.getGL();
		gl.glBindTexture( GL10.GL_TEXTURE_2D, m_textureId );
	}
	
	/**
	 * Release the texture resource.
	 */
	public void dispose() 
	{
		GL10 gl = m_graphics.getGL();
		gl.glBindTexture( GL10.GL_TEXTURE_2D, m_textureId );
		int[] textureIds = { m_textureId };
		gl.glDeleteTextures( 1, textureIds, 0 );	
	}
}
