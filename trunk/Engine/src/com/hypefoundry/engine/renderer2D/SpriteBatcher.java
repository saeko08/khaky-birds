/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.math.Vector3;


/**
 * Main workhorse behind the sprites rendering.
 * 
 * @author Paksas
 *
 */
public class SpriteBatcher 
{
	private final float[] 		m_verticesBuffer;
	private int 				m_bufferIndex;
	private final Geometry 		m_geometry;
	private int 				m_numSprites;
	
	private Texture				m_currentTexture = null;
	
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param maxSprites
	 */
	public SpriteBatcher( GLGraphics graphics, int maxSprites ) 
	{
		m_verticesBuffer = new float[ maxSprites * 4 * 4 ];
		m_geometry = new Geometry( graphics, maxSprites * 4, maxSprites*6, false, true );
		
		m_bufferIndex = 0;
		m_numSprites = 0;
		
		// allocate the index buffer
		short[] indices = new short[ maxSprites * 6 ];
		int len = indices.length;
		short j = 0;
		for ( int i = 0; i < len; i += 6, j += 4 )
		{
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		m_geometry.setIndices( indices, 0, indices.length );
	}
	
	/**
	 * Flushes the batch.
	 */
	public void flush()
	{
		setTexture( null );
	}
	
	/**
	 * Draws a sprite with the texture defined for the batch.
	 * 
	 * @param x				sprite center X coordinate
	 * @param y				sprite center Y coordinate
	 * @param width			desired sprite width
	 * @param height		desired sprite height
	 * @param region		texture region to draw the sprite with
	 */
	public void drawSprite( float x, float y, float width, float height, TextureRegion region ) 
	{
		// set a texture first
		setTexture( region.m_texture );
				
		// add the new sprite to the batcher
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;
		
		m_verticesBuffer[ m_bufferIndex++ ] = x1;
		m_verticesBuffer[ m_bufferIndex++ ] = y1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x2;
		m_verticesBuffer[ m_bufferIndex++ ] = y1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x2;
		m_verticesBuffer[ m_bufferIndex++ ] = y2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		m_verticesBuffer[ m_bufferIndex++ ] = x1;
		m_verticesBuffer[ m_bufferIndex++ ] = y2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		m_numSprites++;
	}
	
	/**
	 * Draws a rotated sprite with the texture defined for the batch.
	 * 
	 * @param x				sprite center X coordinate
	 * @param y				sprite center Y coordinate
	 * @param width			desired sprite width
	 * @param height		desired sprite height
	 * @param angle			rotation angle in degrees
	 * @param region		texture region to draw the sprite with
	 */
	public void drawSprite( float x, float y, float width, float height, float angle, TextureRegion region ) 
	{
		// set a texture first
		setTexture( region.m_texture );
		
		// add the new sprite to the batcher
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float rad = angle * Vector3.TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		float x1 = -halfWidth * cos - (-halfHeight) * sin;
		float y1 = -halfWidth * sin + (-halfHeight) * cos;
		float x2 = halfWidth * cos - (-halfHeight) * sin;
		float y2 = halfWidth * sin + (-halfHeight) * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;
		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;
		
		m_verticesBuffer[ m_bufferIndex++ ] = x1;
		m_verticesBuffer[ m_bufferIndex++ ] = y1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x2;
		m_verticesBuffer[ m_bufferIndex++ ] = y2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x3;
		m_verticesBuffer[ m_bufferIndex++ ] = y3;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		m_verticesBuffer[ m_bufferIndex++ ] = x4;
		m_verticesBuffer[ m_bufferIndex++ ] = y4;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		
		m_numSprites++;
	}
	
	/**
	 * Sets a texture for the batch rendering
	 * 
	 * @param texture
	 */
	private void setTexture( Texture texture ) 
	{
		if ( m_currentTexture != texture )
		{
			// this is a new batch - draw the stuff that's in the buffer
			// and prepare it for the new batch
			
			// draw what's in the buffer
			m_geometry.setVertices( m_verticesBuffer, 0, m_bufferIndex );
			m_geometry.bind();
			m_geometry.draw( GL10.GL_TRIANGLES, 0, m_numSprites * 6 );
			m_geometry.unbind();
				
			// prepare the new texture
			m_currentTexture = texture;
			if ( m_currentTexture != null )
			{
				m_currentTexture.bind();
			}
			m_numSprites = 0;
			m_bufferIndex = 0;
		}
	}
}
