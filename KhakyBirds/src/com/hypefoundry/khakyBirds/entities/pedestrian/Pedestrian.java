package com.hypefoundry.khakyBirds.entities.pedestrian;

import com.hypefoundry.khakyBirds.entities.crap.Crappable;
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
	// visual data
	enum Animation
	{
		ANIM_WALK( 0, "WalkAnim" ),
		ANIM_WIPE_SHIT_OFF( 1, "WipeShitOffAnim" ),
		ANIM_OBSERVE( 2, "ObservingAnim" );
		
		int				m_id;
		String			m_xmlId;
		Animation( int id, String xmlId )
		{
			m_id = id;
			m_xmlId = xmlId;
		}
	}
	String[]			m_animPaths = {
			"animations/pedestrian/walking.xml",
			"animations/pedestrian/wipeShitOff.xml",
			"animations/pedestrian/observing.xml",
	};
	
	// entity data
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
		Shitted,
	}
	
	State		m_state;
	
	/**
	 * Serialization support constructor.
	 */
	public Pedestrian()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, 0.2f, 0.2f ) );	// TODO: config
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

	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	@Override
	public void onLoad( DataLoader loader ) 
	{
		m_hitWithShit = ( loader.getIntValue( "hitWithShit" ) == 1 );
		
		// deserialize visual aspect of the entity
		
		DataLoader visualNode = loader.getChild( "Visual" );
		if ( visualNode != null )
		{			
			Animation[] nodes = Animation.values();
			for( int i = 0; i < nodes.length; ++i )
			{
				DataLoader animNode = visualNode.getChild( nodes[i].m_xmlId );
				if ( animNode != null )
				{
					m_animPaths[i] = animNode.getStringValue( "path" );
				}
			}
		}
	}
	
	@Override
	public void onSave( DataSaver saver ) 
	{
		saver.setIntValue( "hitWithShit", m_hitWithShit ? 1 : 0  );
	}
	
	/**
	 * Checks if the pedestrian was hit with shit.
	 * 
	 * @return
	 */
	public boolean wasHitWithShit() 
	{
		return m_hitWithShit;
	}
}
