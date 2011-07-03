package com.hypefoundry.bubbly;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BubblyMain extends Activity {
	
    /** A handle to the View in which the game is running. */
    private BubblyView mBubblyView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // tell system to use the layout defined in our XML file
        setContentView(R.layout.main);

        // give the LunarView a handle to the TextView used for messages
        mBubblyView = (BubblyView) findViewById(R.id.bubbly);
        mBubblyView.setTextView((TextView) findViewById(R.id.text));
    }
}