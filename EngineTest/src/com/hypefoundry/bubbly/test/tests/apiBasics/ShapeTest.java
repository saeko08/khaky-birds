package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ShapeTest extends Activity 
{
	class RenderView extends View 
	{
		Paint 		m_paint;
		
		public RenderView( Context context ) 
		{
			super( context );
			m_paint = new Paint();
		}
		
		protected void onDraw( Canvas canvas ) 
		{
			canvas.drawRGB(255, 255, 255);
			m_paint.setColor(Color.RED);
			canvas.drawLine(0, 0, canvas.getWidth()-1, canvas.getHeight()-1, m_paint);
			m_paint.setStyle(Style.STROKE);
			m_paint.setColor(0xff00ff00);
			canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 40, m_paint);
			m_paint.setStyle(Style.FILL);
			m_paint.setColor(0x770000ff);
			canvas.drawRect(100, 100, 200, 200, m_paint);
			
			invalidate();
		}
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ) 
	{
		super.onCreate( savedInstanceState );
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView( new RenderView( this ) );
	}
}
