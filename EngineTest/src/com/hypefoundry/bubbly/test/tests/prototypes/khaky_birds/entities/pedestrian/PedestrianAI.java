/**
 * 
 */
package com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.pedestrian;


import com.hypefoundry.bubbly.test.tests.prototypes.khaky_birds.entities.crap.Crapped;
import com.hypefoundry.engine.controllers.fsm.FiniteStateMachine;
import com.hypefoundry.engine.controllers.fsm.FSMState;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.physics.DynamicObject;
import com.hypefoundry.engine.physics.SteeringBehaviors;
import com.hypefoundry.engine.physics.events.OutOfWorldBounds;
import com.hypefoundry.engine.world.Entity;
import com.hypefoundry.engine.world.EntityEvent;
import com.hypefoundry.engine.world.EntityEventListener;


/**
 * AI of a pedestrian.
 * 
 * @author paksas
 *
 */
public class PedestrianAI extends FiniteStateMachine
{
	private Pedestrian			m_pedestrian;
	private SteeringBehaviors 	m_sb;

	// ------------------------------------------------------------------------
	
	class Wander extends FSMState implements EntityEventListener
	{	
		private Vector3 m_tmpDirVec 		= new Vector3();
		
		Wander()
		{
			// register events listeners
			m_pedestrian.attachEventListener( this );
		}
		
		@Override
		public void activate()
		{
			m_sb.reset().wander().faceMovementDirection();
		}
		
		@Override
		public void deactivate()
		{
			m_sb.reset();
		}

		@Override
		public void onEvent( EntityEvent event ) 
		{
			if ( event instanceof Crapped )
			{
				m_pedestrian.setHitWithShit( true );
				transitTo( Observe.class );
			}
			else if ( event instanceof OutOfWorldBounds )
			{
				TurnBack state = transitTo( TurnBack.class );
						
				OutOfWorldBounds oowb = (OutOfWorldBounds)event;
				
				DynamicObject dynObject = m_pedestrian.query( DynamicObject.class );
				dynObject.m_velocity.normalized2D( m_tmpDirVec );
				oowb.reflectVector( state.m_targetDirection, m_tmpDirVec );				
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class TurnBack extends FSMState
	{
		public Vector3 m_targetDirection 	= new Vector3();
		public Vector3 m_tmpDirVec 			= new Vector3();
		
		@Override
		public void activate()
		{
			m_sb.reset().lookAt( m_targetDirection );
		}
		
		@Override
		public void deactivate()
		{
			m_sb.reset();
		}
		
		@Override
		public void execute( float deltaTime )
		{	
			m_tmpDirVec.set( 1.0f, 0.0f, 0.0f ).rotateZ( m_pedestrian.getFacing() );
			float angleDist = m_targetDirection.getAngleBetween( m_tmpDirVec );
			if ( angleDist < 5.0f )
			{
				// when we're close to the desired facing, switch to wandering state
				transitTo( Wander.class );
			}
		}
	}
	
	// ------------------------------------------------------------------------
	
	class Observe extends FSMState
	{
		private float 				m_wait 			= 0.f;
		
		@Override
		public void activate()
		{
			m_wait = 10.0f;
		}
		
		@Override
		public void execute( float deltaTime )
		{
			m_wait -= deltaTime;
			if ( m_wait > 0 )
			{
				transitTo( Wander.class );
			}				
		}
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param world
	 * @param pedestrian			controlled pedestrian
	 */
	public PedestrianAI( Entity pedestrian )
	{
		super( pedestrian );
		
		m_pedestrian = (Pedestrian)pedestrian;
		m_sb = new SteeringBehaviors( m_pedestrian );

		// setup the state machine
		register( new Wander() );
		register( new TurnBack() );
		register( new Observe() );
		begin( Wander.class );
	}
	
	@Override
	public void onUpdate( float deltaTime )
	{
		m_sb.update();
	}
}
