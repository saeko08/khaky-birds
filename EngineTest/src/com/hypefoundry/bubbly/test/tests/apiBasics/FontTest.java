package com.hypefoundry.bubbly.test.tests.apiBasics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class FontTest extends Activity 
{
	class RenderView extends View 
	{
		Paint 			m_paint;
		Typeface 		m_font;
		Rect 			m_bounds = new Rect();
		
		public RenderView( Context context ) 
		{
			super( context );
			
			m_paint = new Paint();
			m_font = Typeface.createFromAsset( context.getAssets(), "fonts/font.ttf" );
		}
		
		protected void onDraw( Canvas canvas ) 
		{
			m_paint.setColor( Color.YELLOW );
			m_paint.setTypeface( m_font );
			m_paint.setTextSize( 28 );
			m_paint.setTextAlign( Paint.Align.CENTER );
			canvas.drawText( "This is a test!", canvas.getWidth() / 2, 100, m_paint );
			
			String text = "This is another test o_O";
			m_paint.setColor( Color.WHITE );
			m_paint.setTextSize( 18 );
			m_paint.setTextAlign( Paint.Align.LEFT );
			m_paint.getTextBounds( text, 0, text.length(), m_bounds );
			canvas.drawText( text, canvas.getWidth() - m_bounds.width(), 140, m_paint );
			invalidate();
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate( savedInstanceState );
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView( new RenderView( this ) );
	}
}
