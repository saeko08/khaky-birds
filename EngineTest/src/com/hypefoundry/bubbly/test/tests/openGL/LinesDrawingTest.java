package com.hypefoundry.bubbly.test.tests.openGL;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.Spline;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.util.FPSCounter;



public class LinesDrawingTest extends GLGame 
{
	@Override
	public Screen getStartScreen() 
	{
		return new LinesDrawingScreen( this );
	}
}

//----------------------------------------------------------------------------

class LinesDrawingScreen extends Screen 
{

	final int	 		MAX_SPRITES = 100;
	GLGraphics 			m_glGraphics;
		
	SpriteBatcher		m_batcher;
	Spline[]			m_spline = { 
			new Spline( Color.RED ).addPoint( new Vector3( 0.0f, 0.0f, 50 ) ).addPoint( new Vector3( 1.0f, 1.0f, 50 ) ),
			new Spline( Color.RED ).addPoint( new Vector3( 0.8f, 0.0f, 10 ) ).addPoint( new Vector3( 0.8f, 9.6f, 10 ) ),
			new Spline( Color.BLUE ).addPoint( new Vector3( 1.89f, 0.0f, 10 ) ).addPoint( new Vector3( 1.89f, 9.6f, 10 ) ),
			new Spline( Color.GREEN ).addPoint( new Vector3( 3.0f, 0.0f, 10 ) ).addPoint( new Vector3( 3.0f, 9.6f, 10 ) ),
			new Spline( Color.WHITE ).addPoint( new Vector3( 4.0f, 0.0f, 10 ) ).addPoint( new Vector3( 4.0f, 9.6f, 10 ) ),
	};
	FPSCounter			m_fpsCounter = new FPSCounter();
	
	
	public LinesDrawingScreen( Game game ) 
	{
		super( game );
		
		m_glGraphics = ((GLGame) game).getGraphics();
		
		m_batcher = new SpriteBatcher( m_glGraphics, MAX_SPRITES );
	}
	
	@Override
	public void present( float deltaTime ) 
	{
		GL10 gl = m_glGraphics.getGL();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		gl.glViewport( 0, 0, m_glGraphics.getWidth(), m_glGraphics.getHeight() );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( 0, 4.8f, 0, 9.6f, 1, -1 );
		
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
		
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
		gl.glLineWidth (1.5f);
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glDisable( GL10.GL_DEPTH_TEST );
		
		for ( int i = 0; i < m_spline.length; ++i )
		{
			m_batcher.drawSpline( m_spline[i] );
		}
		
		m_batcher.flush();
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
