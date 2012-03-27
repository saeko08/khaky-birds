/**
 * 
 */
package com.hypefoundry.engine.renderer2D.fx;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.AlphaFunc;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.meshes.ColoredMesh;

/**
 * An effect that draws light shafts alpha blended with the background.
 * 
 * @author Paksas
 */
public class LightShaft 
{
	
	// shaft definition
	public RenderState		m_renderState;
	public Vector3			m_origin = new Vector3();
	public Vector3			m_end = new Vector3();
	public float			m_width;

	// runtime info
	private ColoredMesh		m_geometry;
	private float			m_maxAlpha;
	private Vector3			m_tmpVec 		= new Vector3();
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param color
	 */
	public LightShaft( GLGraphics graphics, Color color )
	{
		m_renderState = new RenderState();
		m_renderState.enableAlphaBlending( AlphaFunc.AF_Src_Alpha, AlphaFunc.AF_One_Minus_Src_Alpha );
		
		int verticesCount = 3;
		m_geometry = new ColoredMesh( graphics, verticesCount, verticesCount );
		
		// setup indices
		for ( short i = 0; i < verticesCount; ++i )
		{
			m_geometry.m_indices[i] = i;
		}
		
		// setup colors
		m_maxAlpha = color.m_vals[Color.Alpha];
		m_geometry.m_vertexColor[0].set( color );
		m_geometry.m_vertexColor[1].set( color );
		m_geometry.m_vertexColor[2].set( color );
		m_geometry.m_vertexColor[1].m_vals[Color.Alpha] = 0.0f;
		m_geometry.m_vertexColor[2].m_vals[Color.Alpha] = 0.0f;
	}
	
	/**
	 * Sets the shaft's apex alpha value.
	 * 
	 * @param alpha
	 */
	public void setAlpha( float alpha )
	{
		m_geometry.m_vertexColor[0].m_vals[Color.Alpha] = alpha * m_maxAlpha;
	}
	
	/**
	 * Draws the shaft on the screen.
	 * 
	 * @param batcher
	 */
	public void draw( SpriteBatcher batcher )
	{
		batcher.drawMesh( m_geometry, m_renderState );
	}
	
	/**
	 * Call this method if you changed any of the shaft parameters
	 * and want to see your change acutally take place.
	 */
	public void onShaftChanged()
	{
		m_geometry.lock();
		
		m_geometry.m_vertices[0].set( m_origin );
		
		m_tmpVec.set( m_end ).sub( m_origin ).normalize2D();
		m_geometry.m_vertices[1].set( m_tmpVec ).rotateZ( 90.0f ).scale( m_width );
		m_geometry.m_vertices[2].set( m_geometry.m_vertices[1] ).scale( -1.0f );
		
		m_geometry.m_vertices[1].add( m_end );
		m_geometry.m_vertices[2].add( m_end );
		
		m_geometry.unlock();
	}
}
