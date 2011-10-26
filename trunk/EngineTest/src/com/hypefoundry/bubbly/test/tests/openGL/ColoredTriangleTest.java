package com.hypefoundry.bubbly.test.tests.openGL;


import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.renderer2D.Geometry;
import com.hypefoundry.engine.util.FPSCounter;


public class ColoredTriangleTest extends GLGame 
{

	@Override
	public Screen getStartScreen() 
	{
		return new ColorTriangleScreen( this );
	}
}

// ----------------------------------------------------------------------------

class ColorTriangleScreen extends Screen 
{

	final int 			VERTEX_SIZE = (2 + 4) * 4;
	GLGraphics 			m_glGraphics;
	
	float[]				m_vertices;
	short[] 			m_indices;
	Geometry			m_geometry;
	
	FPSCounter			m_fpsCounter = new FPSCounter();
	
	
	public ColorTriangleScreen( Game game ) 
	{
		super( game );
		
		m_glGraphics = ((GLGame) game).getGraphics();
		
		m_vertices = new float[] {	
				// obj 1
				0.0f, 0.0f, 1, 0, 0, 1,
				319.0f, 0.0f, 0, 1, 0, 1,
				319.0f, 479.0f, 0, 0, 1, 1,
				0.0f, 479.0f, 1, 0, 1, 1,
				// obj 2
				240.0f, 120.0f, 0, 1, 0, 1,
				240.0f, 360.0f, 0, 0, 1, 1,
				80.0f, 360.0f, 1, 0, 1, 1,
				80.0f, 120.0f, 1, 0, 0, 1,};
		
		final int MAX_SPRITES = 512;
		m_indices = new short[ MAX_SPRITES * 6 ];
		int len = m_indices.length;
		short j = 0;
		for ( int i = 0; i < len; i += 6, j += 4 )
		{
			m_indices[i + 0] = (short)(j + 0);
			m_indices[i + 1] = (short)(j + 1);
			m_indices[i + 2] = (short)(j + 2);
			m_indices[i + 3] = (short)(j + 2);
			m_indices[i + 4] = (short)(j + 3);
			m_indices[i + 5] = (short)(j + 0);
		}
		
		m_geometry = new Geometry( m_glGraphics, MAX_SPRITES * 4, MAX_SPRITES * 6, true, false );
		m_geometry.setVertices( m_vertices, 0, m_vertices.length );
		m_geometry.setIndices( m_indices, 0, m_indices.length );
	}
	
	@Override
	public void present( float deltaTime ) 
	{
		GL10 gl = m_glGraphics.getGL();
		
		gl.glViewport( 0, 0, m_glGraphics.getWidth(), m_glGraphics.getHeight() );
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( 0, 320, 0, 480, 1, -1 );
		
		// INFO : alpha tests are very slow - they decrease the framerate by half for just 4 indexed triangles
		
		//gl.glEnable( GL10.GL_BLEND );
		//gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		
		m_geometry.bind();
		m_geometry.draw( GL10.GL_TRIANGLES, 0, 12 );
		m_geometry.unbind();
		
		m_fpsCounter.logFrame();
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
