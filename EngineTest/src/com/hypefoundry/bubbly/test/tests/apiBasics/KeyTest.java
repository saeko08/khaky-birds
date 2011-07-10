package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;


public class KeyTest extends Activity implements OnKeyListener
{
	StringBuilder 	m_builder = new StringBuilder();
	TextView 		m_textView;
	
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		m_textView = new TextView( this );
		m_textView.setText( "Press keys (if you have some)!" );
		m_textView.setOnKeyListener( this );
		m_textView.setFocusableInTouchMode( true );
		m_textView.requestFocus();
		setContentView( m_textView );
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) 
	{
		m_builder.setLength(0);
		
		switch ( event.getAction() ) 
		{
		case KeyEvent.ACTION_DOWN:
			m_builder.append("down, ");
		break;
		case KeyEvent.ACTION_UP:
			m_builder.append("up, ");
		break;
		}
		
		m_builder.append(event.getKeyCode());
		m_builder.append(", ");
		m_builder.append((char) event.getUnicodeChar());
		
		String text = m_builder.toString();
		Log.d("KeyTest", text);
		m_textView.setText(text);
		
		boolean wasKeyProcessed = ( event.getKeyCode() == KeyEvent.KEYCODE_BACK );
		return wasKeyProcessed;
	}
}
