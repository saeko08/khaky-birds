/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.zombie;


import com.hypefoundry.khakyBirds.entities.crap.Crappable;
import com.hypefoundry.khakyBirds.entities.hunter.Shootable;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class Zombie extends Entity implements Crappable, Shootable

{
boolean				m_hitWithShit;
public World 		m_world    				     = null;

	enum State
	{
		Wander,
		TurnAround,
		Observe,
		Chasing,
		Eat,
		Avoid
	}
	
	State		m_state;
	
	/**
	 * Serialization support constructor.
	 */
	public Zombie()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, 0.2f, 0.2f ) );	// TODO: config
		setPosition( 0, 0, 80 );
		
		// add movement capabilities
		final float maxLinearSpeed = 1.15f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	/**
	 * default Constructor.
	 *
	 * @param spawnAreaWidth
	 * @param spawnAreaHeight
	 */
	public Zombie( float spawnAreaWidth, float spawnAreaHeight )
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
	 * Constructor.
	 *
	 * @param Vector3 pos
	 */
	public Zombie(Vector3 pos)
	{
		// call the default constructor first to perform the generic initialization
		this();
		
		//initialize specified position
		setPosition(pos );
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
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
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
