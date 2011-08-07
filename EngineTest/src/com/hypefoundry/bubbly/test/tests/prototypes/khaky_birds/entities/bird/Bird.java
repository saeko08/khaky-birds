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
public class Bird extends Entity implements EntityEventListener
{
	private CableProvider		m_cables			 = null;
	private int					m_cableIdx  		 = 0;
	private final float 		m_dy 				 = 1;		// the distance the bird can jump up or down
	private World 				m_world    			 = null;
	public 	boolean 			crosshairInitialized = false;
	private Crap				m_crap               = null;
	
	
	
	/**
	 * Constructor.
	 */
	public Bird()
	{
		setPosition( 0, 0, 0 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, -0.1f, 0.2f, 0.2f, 0.1f ) );	// TODO: config
		
		// define events the entity responds to
		registerEvent( Eaten.class, new EventFactory< Eaten >() { @Override public Eaten createObject() { return new Eaten (); } } );
		registerEvent( Shocked.class, new EventFactory< Shocked >() { @Override public Shocked createObject() { return new Shocked (); } } );
		
		// register events listeners
		attachEventListener( this );
		
		// add movement capabilities
		final float maxLinearSpeed = 1.0f;
		final float maxRotationSpeed = 180.0f;
		defineAspect( new DynamicObject( maxLinearSpeed, maxRotationSpeed ) );
	}
	
	@Override
	public void onAddedToWorld( World hostWorld )
	{
		m_world = hostWorld;
		m_cables = (CableProvider)hostWorld.findEntity( CableProvider.class );
		
		if ( m_cables != null )
		{
			m_cableIdx = m_cables.getStartCableIdx();
			
			float y = hostWorld.getHeight() / 2;
			float x = 0;
			try
			{
				x = m_cables.getPositionOnCable( m_cableIdx, y );
			}
			catch( RuntimeException ex )
			{
				// TODO: there are no cables - fly
			}
			
			setPosition( x, y, 0 );
		}
	}
	
	@Override
	public void onRemovedFromWorld( World hostWorld ) 
	{
		m_cables = null;
	}

	/**
	 * Moves the bird to the next cable to its left. 
	 */
	public void jumpLeft() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
		Vector3 currPos = getPosition();
		
		m_cableIdx = m_cables.getLeftCable( m_cableIdx );
		float x = m_cables.getPositionOnCable( m_cableIdx, currPos.m_y );
		
		translate( x - currPos.m_x, 0, 0 );
	}

	/**
	 * Moves the bird to the next cable to its right. 
	 */
	public void jumpRight() 
	{
		if ( m_cables == null )
		{
			return;
		}
		Vector3 currPos = getPosition();
		
		m_cableIdx = m_cables.getRightCable( m_cableIdx );
		float x = m_cables.getPositionOnCable( m_cableIdx, currPos.m_y );
		
		translate( x - currPos.m_x, 0, 0 );
		
	}

	/**
	 * Moves the bird down the cable it's sitting on. 
	 */
	public void jumpDown() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
		Vector3 currPos = getPosition();
		float x = m_cables.getPositionOnCable( m_cableIdx, currPos.m_y + m_dy );
		translate( x - currPos.m_x, m_dy, 0 );
	}

	/**
	 * Moves the bird up the cable it's sitting on. 
	 */
	public void jumpUp() 
	{
		if ( m_cables == null )
		{
			return;
		}
		
		Vector3 currPos = getPosition();
		float x = m_cables.getPositionOnCable( m_cableIdx, currPos.m_y - m_dy );
		translate( x - currPos.m_x, -m_dy, 0 );
	}

	/**
	 * Initialize the crosshair on bird 
	 */
	public void crosshairOn() 
	{
		crosshairInitialized = true;
	}
	
	/**
	 * Bird starts shitting
	 */
	public void makeShit() 
	{
		m_crap = new Crap();
		crosshairInitialized = false;
		
		m_world.addEntity(m_crap);
	}

	// ------------------------------------------------------------------------
	// Environment interactions
	// ------------------------------------------------------------------------

	@Override
	public void onEvent( EntityEvent event ) 
	{
		if ( event instanceof Eaten || event instanceof Shocked )
		{
			m_world.removeEntity( this );
		}
	}

}
