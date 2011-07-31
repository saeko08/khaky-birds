package com.hypefoundry.engine.impl.core.input;

import java.util.ArrayList;
import java.util.List;

import com.hypefoundry.engine.util.Pool;
import com.hypefoundry.engine.util.PoolObjectFactory;
import com.hypefoundry.engine.core.Input.KeyEvent;

import android.view.View;
import android.view.View.OnKeyListener;


public class KeyboardHandler implements OnKeyListener 
{
	boolean[] 		m_pressedKeys 	= new boolean[128];
	Pool<KeyEvent> 	m_keyEventPool;
	List<KeyEvent> 	m_keyEventsBuffer = new ArrayList<KeyEvent>();
	List<KeyEvent> 	m_keyEvents = new ArrayList<KeyEvent>();
	
	/**
	 * Constructor.
	 * 
	 * @param view			view we want to be receiving key events from
	 */
	public KeyboardHandler( View view ) 
	{
		// define the KeyEvents factory class 
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() 
		{
			@Override
			public KeyEvent createObject() 
			{
				return new KeyEvent();
			}
		};
		
		m_keyEventPool = new Pool<KeyEvent>( factory, 100 );
		view.setOnKeyListener( this );
		view.setFocusableInTouchMode( true );
		view.requestFocus();	
	}

	@Override
	public boolean onKey( View v, int keyCode, android.view.KeyEvent event ) 
	{
		if ( event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE )
		{
			// we're not interested in events of the 'multiple' type
			return false;
		}
		
		// the events are received on the UI thread, and read on the main loop thread
		// or hell knows where, so we need to keep the access to our members synchronized
		synchronized (this) 
		{
			KeyEvent keyEvent = m_keyEventPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			
			if ( event.getAction() == android.view.KeyEvent.ACTION_DOWN ) 
			{
				keyEvent.type = KeyEvent.KEY_DOWN;
				if( keyCode > 0 && keyCode < 127 )
				{
					m_pressedKeys[keyCode] = true;
				}
			}
			
			if ( event.getAction() == android.view.KeyEvent.ACTION_UP ) 
			{
				keyEvent.type = KeyEvent.KEY_UP;
				if( keyCode > 0 && keyCode < 127 )
				{
					m_pressedKeys[keyCode] = false;
				}
			}
			m_keyEventsBuffer.add( keyEvent );
		}
		
		return false;
	}
	
	/**
	 * Checks if the key is pressed.
	 * 
	 * @param keyCode			KeyEvent.KEYCODE_XXX constant
	 * @return
	 */
	public boolean isKeyPressed( int keyCode ) 
	{
		if ( keyCode < 0 || keyCode > 127 )
		{
			return false;
		}
		
		// no need to synchronize - even though we're reading from
		// an array that's written to by a different thread, 
		// we're working with primitive types
		return m_pressedKeys[keyCode];
	}

	/**
	 * Returns all occurring key events.
	 * 
	 *  PERFORMANCE CAVEAT: Poll this frequently in order to prevent
	 *  too many key event objects from being created and thus allowing
	 *  the GC to step in - as long as you're polling it frequently,
	 *  only objects created in the pool we'll be used.
	 * 
	 * @return
	 */
	public List<KeyEvent> getKeyEvents() 
	{
		// now we need to synchronize - we're working with collections
		synchronized ( this ) 
		{
			int len = m_keyEvents.size();
			for ( int i = 0; i < len; i++ )
			{
				m_keyEventPool.free( m_keyEvents.get( i ) );
			}
			m_keyEvents.clear();
			
			m_keyEvents.addAll( m_keyEventsBuffer );
			m_keyEventsBuffer.clear();
			
			return m_keyEvents;
		}
	}
}
