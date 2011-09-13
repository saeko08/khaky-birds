package com.hypefoundry.engine.impl.core.input;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.hypefoundry.engine.util.Pool;
import com.hypefoundry.engine.util.PoolObjectFactory;
import com.hypefoundry.engine.core.Input.TouchEvent;

/**
 * A touch handler for Android systems < 2.0, that don't support multi-touch
 * screens.
 * 
 * @author paksas
 *
 */
public class SingleTouchHandler implements TouchHandler 
{	
	boolean 			m_isTouched;
	int	 				m_touchX;
	int 				m_touchY;
	Pool<TouchEvent> 	m_touchEventPool;
	List<TouchEvent> 	m_touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> 	m_touchEventsBuffer = new ArrayList<TouchEvent>();
	float 				m_scaleX;
	float 				m_scaleY;
	float				m_touchDuration;
	float				m_doubleTapStartTime = -1;
	float				m_doubleTapDuration;
	
	
	/**
	 * Constructor.
	 * 
	 * @param view					view the touches of which we want to listen to
	 * @param scaleX				X scale of the screen
	 * @param scaleY				Y scale of the screen
	 * @param doubleTapDuration		period between two consecutive touch-downs ( in seconds ) that will allow us 
	 * 								to consider them a single double tap event
	 */
	public SingleTouchHandler( View view, float scaleX, float scaleY, float doubleTapDuration ) 
	{
		// create the TouchEvents objects factory
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() 
		{
			@Override
			public TouchEvent createObject() 
			{
				return new TouchEvent();
			}
		};
		// set the touch listener on the selected view
		m_touchEventPool = new Pool<TouchEvent>( factory, 100 );
		view.setOnTouchListener( this );
		
		m_scaleX = scaleX;
		m_scaleY = scaleY;
		m_doubleTapDuration = doubleTapDuration;
	}
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) 
	{
		// the events are received on the UI thread, and read on the main loop thread
		// or hell knows where, so we need to keep the access to our members synchronized
		synchronized( this ) 
		{
			TouchEvent touchEvent = m_touchEventPool.newObject();
			touchEvent.x = m_touchX = (int)( event.getX() * m_scaleX );
			touchEvent.y = m_touchY = (int)( event.getY() * m_scaleY );
			
			float currTime = System.nanoTime() / 1000000000.0f; // [s]
			
			switch ( event.getAction() ) 
			{
			case MotionEvent.ACTION_DOWN:
				
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				
				// check for a double tap event
				float timeDiff = currTime - m_doubleTapStartTime;
				if ( timeDiff <= m_doubleTapDuration )
				{
					// we have a double tap
					touchEvent.type = TouchEvent.TOUCH_DOUBLE_TAP;
				}
				m_doubleTapStartTime = currTime;

				m_isTouched = true;
				m_touchDuration = 0; // reset the touch timer
								
			break;
			
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				m_isTouched = true;
			break;
			
			case MotionEvent.ACTION_CANCEL: 	// fallthrough
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				m_isTouched = false;
			break;
			}
			m_touchEventsBuffer.add( touchEvent );
			
			return true;
		}
	}

	@Override
	public boolean isTouchDown( int pointer ) 
	{
		synchronized( this ) 
		{
			if( pointer == 0 )
			{
				return m_isTouched;
			}
			else
			{
				return false;
			}
		}
	}

	@Override
	public int getTouchX( int pointer ) 
	{
		synchronized( this ) 
		{
			return m_touchX;
		}
	}

	@Override
	public int getTouchY( int pointer ) 
	{
		synchronized( this ) 
		{
			return m_touchY;
		}
	}
	
	@Override
	public float getTouchDuriation( int pointer )
	{
		return m_touchDuration;
	}
	
	@Override
	public void clearTouchDuration()
	{
		m_touchDuration = 0;
	}
	
	@Override
	public void update( float deltaTime )
	{
		synchronized ( this ) 
		{	
			if ( m_isTouched )
			{
				m_touchDuration += deltaTime;
			}
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() 
	{
		synchronized( this ) 
		{
			// free old events back to the pool
			int len = m_touchEvents.size();
			for( int i = 0; i < len; i++ )
			{
				m_touchEventPool.free( m_touchEvents.get( i ) );
			}
			m_touchEvents.clear();
			

			m_touchEvents.addAll( m_touchEventsBuffer );
			m_touchEventsBuffer.clear();
			
			return m_touchEvents;
		}
	}

}
