package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;

import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;


/**
 * A pedestrian populating the game world.
 * 
 * It's a walking target for the bird, and shitting on him
 * will earn us some points. 
 * 
 * @author paksas
 *
 */
public class Pedestrian extends Entity
{
	boolean				m_hitWithShit;
	
	/**
	 * Serialization support constructor.
	 */
	public Pedestrian()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		setPosition( 0, 0, 80 );
		
		// add movement capabilities
		final float maxLinearSpeed = 1.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	/**
	 * Constructor.
	 *
	 * @param spawnAreaWidth
	 * @param spawnAreaHeight
	 */
	public Pedestrian( float spawnAreaWidth, float spawnAreaHeight )
	{
		// call the default constructor first to perform the generic initialization
		this();
		
		// initialize random position
		float x, y;
		x = (float) Math.random() * spawnAreaWidth;
		y = (float) Math.random() * spawnAreaHeight;
		setPosition( x, y, 80 );
		
		m_hitWithShit = false;
	}


	/**
	 * Tells that the pedestrian was hit with shit.
	 * 
	 * @param flag
	 */
	public void setHitWithShit( boolean flag ) 
	{
		m_hitWithShit = flag;
	}

	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_hitWithShit = ( loader.getIntValue( "hitWithShit" ) == 1 );
	}
	
	@Override
	public void onSave( DataSaver saver ) 
	{
		saver.setIntValue( "hitWithShit", m_hitWithShit ? 1 : 0  );
	}
}
