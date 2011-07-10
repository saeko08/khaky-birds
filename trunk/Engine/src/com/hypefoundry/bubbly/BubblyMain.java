package com.hypefoundry.bubbly;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BubblyMain extends Activity {
	
    /** A handle to the View in which the game is running. */
    private BubblyView mBubblyView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        
        // tell system to use the layout defined in our XML file
        setContentView( R.layout.main );

        // get the view instance
        mBubblyView = (BubblyView)findViewById( R.id.bubbly );
    }
    
}