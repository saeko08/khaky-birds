/**
 * 
 */
package com.hypefoundry.engine.renderer2D.particleSystem.particles;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.meshes.ColoredMesh;
import com.hypefoundry.engine.renderer2D.particleSystem.Particle;

/**
 * @author Paksas
 *
 */
public class CircleParticle extends Particle 
{
	private GL10			m_gl;
	private ColoredMesh		m_circleMesh;
	private RenderState		m_renderState;
	private Color			m_color;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param renderState
	 * @param color
	 * @param radius
	 * @param segmentsCount
	 */
	CircleParticle( GLGraphics graphics, RenderState renderState, Color	color, float radius, int segmentsCount )
	{
		m_gl = graphics.getGL();
		m_renderState = renderState;
		m_color = color;
		
		int verticesOnCircumferenceCount = segmentsCount;
		int indicesCount = verticesOnCircumferenceCount * 3;
		m_circleMesh = new ColoredMesh( graphics, verticesOnCircumferenceCount + 1, indicesCount );
		
		// create the circle - assign it all the same colors
		{
			// the first vertex
			m_circleMesh.m_vertices[0].set( 0, 0, 0 );
			m_circleMesh.m_vertexColor[0] = color;
			
			float dAngle = (float)( Math.PI * 2.0f ) / verticesOnCircumferenceCount;
			float angle = 0.0f;
			for ( int i = 0; i < verticesOnCircumferenceCount; ++i, angle += dAngle )
			{
				m_circleMesh.m_vertices[i + 1].set( (float)Math.cos( angle ) * radius, (float)Math.sin( angle ) * radius, 0.0f );
				m_circleMesh.m_vertexColor[i + 1] = color;
			}
			
			// define the indices
			int index = 0;
			for ( short i = 0; i < verticesOnCircumferenceCount; ++i )
			{
				short firstVtxIdx = (short)( i + 1 );
				short nextVtxIdx = (short)( i + 2 );
				if( nextVtxIdx >= verticesOnCircumferenceCount + 1 )
				{
					nextVtxIdx = 1;
				}
				
				m_circleMesh.m_indices[index++] = 0;
				m_circleMesh.m_indices[index++] = firstVtxIdx;
				m_circleMesh.m_indices[index++] = nextVtxIdx;
			}
			
		}
	}
	
	@Override
	protected void draw( float x, float y, SpriteBatcher batcher, float deltaTime )
	{
		// make sure the color gets modified
		m_circleMesh.lock();
		m_circleMesh.unlock();
		
		// scale and draw the circle		
		m_gl.glPushMatrix();
		m_gl.glTranslatef( m_position.m_x + x, m_position.m_y + y, 0.0f);
		m_gl.glScalef( m_scale, m_scale, m_scale );
		
		batcher.drawMesh( m_circleMesh, m_renderState );
		
		m_gl.glPopMatrix();
	}
}
