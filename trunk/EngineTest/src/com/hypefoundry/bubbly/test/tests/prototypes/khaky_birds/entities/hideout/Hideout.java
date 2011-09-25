/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hideout;

import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.hunter.Shootable;
import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.world.Entity;

/**
 * @author azagor
 *
 */
public class Hideout extends Entity implements NotWalkAble, Shootable
{

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
	 * @param facing
	 */
	public Hideout( float x, float y)
	{
		setPosition( x, y, 70 );
		
		setBoundingBox( new BoundingBox( -0.7f, -0.7f, -15f, 0.7f, 0.7f, 15f ) );	// TODO: config
		
		final float maxRotationSpeed = 0f;
		final float maxLinearSpeed = 0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
		
	}
	

}
