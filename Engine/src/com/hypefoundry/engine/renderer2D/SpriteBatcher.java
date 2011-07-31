/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.impl.openGL.Geometry;
import com.hypefoundry.engine.math.Vector3;

/**
 * Main workhorse behind the sprites rendering.
 * 
 * @author Paksas
 *
 */
public class SpriteBatcher 
{
	final float[] 		m_verticesBuffer;
	int 				m_bufferIndex;
	final Geometry 		m_geometry;
	int 				m_numSprites;
	
	
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
	 * Begins the batch rendering with the specified texture.
	 * 
	 * @param texture
	 */
	public void beginBatch( Texture texture ) 
	{
		texture.bind();
		m_numSprites = 0;
		m_bufferIndex = 0;
	}
	
	/**
	 * Finishes the batch rendering.
	 */
	public void endBatch() 
	{
		m_geometry.setVertices( m_verticesBuffer, 0, m_bufferIndex );
		m_geometry.bind();
		m_geometry.draw( GL10.GL_TRIANGLES, 0, m_numSprites * 6 );
		m_geometry.unbind();
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
}
