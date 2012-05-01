/**
 * 
 */
package com.hypefoundry.kabloons.entities.webLink;

import java.util.List;

import android.content.Intent;
import android.net.Uri;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.core.Input.TouchEvent;
import com.hypefoundry.engine.game.Game;
import com.hypefoundry.engine.game.InputHandler;
import com.hypefoundry.engine.game.Screen;
import com.hypefoundry.engine.impl.game.GLGame;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.renderer2D.Camera2D;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class WebLink extends Entity implements InputHandler
{
	String						m_imagePath;
	String 						m_url;
	
	private		Game			m_game;
	private		Camera2D		m_camera;
	private		Vector3			m_touchPos 		= new Vector3();
	
	
	/**
	 * Constructor.
	 * 
	 * @param game
	 * @param parentScreen
	 * @param camera
	 */
	public WebLink( Game game, Screen parentScreen, Camera2D camera )
	{
		m_game = game;
		parentScreen.registerInputHandler( this );
		
		m_camera = camera;
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_imagePath = loader.getStringValue( "path" );
		m_url = loader.getStringValue( "url" );
	}

	@Override
	public boolean handleInput( Input input, float deltaTime ) 
	{
		boolean linkHit = false;
		
		List< TouchEvent > inputEvents = input.getTouchEvents();
		int count = inputEvents.size();
		for ( int i = 0 ; i < count; ++i )
		{				
			TouchEvent lastEvent = inputEvents.get(i);
			if ( lastEvent.type == TouchEvent.TOUCH_DOWN )
			{
				// change the gesture direction from screen to model space
				m_touchPos.set( lastEvent.x, lastEvent.y, 0 );
				m_camera.screenPosToWorld( m_touchPos, m_touchPos );
				
				if ( this.getWorldBounds().doesOverlap( m_touchPos, null ) == true )
				{
					// link has been hit
					linkHit = true;
					break;
				}
			}
		}
		
		if ( linkHit )
		{
			followLink();
		}

		return linkHit;
	}
	
	/**
	 * Follows a link to the hypeforge web page 
	 */
	private void  followLink()
	{
		Intent i = new Intent( "android.intent.action.VIEW", Uri.parse( m_url ) ); 
		((GLGame)m_game).startActivity( i );
	}
}
