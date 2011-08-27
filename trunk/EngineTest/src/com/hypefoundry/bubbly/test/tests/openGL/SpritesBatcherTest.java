package com.hypefoundry.bubbly.test.tests.openGL;

import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.GLGraphics;
import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.core.Texture;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.util.FPSCounter;



public class SpritesBatcherTest extends GLGame 
{
	@Override
	public Screen getStartScreen() 
	{
		return new SpritesBatcherScreen( this );
	}
}

//----------------------------------------------------------------------------

class SpritesBatcherScreen extends Screen 
{

	final int	 		MAX_SPRITES = 100;
	final int 			VERTEX_SIZE = (2 + 4) * 4;
	GLGraphics 			m_glGraphics;
		
	SpriteBatcher		m_batcher;
	ResourceManager		m_resourceManager;
	TextureRegion		m_pixmap;
	FPSCounter			m_fpsCounter = new FPSCounter();
	
	
	public SpritesBatcherScreen( Game game ) 
	{
		super( game );
		
		m_glGraphics = ((GLGame) game).getGraphics();
		m_resourceManager = new ResourceManager( game );
		m_resourceManager.loadResources();
		
		m_batcher = new SpriteBatcher( m_glGraphics, MAX_SPRITES );
		
		Texture atlas = m_resourceManager.getResource( Texture.class, "bitmaps/bobargb8888.png" );
		m_pixmap = new TextureRegion( atlas, 0, 0, 256, 256 );
	}
	
	@Override
	public void present( float deltaTime ) 
	{
		GL10 gl = m_glGraphics.getGL();
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		for ( int i = 0; i < MAX_SPRITES; ++i )
		{
			m_batcher.drawSprite( i * 5, i * 5, 32, 32, m_pixmap );
		}

		m_batcher.flush();
		
		// m_fpsCounter.logFrame();			// <== GC flooder
	}
	
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		GL10 gl = m_glGraphics.getGL();
		
		gl.glViewport( 0, 0, m_glGraphics.getWidth(), m_glGraphics.getHeight() );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();
		gl.glOrthof( 0, 320, 0, 480, 1, -1 );
		
		gl.glEnable( GL10.GL_TEXTURE_2D );	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
