/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hideout;

import com.hypefoundry.khakyBirds.entities.crap.Crappable;
import com.hypefoundry.khakyBirds.entities.hunter.Shootable;
import com.hypefoundry.khakyBirds.entities.pedestrian.Pedestrian;
import com.hypefoundry.khakyBirds.entities.perkPedestrian.PerkPedestrian;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class Hideout extends Entity implements NotWalkAble, Shootable, Crappable
{
	
	public int						m_pedestrians 				= 0;
	public int						m_perkPedestrians			= 0;
	public int						m_maxPerkPedestrianNumber	= 0;
	private World 					m_world;
	public boolean					m_isDemolished				= false;
	
	float							m_defaultPedestrianRespawnTime 		= 10.0f;
	float							m_defaultPerkPedestrianRespawnTime 	= 10.0f;
	float							m_bombedPedestrianRespawnTime 		= 0.5f;
	float							m_bombedPerkPedestrianRespawnTime 	= 0.5f;
	float 							m_panicTime							= 4.0f;
	
	public enum State
	{
		Default,
		Bombed
	}
	
	State				m_state;
	
	/////////////////////////////////////////////////////////////
	/**
	 * Default constructor.
	 */
	public Hideout()
	{
		setBoundingBox( new BoundingBox( -0.7f, -0.7f, 0.7f, 0.7f ) );	// TODO: config
		setPosition( 0, 0, 70 );
		
		final float maxRotationSpeed = 0.0f;
		final float maxLinearSpeed = 0.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		m_state = State.Default;

	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Hideout( float x, float y)
	{
		// call the default constructor first to perform the generic initialization
		this();
				
		setPosition( x, y, 70 );
		m_isDemolished		= false;
		
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
	}
	//debug
	@Override
	public void onRemovedFromWorld( World hostWorld )
	{
		m_world = hostWorld;
	}
	
	public void goOut()
	{
		m_world.addEntity( new Pedestrian(getPosition() ) );
	}

	public void perkPedestrianGoOut()
	{
		//tu trzeba bêdzie przekazywaæ instancjê hideouta
		m_world.addEntity( new PerkPedestrian( getPosition(), this ) );
	}
	
	public void bombed()
	{
		m_isDemolished		= true;
	}
	
	// ------------------------------------------------------------------------
	// Serialization support
	// ------------------------------------------------------------------------
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		DataLoader pedestrianLoader = loader.getChild( "Pedestrian" );
		if ( pedestrianLoader != null )
		{
			m_defaultPedestrianRespawnTime 			= pedestrianLoader.getFloatValue("defaultRespawnTime");
			m_bombedPedestrianRespawnTime 			= pedestrianLoader.getFloatValue("bombedRespawnTime");
		}
		
		DataLoader perkPedestrianLoader = loader.getChild( "PerkPedestrian" );
		if ( perkPedestrianLoader != null )
		{
			m_defaultPedestrianRespawnTime 			= perkPedestrianLoader.getFloatValue("defaultRespawnTime");
			m_bombedPedestrianRespawnTime 			= perkPedestrianLoader.getFloatValue("bombedRespawnTime");
		}
		
		m_panicTime = loader.getFloatValue( "panicTime" );	
		m_maxPerkPedestrianNumber = loader.getIntValue( "maxPerkPedestrians" );
	}
}
