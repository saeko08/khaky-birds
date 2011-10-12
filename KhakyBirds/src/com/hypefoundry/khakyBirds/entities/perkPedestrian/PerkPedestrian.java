/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.perkPedestrian;

import com.hypefoundry.khakyBirds.entities.crap.Crappable;
import com.hypefoundry.khakyBirds.entities.hideout.Hideout;
import com.hypefoundry.khakyBirds.entities.hunter.Bullet;
import com.hypefoundry.khakyBirds.entities.zombie.Biteable;
import com.hypefoundry.khakyBirds.entities.zombie.Zombie;
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
public class PerkPedestrian  extends Entity implements Crappable, Biteable
{
	boolean				m_hitWithShit					= false;
	public World 		m_world    				    	= null;
	boolean 			m_wasBeaten 				 	= false;
	private Vector3 	m_tmpBulletPos 					= new Vector3();
	public Hideout		m_hideout						= null;
	
	enum State
	{
		Wander,
		TurnAround,
		Observe,
		Aiming,
		Shooting,
		Eaten,
		Avoid,
		Hiding,
		Shitted
	}
	
	State		m_state;
	
	/**
	 * Serialization support constructor.
	 */
	public PerkPedestrian()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		setPosition( 0, 0, 80 );
		
		// add movement capabilities
		final float maxLinearSpeed = 1.3f;
		final float maxRotationSpeed = 195.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	/**
	 * Constructor.
	 *
	 * @param Vector3
	 *
	 */
	public PerkPedestrian( Vector3 spawnPos, Hideout home )
	{
		// call the default constructor first to perform the generic initialization
		this();
		
		setPosition( spawnPos.m_x, spawnPos.m_y, 80 );
		
		m_hideout = home;
		
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
			Vector3 perkPedestrianPos = getPosition();
			m_world.addEntity( new Zombie(perkPedestrianPos));
		}
	}
	
	/**
	 *PerkPedestrian make a  Shoot
	 */
	public void shoot() 
	{
		Vector3 hunterPos = getPosition();
		
		m_tmpBulletPos.set(Vector3.EX).rotateZ( getFacing() ).scale(0.3f).add( hunterPos );		
		m_world.addEntity( new Bullet( m_tmpBulletPos.m_x, m_tmpBulletPos.m_y, getFacing(), Zombie.class ) );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		m_hideout.m_perkPedestrians += 1;
	}
	@Override
	public void onRemovedFromWorld( World hostWorld )
	{
		m_hideout.m_perkPedestrians -= 1;
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


