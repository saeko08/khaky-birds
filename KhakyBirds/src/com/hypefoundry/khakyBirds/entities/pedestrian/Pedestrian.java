package com.hypefoundry.khakyBirds.entities.pedestrian;

import com.hypefoundry.khakyBirds.entities.crap.Crappable;
import com.hypefoundry.khakyBirds.entities.hunter.Shootable;
import com.hypefoundry.khakyBirds.entities.zombie.Biteable;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
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
public class Pedestrian extends Entity implements Crappable, Biteable
{
	boolean				m_hitWithShit;
	public World 		m_world    				     = null;
	boolean 			m_wasBeaten 				 = false;
	
	enum State
	{
		Wander,
		TurnAround,
		Observe,
		Evade,
		Eaten,
		Avoid,
		Hiding,
		Shitted
	}
	
	State		m_state;
	
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
	 * Constructor.
	 *
	 * @param Vector3
	 *
	 */
	public Pedestrian( Vector3 spawnPos )
	{
		// call the default constructor first to perform the generic initialization
		this();
		
		setPosition( spawnPos.m_x, spawnPos.m_y, 80 );
		
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
	
	public void turnIntoZombie()
	{
		if (m_wasBeaten == false)
		{
			m_wasBeaten = true;
			Vector3 pedestrianPos = getPosition();
			m_world.addEntity( new Zombie(pedestrianPos));
		}
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
