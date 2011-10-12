/**
 * 
 */
package com.hypefoundry.khakyBirds.entities.hunter;


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

	public final float 		maxLinearSpeed 			 	= 1.0f;
	public World 			m_world						= null;
	private Class			m_shootableClass;
	
	
	/**
	 * Default constructor.
	 */
	public Bullet()
	{
		this( 0, 0, 0, null );
	}
	
	/**
	 * Constructor.
	 * 
	 * @param x
	 * @param y
	 * @param facing
	 * @param shootable
	 */
	public Bullet( float x, float y, float facing, Class shootable )
	{
		setPosition( x, y, 50 );
		setFacing( facing );
		
		setBoundingBox( new BoundingBox( -0.05f, -0.05f, -100.0f, 0.05f, 0.05f, 100.0f ) );	// TODO: config
		
		final float maxRotationSpeed = 180.0f;
		DynamicObject dynObj = new DynamicObject( maxLinearSpeed, maxRotationSpeed );
		defineAspect( dynObj );
		
		m_shootableClass = shootable;
		
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
			if ( m_shootableClass != null && m_shootableClass.isInstance( collider ) )
			{
				collider.sendEvent( Shot.class );   
				die();
			}
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
