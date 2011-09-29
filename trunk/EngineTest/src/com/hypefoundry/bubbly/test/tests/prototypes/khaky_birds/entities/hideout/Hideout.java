/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hideout;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter.Shootable;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian.Pedestrian;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.perkPedestrian.PerkPedestrian;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class Hideout extends Entity implements NotWalkAble, Shootable
{
	
	public int						m_pedestrians 		= 0;
	public int						m_perkPedestrians	= 0;
	private World 					m_world;
	
	/**
	 * Default constructor.
	 */
	public Hideout()
	{
		this( 0, 0);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Hideout( float x, float y)
	{
		setPosition( x, y, 70 );
		
		setBoundingBox( new BoundingBox( -0.7f, -0.7f, -15f, 0.7f, 0.7f, 15f ) );	// TODO: config
		
		final float maxRotationSpeed = 0f;
		final float maxLinearSpeed = 0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
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
		m_world.addEntity( new PerkPedestrian(getPosition(), this ) );
	}
}
