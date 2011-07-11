package com.hypefoundry.engine.impl.core.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccelerometerHandler implements SensorEventListener 
{
	float 		m_accelX;
	float 		m_accelY;
	float 		m_accelZ;
	
	public AccelerometerHandler( Context context ) 
	{
		SensorManager manager = (SensorManager) context.getSystemService( Context.SENSOR_SERVICE );
		if ( manager.getSensorList( Sensor.TYPE_ACCELEROMETER).size() != 0 ) 
		{
			Sensor accelerometer = manager.getSensorList( Sensor.TYPE_ACCELEROMETER ).get(0);
			manager.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_GAME );
		}
	}
	
	@Override
	public void onAccuracyChanged( Sensor sensor, int accuracy ) 
	{
		// nothing to do here
	}
	
	@Override
	public void onSensorChanged( SensorEvent event ) 
	{
		m_accelX = event.values[0];
		m_accelY = event.values[1];
		m_accelZ = event.values[2];
	}
	
	public float getAccelX() 
	{
		return m_accelX;
	}
	
	public float getAccelY() 
	{
		return m_accelY;
	}
	
	public float getAccelZ() 
	{
		return m_accelZ;
	}
}
