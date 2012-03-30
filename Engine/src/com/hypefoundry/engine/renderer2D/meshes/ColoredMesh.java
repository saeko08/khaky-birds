/**
 * 
 */
package com.hypefoundry.engine.renderer2D.meshes;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.Geometry;
import com.hypefoundry.engine.renderer2D.Mesh;


/**
 * A wrapper around the geometry class that provides us the means of
 * easily creating meshes composed of triangles that use only colors.
 * 
 * @author Paksas
 */
public class ColoredMesh implements Mesh
{
	public Vector3[]			m_vertices;
	public Color[]				m_vertexColor;
	public short[]				m_indices;
	
	private Geometry 			m_geometry;
	
	private float[]				m_flatVertexData;
	private final short			m_vertexStride = 6;
	
	private int					m_numElements;
	
	/**
	 * Constructor.
	 * 
	 * @param graphics
	 * @param verticesCount
	 * @param indicesCount
	 */
	public ColoredMesh( GLGraphics graphics, int verticesCount, int indicesCount )
	{
		m_geometry = new Geometry( graphics, verticesCount, indicesCount, true, false );
		
		m_vertices = new Vector3[ m_geometry.m_maxVertices ];
		m_vertexColor = new Color[ m_geometry.m_maxVertices ];
		m_flatVertexData = new float[ m_geometry.m_maxVertices * m_vertexStride ];
		for ( int i = 0; i < m_vertices.length; ++i )
		{
			m_vertices[i] = new Vector3();
			m_vertexColor[i] = new Color();
		}
		
		m_indices = new short[indicesCount];
		
		if ( indicesCount > 0 )
		{
			m_numElements = indicesCount;
		}
		else
		{
			m_numElements = verticesCount;
		}
	}
	
	/**
	 * Locks the geometry, making writable.
	 */
	public void lock()
	{
		// do nothing - right now it's more of an informative quality
	}
	
	/**
	 * Unlocks the geometry, making it read-only.
	 */
	public void unlock()
	{
		// flatten the vertex data
		int flatVertexIdx = 0;
		for ( int i = 0; i < m_vertices.length; ++i )
		{
			m_flatVertexData[flatVertexIdx] = m_vertices[i].m_x;					++flatVertexIdx;
			m_flatVertexData[flatVertexIdx] = m_vertices[i].m_y;					++flatVertexIdx;
			m_flatVertexData[flatVertexIdx] = m_vertexColor[i].m_vals[Color.Red];	++flatVertexIdx;
			m_flatVertexData[flatVertexIdx] = m_vertexColor[i].m_vals[Color.Green];	++flatVertexIdx;
			m_flatVertexData[flatVertexIdx] = m_vertexColor[i].m_vals[Color.Blue];	++flatVertexIdx;
			m_flatVertexData[flatVertexIdx] = m_vertexColor[i].m_vals[Color.Alpha];	++flatVertexIdx;
		}
		// and set the flat vertex data
		m_geometry.setVertices(m_flatVertexData, 0, m_flatVertexData.length);
		
		// set the indices
		m_geometry.setIndices( m_indices, 0, m_indices.length );
	}
	
	@Override
	public void draw()
	{
		m_geometry.bind();
		m_geometry.draw( GL10.GL_TRIANGLES, 0, m_numElements );
		m_geometry.unbind();	
	}
}
