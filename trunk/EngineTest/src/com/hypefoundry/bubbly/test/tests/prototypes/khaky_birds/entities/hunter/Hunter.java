/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crap;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crappable;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.util.serialization.DataSaver;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * Hunter that tries to shoot the bird down.
 * 
 * @author azagor
 *
 */
public class Hunter extends Entity  implements Crappable
{
	
	private Vector3 			m_tmpBulletPos 		= new Vector3();
	public World 				m_world    			= null;
	
	enum State
	{
		Aiming,
		Shooting,
		Shitted
	}
	
	State		m_state;
	
	/**
	 * Serialization support constructor.
	 */
	public Hunter()
	{		
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		setPosition( 0, 0, 80 );
		
		// add movement capabilities
		final float maxLinearSpeed = 0.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
		m_state = State.Aiming;

	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{		
		m_world = hostWorld;
	}
	
	/**
	 *Hunter make a  Shoot
	 */
	public void shoot() 
	{
		Vector3 hunterPos = getPosition();
		//float m_facing = getFacing();
		
		m_tmpBulletPos.set(Vector3.EX).rotateZ( getFacing() ).scale(0.3f).add( hunterPos );		
		m_world.addEntity( new Bullet( m_tmpBulletPos.m_x, m_tmpBulletPos.m_y, getFacing() ) );
	}
	
	@Override
	public void onLoad( DataLoader loader ) 
	{
		String stateStr = loader.getStringValue( "state" );
		
		try
		{
			m_state = State.valueOf( State.class, stateStr );
		}
		catch( Exception ex )
		{
			m_state = State.Aiming;
		}
	}
	
	@Override
	public void onSave( DataSaver saver ) 
	{
		saver.setStringValue( "state", m_state.toString() );
	}
}
