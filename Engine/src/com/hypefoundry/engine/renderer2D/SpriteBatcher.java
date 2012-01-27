/**
 * 
 */
package com.hypefoundry.engine.renderer2D;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;


/**
 * Main workhorse behind the sprites rendering.
 * 
 * @author Paksas
 *
 */
public class SpriteBatcher 
{
	private enum DrawItem
	{
		Lines,
		Sprites
	};
			
	public GLGraphics			m_graphics;
	private GL10				m_gl;
	private final float[] 		m_verticesBuffer;
	private int 				m_bufferIndex;
	
	private final Geometry 		m_geometry;
	private final Geometry 		m_lines;
	
	private int 				m_numSprites;
	private int					m_numLines;
	
	private DrawItem			m_currentDrawItem = DrawItem.Lines;
	private RenderState			m_currRenderState = new RenderState();		
	
	private Vector3 			tmpSpriteCenter = new Vector3();
	
	
	/**
	 * Constructor.
	 * 
	 * CAUTION: at this point we're using the same vertex buffer to store
	 * both lines and the sprites data - we may need to separate them in the future
	 * if we need to differentiate the maximum number of sprites and lines. Right
	 * now we're using the same value for both.
	 * 
	 * @param graphics
	 * @param maxSprites
	 */
	public SpriteBatcher( GLGraphics graphics, int maxSprites ) 
	{	
		m_graphics = graphics;
		m_gl = graphics.getGL();
		
		m_verticesBuffer = new float[ maxSprites * 4 * 4 ];
		m_geometry = new Geometry( graphics, maxSprites * 4, maxSprites*6, false, true );
		m_lines = new Geometry( graphics, maxSprites * 2, 0, true, false );
		
		m_bufferIndex = 0;
		m_numSprites = 0;
		m_numLines = 0;
		
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
		// draw what's in the sprites buffer
		if ( m_numSprites > 0 )
		{
			assert( m_numLines == 0 ); // those are mutually exclusive
			
			m_geometry.setVertices( m_verticesBuffer, 0, m_bufferIndex );
			m_geometry.bind();
			m_geometry.draw( GL10.GL_TRIANGLES, 0, m_numSprites * 6 );
			m_geometry.unbind();
			
			m_numSprites = 0;
		}
		
		// draw what's in the lines buffer
		if ( m_numLines > 0 )
		{
			assert( m_numSprites == 0 ); // those are mutually exclusive
			
			m_lines.setVertices( m_verticesBuffer, 0, m_bufferIndex );
			m_lines.bind();
			m_lines.draw( GL10.GL_LINES, 0, m_numLines * 2 );
			m_lines.unbind();
			
			m_numLines = 0;
		}
		
		m_bufferIndex = 0;
	}
	
	/**
	 * Draws a spline.
	 * 
	 * @param spline
	 * @param color
	 * @param rs		render state
	 */
	public void drawSpline( Spline spline, Color color, RenderState rs )
	{		
		// check if the spline has any segments defined
		int count = spline.m_points.length - 1;
		if ( count <= 0 )
		{
			return;
		}
		
		// we'll be drawing lines now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Lines );
		
		// set the render state
		setRenderState( rs );
		
		// draw the spline
		for ( int i = 0; i < count; ++i )
		{
			// line start point
			Vector3 pt = spline.m_points[ i ];
			m_verticesBuffer[ m_bufferIndex++ ] = pt.m_x;
			m_verticesBuffer[ m_bufferIndex++ ] = pt.m_y;
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Red ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Green ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Blue ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Alpha ];
			
			// line end point
			pt = spline.m_points[ i + 1 ];
			m_verticesBuffer[ m_bufferIndex++ ] = pt.m_x;
			m_verticesBuffer[ m_bufferIndex++ ] = pt.m_y;
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Red ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Green ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Blue ];
			m_verticesBuffer[ m_bufferIndex++ ] = color.m_vals[ Color.Alpha ];
			
			++m_numLines;
		}
	}
	
	/**
	 * Draws a sprite with the texture defined for the batch.
	 * 
	 * @param pos			position of the sprite
	 * @param bb			sprite's bounding box
	 * @param region		texture region to draw the sprite with
	 */
	public void drawSprite( Vector3 pos, BoundingBox bb, TextureRegion region ) 
	{
		// we'll be drawing sprites now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Sprites );
		
		// set the render state
		setRenderState( region.m_renderState );
		
		float x1 = pos.m_x + bb.m_minX;
		float y1 = pos.m_y + bb.m_minY;
		float x2 = pos.m_x + bb.m_maxX;
		float y2 = pos.m_y + bb.m_maxY;
		
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
	 * @param pos			position of the sprite
	 * @param bb			sprite's bounding box
	 * @param angle			rotation angle in degrees
	 * @param region		texture region to draw the sprite with
	 */
	public void drawSprite( Vector3 pos, BoundingBox bb, float angle, TextureRegion region ) 
	{
		// we'll be drawing sprites now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Sprites );
		
		// set the render state
		setRenderState( region.m_renderState );
		
		// add the new sprite to the batcher
		float rad = angle * Vector3.TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		float x1 = bb.m_minX * cos - bb.m_minY * sin;
		float y1 = bb.m_minX * sin + bb.m_minY * cos;
		float x2 = bb.m_maxX * cos - bb.m_minY * sin;
		float y2 = bb.m_maxX * sin + bb.m_minY * cos;
		float x3 = bb.m_maxX * cos - bb.m_maxY * sin;
		float y3 = bb.m_maxX * sin + bb.m_maxY * cos;
		float x4 = bb.m_minX * cos - bb.m_maxY * sin;
		float y4 = bb.m_minX * sin + bb.m_maxY * cos;
		x1 += pos.m_x;
		y1 += pos.m_y;
		x2 += pos.m_x;
		y2 += pos.m_y;
		x3 += pos.m_x;
		y3 += pos.m_y;
		x4 += pos.m_x;
		y4 += pos.m_y;
		
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
		// we'll be drawing sprites now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Sprites );
		
		// set the render state
		setRenderState( region.m_renderState );
						
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
		// we'll be drawing sprites now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Sprites );
		
		// set the render state
		setRenderState( region.m_renderState );
		
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
	 * Draws an unaligned sprite ( one for which the x,y coordinates specify the top-left corner position ) with the texture defined for the batch.
	 * 
	 * CAUTION: it draws the sprite in an inverse orientation ( assuming that 0 is at the top of the viewport, and the viewport Y axis extends downwards ).
	 *          For this reason it should only be used for the HUD rendering.
	 * 
	 * @param x				sprite center X coordinate
	 * @param y				sprite center Y coordinate
	 * @param width			desired sprite width
	 * @param height		desired sprite height
	 * @param region		texture region to draw the sprite with
	 */
	public void drawUnalignedSprite( float x, float y, float width, float height, TextureRegion region ) 
	{
		// we'll be drawing sprites now, so flush the buffer if something else was drawn before
		switchTo( DrawItem.Sprites );
		
		// set the render state
		setRenderState( region.m_renderState );
						
		// add the new sprite to the batcher	
		float x1 = x;
		float y1 = y;
		float x2 = x + width;
		float y2 = y + height;
		
		m_verticesBuffer[ m_bufferIndex++ ] = x1;
		m_verticesBuffer[ m_bufferIndex++ ] = y2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x2;
		m_verticesBuffer[ m_bufferIndex++ ] = y2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v2;
		m_verticesBuffer[ m_bufferIndex++ ] = x2;
		m_verticesBuffer[ m_bufferIndex++ ] = y1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u2;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		m_verticesBuffer[ m_bufferIndex++ ] = x1;
		m_verticesBuffer[ m_bufferIndex++ ] = y1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_u1;
		m_verticesBuffer[ m_bufferIndex++ ] = region.m_v1;
		m_numSprites++;
	}
	
	/**
	 * Sets the render state
	 */
	private void setRenderState( RenderState rs )
	{
		if ( m_currRenderState.equals( rs ) == false )
		{
			flush();
			m_currRenderState.set( rs );
			m_currRenderState.bind( m_gl );
		}
	}
	
	/**
	 * Switches the drawing mode to a different item, flushing the contents
	 * of the vertex buffer if necessary.
	 * 
	 * @param item
	 */
	private void switchTo( DrawItem item )
	{
		if ( m_currentDrawItem != item )
		{
			flush();
			
			m_currentDrawItem = item;
		}
	}
}
