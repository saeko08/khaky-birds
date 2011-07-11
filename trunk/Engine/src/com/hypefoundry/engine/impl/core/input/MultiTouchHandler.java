package com.hypefoundry.engine.impl.core.input;

import java.util.ArrayList;
import java.util.List;

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
	boolean[] 			m_isTouched = new boolean[20];
	int[] 				m_touchX = new int[20];
	int[] 				m_touchY = new int[20];
	Pool<TouchEvent> 	m_touchEventPool;
	List<TouchEvent> 	m_touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> 	m_touchEventsBuffer = new ArrayList<TouchEvent>();
	float 				m_scaleX;
	float 				m_scaleY;
	
	
	/**
	 * Constructor.
	 * 
	 * @param view				view the touches of which we want to listen to
	 * @param scaleX			X scale of the screen
	 * @param scaleY			Y scale of the screen
	 */
	public MultiTouchHandler( View view, float scaleX, float scaleY ) 
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
			
			TouchEvent touchEvent;
			switch ( action ) 
			{
				case MotionEvent.ACTION_DOWN:				// fallthrough
				case MotionEvent.ACTION_POINTER_DOWN:
				{
					touchEvent = m_touchEventPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					touchEvent.pointer = pointerId;
					touchEvent.x = m_touchX[pointerId] = (int)( event.getX( pointerIndex ) * m_scaleX );
					touchEvent.y = m_touchY[pointerId] = (int)( event.getY( pointerIndex ) * m_scaleY );
					m_isTouched[pointerId] = true;
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
			if ( pointer < 0 || pointer >= 20 )
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
			if ( pointer < 0 || pointer >= 20 )
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
			if ( pointer < 0 || pointer >= 20 )
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
