package com.hypefoundry.bubbly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

public class BubblyView extends SurfaceView implements SurfaceHolder.Callback {
	
	class AnimationThread extends Thread {
		
		/** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;
        
		/** Message handler used by thread to interact with TextView */
        private Handler mHandler;
        
        /** Handle to the application context, used to e.g. fetch Drawables. */
        private Context mContext;
        
        /** Is the thread running? */
        private boolean mRun = false;
        
        /** The drawable to use as the background of the animation canvas */
        private Bitmap mBackgroundImage;
        
        /**
         * Current height of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        private int mCanvasHeight = 1;

        /**
         * Current width of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        private int mCanvasWidth = 1;
        
		public AnimationThread(SurfaceHolder surfaceHolder, Context context,
	                Handler handler) {
			 
			// get handles to some important objects
	        mSurfaceHolder = surfaceHolder;
	        mHandler = handler;
	        mContext = context;
	        
	        mRun = false;
		}
		
		/**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }
		
		/* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
/*
                // don't forget to resize the background image
                mBackgroundImage = mBackgroundImage.createScaledBitmap(
                        mBackgroundImage, width, height, true);*/
            }
        }
		
		@Override
        public void run() {
            while ( mRun ) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
		
		/**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
            canvas.restore();
        }

	}
	
	/** The thread that actually draws the animation */
    private AnimationThread thread;

	/** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;
    
	public BubblyView( Context context, AttributeSet attrs ) {
        super(context, attrs);
        
        SurfaceHolder holder = getHolder();
        
        // create thread only; it's started in surfaceCreated()
        thread = new AnimationThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true); // make sure we get key events
        
        // register our interest in hearing about changes to our surface
        holder.addCallback(this);
    }
	
	/**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
		// start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();	
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

}
