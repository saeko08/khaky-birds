package com.hypefoundry.engine.impl.core;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.hypefoundry.engine.core.Input;
import com.hypefoundry.engine.impl.core.input.AccelerometerHandler;
import com.hypefoundry.engine.impl.core.input.KeyboardHandler;
import com.hypefoundry.engine.impl.core.input.MultiTouchHandler;
import com.hypefoundry.engine.impl.core.input.SingleTouchHandler;
import com.hypefoundry.engine.impl.core.input.TouchHandler;

/**
 * An implementation using the Android API.
 * 
 * @author paksas
 *
 */
public class AndroidInput implements Input 
{
	AccelerometerHandler 	m_accelHandler;
	KeyboardHandler 		m_keyHandler;
	TouchHandler 			m_touchHandler;
	
	/**
	 * Constructor.
	 * 
	 * @param context				activity context
	 * @param view					view we want to be receiving key events from
	 * @param scaleX				screen scale X
	 * @param scaleY				screen scale Y
	 * @param doubleTapDuration		period between two consecutive touch-downs ( in seconds ) that will allow us 
	 * 								to consider them a single double tap event
	 */
	public AndroidInput( Context context, View view, float scaleX, float scaleY, float doubleTapDuration ) 
	{
		m_accelHandler = new AccelerometerHandler( context );
		m_keyHandler = new KeyboardHandler( view );
		
		// create a proper touch handler based on the Android version
		if( Integer.parseInt( VERSION.SDK ) < 5 )
		{
			m_touchHandler = new SingleTouchHandler( view, scaleX, scaleY, doubleTapDuration );
		}
		else
		{
			m_touchHandler = new MultiTouchHandler( view, scaleX, scaleY, doubleTapDuration );
		}
	}
	
	@Override
	public boolean isKeyPressed( int keyCode ) 
	{
		return m_keyHandler.isKeyPressed( keyCode );
	}

	@Override
	public boolean isTouchDown( int pointer ) 
	{
		return m_touchHandler.isTouchDown( pointer );
	}

	@Override
	public int getTouchX( int pointer ) 
	{
		return m_touchHandler.getTouchX( pointer );
	}

	@Override
	public int getTouchY( int pointer ) 
	{
		return m_touchHandler.getTouchY( pointer );
	}
	
	@Override
	public float getTouchDuriation( int pointer )
	{
		return m_touchHandler.getTouchDuriation( pointer );
	}
	
	@Override
	public void clearTouchDuration()
	{
		m_touchHandler.clearTouchDuration();
	}

	@Override
	public float getAccelX() 
	{
		return m_accelHandler.getAccelX();
	}

	@Override
	public float getAccelY() 
	{
		return m_accelHandler.getAccelY();
	}

	@Override
	public float getAccelZ() 
	{
		return m_accelHandler.getAccelZ();
	}

	@Override
	public List<KeyEvent> getKeyEvents() 
	{
		return m_keyHandler.getKeyEvents();
	}

	@Override
	public List<TouchEvent> getTouchEvents() 
	{
		return m_touchHandler.getTouchEvents();
	}
	
	@Override 
	public void clear()
	{
		m_accelHandler.clear();
		m_keyHandler.clear();
		m_touchHandler.clear();
	}

	/**
	 * Updates the status of the input manager.
	 * @param deltaTime
	 */
	public void update( float deltaTime )
	{
		m_touchHandler.update( deltaTime );
	}
}
