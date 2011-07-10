package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class AccelerometerTest extends Activity implements SensorEventListener
{
	TextView 			m_textView;
	StringBuilder 		m_builder = new StringBuilder();
	
	@Override
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		m_textView = new TextView(this);
		setContentView( m_textView );
		
		SensorManager manager = (SensorManager)getSystemService( Context.SENSOR_SERVICE );
		if ( manager.getSensorList( Sensor.TYPE_ACCELEROMETER ).size() == 0 ) 
		{
			m_textView.setText( "No accelerometer installed" );
		} 
		else 
		{
			Sensor accelerometer = manager.getSensorList( Sensor.TYPE_ACCELEROMETER ).get(0);
			if ( !manager.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_GAME ) ) 
			{
				m_textView.setText("Couldn't register sensor listener");
			}
		}
	}
	
	@Override
	public void onSensorChanged( SensorEvent event ) 
	{
		m_builder.setLength( 0 );
		m_builder.append( "x: " );
		m_builder.append( event.values[0] );
		m_builder.append( ", y: " );
		m_builder.append( event.values[1] );
		m_builder.append( ", z: " );
		m_builder.append( event.values[2] );
		m_textView.setText( m_builder.toString() );
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// nothing to do here
	}
}
