package com.hypefoundry.engine.test.renderer2D;

import com.hypefoundry.engine.renderer2D.Animation;
import com.hypefoundry.engine.renderer2D.TextureRegion;

import android.test.AndroidTestCase;


public class AnimationTest extends AndroidTestCase 
{
	
	public void testFrameSelection()
	{
		TextureRegion[] frames = {
				new TextureRegion(),
				new TextureRegion(),
				new TextureRegion(),
		};
		Animation animation = new Animation( 0.2f, frames );
		
		assertEquals( frames[0], animation.animate( 0 ) );
		assertEquals( frames[0], animation.animate( 0.1f ) );
		assertEquals( frames[0], animation.animate( 0.19f ) );
		assertEquals( frames[1], animation.animate( 0.2f ) );
		assertEquals( frames[1], animation.animate( 0.21f ) );
		assertEquals( frames[2], animation.animate( 0.41f ) );
		assertEquals( frames[0], animation.animate( 0.6f ) );
		assertEquals( frames[0], animation.animate( 0.61f ) );
	}
}
