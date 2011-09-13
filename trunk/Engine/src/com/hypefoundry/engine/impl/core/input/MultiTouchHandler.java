package com.hypefoundry.engine.impl.core.input;

import java.util.*;

import android.view.MotionEvent;
import android.view.View;

import com.hypefoundry.engine.util.Pool;
import com.hypefoundry.engine.util.PoolObjectFactory;
import com.hypefoundry.engine.core.Input.TouchEvent;


/**
 * A touch handler for Android systems >= 2.0, that support multi-touch
 * screens.
 * 
 * @author paksas
 *
 */
public class MultiTouchHandler implements TouchHandler 
{
	private static int  MAX_POINTERS_COUNT = 20;
	
	boolean[] 			m_isTouched = new boolean[MAX_POINTERS_COUNT];
	int[] 				m_touchX = new int[MAX_POINTERS_COUNT];
	int[] 				m_touchY = new int[MAX_POINTERS_COUNT];
	float[]				m_touchDuration = new float[MAX_POINTERS_COUNT];
	float[]				m_doubleTapStartTimes = new float[MAX_POINTERS_COUNT];
	Pool<TouchEvent> 	m_touchEventPool;
	List<TouchEvent> 	m_touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> 	m_touchEventsBuffer = new ArrayList<TouchEvent>();
	float 				m_scaleX;
	float 				m_scaleY;
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
	public MultiTouchHandler( View view, float scaleX, float scaleY, float doubleTapDuration ) 
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
		
		for ( int i = 0; i < m_doubleTapStartTimes.length; ++i )
		{
			m_doubleTapStartTimes[i] = -1;
		}
		
		m_scaleX = scaleX;
		m_scaleY = scaleY;
		m_doubleTapDuration = doubleTapDuration;
	}
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) 
	{
		// the events are received on the UI thread, and read on the main loop thread
		// or hell knows where, so we need to keep the access to our members synchronized
		synchronized ( this ) 
		{
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = ( event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK ) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = event.getPointerId( pointerIndex );
			
			float currTime = System.nanoTime() / 1000000000.0f; // [s]
			
			TouchEvent touchEvent;
			switch ( action ) 
			{
				case MotionEvent.ACTION_DOWN:				// fallthrough
				case MotionEvent.ACTION_POINTER_DOWN:
				{
					touchEvent = m_touchEventPool.newObject();
					touchEvent.pointer = pointerId;
					touchEvent.x = m_touchX[pointerId] = (int)( event.getX( pointerIndex ) * m_scaleX );
					touchEvent.y = m_touchY[pointerId] = (int)( event.getY( pointerIndex ) * m_scaleY );
					m_isTouched[pointerId] = true;
					m_touchDuration[pointerId] = 0;  // reset the touch timer
					
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					
					// check for a double tap event
					float timeDiff = currTime - m_doubleTapStartTimes[pointerId];
					if ( timeDiff <= m_doubleTapDuration )
					{
						// we have a double tap
						touchEvent.type = TouchEvent.TOUCH_DOUBLE_TAP;
					}
					m_doubleTapStartTimes[pointerId] = currTime;
					
					m_touchEventsBuffer.add( touchEvent );
					break;
				}
				
				case MotionEvent.ACTION_UP:					// fallthrough
				case MotionEvent.ACTION_POINTER_UP:			// fallthrough
				case MotionEvent.ACTION_CANCEL:
				{
					touchEvent = m_touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_UP;
					touchEvent.pointer = pointerId;
					touchEvent.x = m_touchX[pointerId] = (int)( event.getX( pointerIndex ) * m_scaleX );
					touchEvent.y = m_touchY[pointerId] = (int)( event.getY( pointerIndex ) * m_scaleY );
					m_isTouched[pointerId] = false;
					m_touchEventsBuffer.add( touchEvent );
					break;
				}
				
				case MotionEvent.ACTION_MOVE:
				{
					int pointerCount = event.getPointerCount();
					for ( int i = 0; i < pointerCount; i++ ) 
					{
						pointerIndex = i;
						pointerId = event.getPointerId( pointerIndex );
						touchEvent = m_touchEventPool.newObject();
						touchEvent.type = TouchEvent.TOUCH_DRAGGED;
						touchEvent.pointer = pointerId;
						touchEvent.x = m_touchX[pointerId] = (int)( event.getX( pointerIndex ) * m_scaleX );
						touchEvent.y = m_touchY[pointerId] = (int)( event.getY( pointerIndex ) * m_scaleY );
						m_touchEventsBuffer.add(touchEvent);
					}
					break;
				}
			}
		}
			
		return true;
	}

	@Override
	public boolean isTouchDown( int pointer ) 
	{
		synchronized ( this ) 
		{
			if ( pointer < 0 || pointer >= MAX_POINTERS_COUNT )
			{
				return false;
			}
			else
			{
				return m_isTouched[pointer];
			}
		}
	}

	@Override
	public int getTouchX( int pointer ) 
	{
		synchronized ( this ) 
		{
			if ( pointer < 0 || pointer >= MAX_POINTERS_COUNT )
			{
				return 0;
			}
			else
			{
				return m_touchX[pointer];
			}
		}
	}

	@Override
	public int getTouchY( int pointer ) 
	{
		synchronized ( this ) 
		{
			if ( pointer < 0 || pointer >= MAX_POINTERS_COUNT )
			{
				return 0;
			}
			else
			{
				return m_touchY[pointer];
			}
		}
	}
	
	@Override
	public float getTouchDuriation( int pointer )
	{
		synchronized ( this ) 
		{
			if ( pointer < 0 || pointer >= MAX_POINTERS_COUNT )
			{
				return 0;
			}
			else
			{
				return m_touchDuration[pointer];
			}
		}
	}
	
	@Override
	public void clearTouchDuration()
	{
		synchronized ( this ) 
		{
			for ( int i = 0; i < m_touchDuration.length; ++i )
			{
				m_touchDuration[i] = 0;
			}
		}
	}
	
	@Override
	public void update( float deltaTime )
	{
		synchronized ( this ) 
		{
			for ( int i = 0; i < MAX_POINTERS_COUNT; ++i )
			{
				if ( m_isTouched[i] )
				{
					m_touchDuration[i] += deltaTime;
				}
			}
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() 
	{
		synchronized ( this ) 
		{
			int len = m_touchEvents.size();
			for ( int i = 0; i < len; i++ )
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
