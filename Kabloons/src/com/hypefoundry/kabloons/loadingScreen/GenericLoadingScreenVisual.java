/**
 * 
 */
package com.hypefoundry.kabloons.loadingScreen;


import javax.microedition.khronos.opengles.GL10;

import com.hypefoundry.engine.core.ResourceManager;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.impl.game.LoadingScreenVisual;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.renderer2D.Color;
import com.hypefoundry.engine.renderer2D.RenderState;
import com.hypefoundry.engine.renderer2D.SpriteBatcher;
import com.hypefoundry.engine.renderer2D.StencilOp;
import com.hypefoundry.engine.renderer2D.TextureRegion;
import com.hypefoundry.engine.renderer2D.meshes.ColoredMesh;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystem;
import com.hypefoundry.engine.renderer2D.particleSystem.ParticleSystemPlayer;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class GenericLoadingScreenVisual extends LoadingScreenVisual 
{
	ParticleSystemPlayer	m_bubblesFxPlayer;
	TextureRegion			m_loadingScreenImage;
	
	ColoredMesh				m_bgQuad;
	RenderState				m_bgQuadRenderState;
	
	float 					m_yPos					= -9.0f;
	float					m_velocity				= 10.0f;
	float					m_yBoundary				= 0.0f;
	float					m_fxYOffset				= 0.0f;
	/**
	 * Constructor.
	 * 
	 * @param resMgr
	 * @param loadingScreenEntity
	 */
	public GenericLoadingScreenVisual( ResourceManager resMgr, Entity loadingScreenEntity ) 
	{
		super( loadingScreenEntity );
		
		ParticleSystem bubblesFx = resMgr.getResource( ParticleSystem.class, "loadingScreen/bubblesFx.xml" );
		m_bubblesFxPlayer = new ParticleSystemPlayer( bubblesFx, false );
			
		m_loadingScreenImage = resMgr.getResource( TextureRegion.class, "loadingScreen/loadingScreenImg.xml" );
		
		// prepare the background quad
		m_bgQuad = new ColoredMesh( GLGame.getInstance().getGraphics(), 4, 6 );
		m_bgQuad.lock();
		m_bgQuad.m_vertices[0].set( 0.0f, 0.0f, 0.0f );
		m_bgQuad.m_vertexColor[0] = Color.WHITE;
		m_bgQuad.m_vertices[1].set( 0.0f, 8.0f, 0.0f );
		m_bgQuad.m_vertexColor[1] = Color.WHITE;
		m_bgQuad.m_vertices[2].set( 4.8f, 8.0f, 0.0f );
		m_bgQuad.m_vertexColor[2] = Color.WHITE;
		m_bgQuad.m_vertices[3].set( 4.8f, 0.0f, 0.0f );
		m_bgQuad.m_vertexColor[3] = Color.WHITE;
		
		m_bgQuad.m_indices[0] = 0;
		m_bgQuad.m_indices[1] = 1;
		m_bgQuad.m_indices[2] = 2;
		m_bgQuad.m_indices[3] = 0;
		m_bgQuad.m_indices[4] = 2;
		m_bgQuad.m_indices[5] = 3;
		m_bgQuad.unlock();
		
		// .. and its state
		m_bgQuadRenderState = new RenderState();
		
		// start from the fadeout
		m_yPos = -9.0f;
		m_yBoundary = 0.0f;
		m_fxYOffset = 8.0f;
	}

	@Override
	public void draw( SpriteBatcher batcher, Camera2D camera, float deltaTime ) 
	{	
		// update the position
		m_yPos += m_velocity * deltaTime;
		m_yPos = Math.min( m_yPos, m_yBoundary );
			
		// draw the stenciled quad sliding into position on the screen
		GL10 gl = batcher.m_graphics.getGL();
		gl.glPushMatrix();
		
		m_bgQuadRenderState.setStencilOp( StencilOp.SO_Write_Or );
		gl.glTranslatef( 0, m_yPos, 0 );
		batcher.drawMesh( m_bgQuad, m_bgQuadRenderState );
		
		gl.glPopMatrix();
		
		// draw the effect
		Vector3 pos = m_entity.getPosition();
		m_bubblesFxPlayer.draw( pos.m_x, m_yPos + m_fxYOffset, batcher, deltaTime );
		
		// draw the non-stenciled background
		m_bgQuadRenderState.setStencilOp( StencilOp.SO_Test );
		batcher.drawMesh( m_bgQuad, m_bgQuadRenderState );
		
		
		// draw the stencil tested graphics on top of it all
		//batcher.drawSprite( pos, bs, m_loadingScreenImage );*/
	}

	@Override
	public void startFadeIn() 
	{		
		m_yBoundary = 9.0f;
		m_fxYOffset = -0.0f;
	}


	@Override
	public boolean hasFadedOut() 
	{
		return m_yPos >= m_yBoundary;
	}

	@Override
	public boolean hasFadedIn() 
	{
		return m_yPos >= m_yBoundary;
	}

}
