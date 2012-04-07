package com.hypefoundry.bubbly.test;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class BubblyTestMain extends ListActivity 
{
	/// Add your test classes here
	private final String m_testClassNames[] = 
	{ 
			"prototypes.khaky_birds.KhakyBirds"
			, "openGL.MoviesPlayback"
			, "openGL.LinesDrawingTest"
			, "openGL.SpritesBatcherTest"
			, "openGL.ColoredTriangleTest"
			, "openGL.GLSurfaceViewTest"
			, "apiBasics.SurfaceViewTest"
			, "apiBasics.FontTest"
			, "apiBasics.BitmapTest"
			, "apiBasics.ShapeTest"
			, "apiBasics.SoundPoolTest"
			, "apiBasics.ExternalStorageTest"
			, "apiBasics.AssetsTest"
			, "apiBasics.AccelerometerTest"
			, "apiBasics.KeyTest"
			, "apiBasics.SingleTouchTest"
			, "misc.LifeCycleTest"
	};
	
	private String 		m_tests[];
	private Class 		m_classes[];
	
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		
		parseClasses();
		setListAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, m_tests ) );
	}
			
	@Override
	protected void onListItemClick( ListView list, View view, int position, long id ) 
	{
		super.onListItemClick( list, view, position, id );
	
		Class testClass = m_classes[position];
		Intent intent = new Intent( this, testClass );
		startActivity( intent );
	}
	
	/**
     * Scans the test framework for available tests.
     */
    private void parseClasses()
    {
        ArrayList< Class > classes = new ArrayList< Class >();
        
        for ( String className : m_testClassNames )
        {
	        try 
	        {
	        	String fullClassName = "com.hypefoundry.bubbly.test.tests." + className;
	        	Class testClass = Class.forName( fullClassName );
	        	classes.add( testClass );
	        } 
	        catch ( ClassNotFoundException e ) 
	        {
	        }
        }

        // obtain simple names of the classes
        ArrayList<String> testNames = new ArrayList<String>();
        for ( Class testClass : classes )
        {
        	testNames.add( testClass.getSimpleName() );
        }
        
        // initialize our arrays based on the previously created lists
        m_classes = new Class[ classes.size() ];
        m_classes = classes.toArray( m_classes);
        
        m_tests = new String[ testNames.size() ];
        m_tests = testNames.toArray( m_tests );
    }
		
}
