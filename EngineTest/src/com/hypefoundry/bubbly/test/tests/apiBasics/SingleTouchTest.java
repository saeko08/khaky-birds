package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;


public class SingleTouchTest extends Activity implements OnTouchListener 
{
	StringBuilder 	m_builder = new StringBuilder();
	TextView 		m_textView;
	
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		m_textView = new TextView( this );
		m_textView.setText( "Touch and drag (one finger only)!" );
		m_textView.setOnTouchListener( this );
		setContentView( m_textView );
	}
	
	@Override
	public boolean onTouch( View v, MotionEvent event ) 
	{
		m_builder.setLength(0);
		switch (event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			m_builder.append("down, ");
		break;
		case MotionEvent.ACTION_MOVE:
			m_builder.append("move, ");
		break;
		case MotionEvent.ACTION_CANCEL:
			m_builder.append("cancle, ");
		break;
		case MotionEvent.ACTION_UP:
			m_builder.append("up, ");
		break;
		}
		
		m_builder.append(event.getX());
		m_builder.append(", ");
		m_builder.append(event.getY());
		String text = m_builder.toString();
		Log.d("TouchTest", text);
		m_textView.setText(text);
		
		return true;
	}
}
