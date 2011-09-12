/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter;


import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.events.CollisionEvent;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;
import com.hypefoundry.engine.world.EventFactory;
import com.hypefoundry.engine.world.World;

/**
 * @author azagor
 *
 */
public class Bullet extends Entity implements EntityEventListener
{

	public final float 	maxLinearSpeed 			 = 4.0f;
	public World 		m_world    				     = null;

	/**
	 * Default constructor.
	 */
	public Bullet()
	{
		this( 0, 0, 0 );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 */
	public Bullet( float x, float y, float facing )
	{
		setPosition( x, y, 0 );
		setFacing( facing );
		
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
		setBoundingBox( new BoundingBox( -0.05f, -0.05f, -10.0f, 0.05f, 0.05f, 10.0f ) );	// TODO: config
		
		//register events
		registerEvent( OutOfWorldBounds.class, new EventFactory< OutOfWorldBounds >() { @Override public OutOfWorldBounds createObject() { return new OutOfWorldBounds (); } } );
		registerEvent( CollisionEvent.class, new EventFactory< CollisionEvent >() { @Override public CollisionEvent createObject() { return new CollisionEvent (); } } );
	}
	
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		attachEventListener( this );
	}
	
	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof CollisionEvent )
		{
			// if it collides with another entity, it attempts eating it
			Entity collider = ( (CollisionEvent)event ).m_collider;
			collider.sendEvent( Shot.class );   
			die();
		}
		if ( event instanceof OutOfWorldBounds )
		{
			die();
		}
	}
	
	void die()
	{
		m_world.removeEntity( this );
	}
	
}
