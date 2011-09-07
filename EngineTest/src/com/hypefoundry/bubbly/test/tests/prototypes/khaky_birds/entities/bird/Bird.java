package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.bird;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crap;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.falcon.Eaten;
import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.shock.Shocked;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;


/**
 * The game's main character. A nasty bird that jumps on the cables
 * trying to avoid electrocution and make the lives of the pedestrians
 * as miserable as possible.
 * 
 * @author paksas
 *
 */
public class Bird extends Entity
{
	public CableProvider		m_cables			= null;
	public int					m_cableIdx  		= 0;
	public World 				m_world    			= null;
	
	private Vector3 			m_tmpCrapPos 		= new Vector3();
	
	public enum State
	{
		Idle,
		Jumping,
		Shitting,
		Flying,
		Landing,
	};
	
	public State				m_state;
	
	/**
	 * Constructor.
	 */
	public Bird()
	{
		setPosition( 0, 0, 0 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		m_state = State.Flying; 
					
		// add movement capabilities
		final float maxLinearSpeed = 3.0f;
		final float maxRotationSpeed = 720.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{		
		m_world = hostWorld;
		m_cables = (CableProvider)hostWorld.findEntity( CableProvider.class );
		setPosition( m_world.getWidth() / 2, m_world.getHeight() / 2, 0 );
	}
	
	@Override
	public void onRemovedFromWorld( World hostWorld ) 
	{
		m_cables = null;
	}
	
	/**
	 * Bird makes a crap.
	 */
	public void makeShit() 
	{
		Vector3 birdPos = getPosition();
		
		m_tmpCrapPos.set(Vector3.EX).rotateZ( getFacing() ).scale(-0.3f).add( birdPos );		
		m_world.addEntity( new Crap( m_tmpCrapPos.m_x, m_tmpCrapPos.m_y ) );
	}
}
