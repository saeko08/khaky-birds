package com.hypefoundry.khakyBirds.entities.bird;

import java.util.Random;

import com.hypefoundry.khakyBirds.entities.crap.Crap;
import com.hypefoundry.khakyBirds.entities.crap.DemolisheCrap;
import com.hypefoundry.khakyBirds.entities.crap.GranadeCrap;
import com.hypefoundry.khakyBirds.entities.hunter.Shootable;
import com.hypefoundry.engine.world.Entity;
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
public class Bird extends Entity implements Shootable
{
	public CableProvider		m_cables			= null;
	public int					m_cableIdx  		= 0;
	public World 				m_world    			= null;
	public int					m_maxSpecialCraps	= 2;
	boolean						m_canCrap			= true;
	boolean						m_paused			= false;
	
	private Vector3 			m_tmpCrapPos 		= new Vector3();
	
	
	public enum State
	{
		Paused,
		Idle,
		Jumping,
		Flying,
		Landing,
	}
	
	public State				m_state;
	
//////////////////////////////////////////////////////////////////////////////////////////////////////	
	public enum CrapType
	{
		SpecialGranadeCrap,
		SpecialDemolishCrap
	};
	
	public CrapType			m_crapType;
	private Random 			m_randCrapType  			= new Random();
	private int				m_specialCrapTypeAmount		= 2;
	public int				m_currentSpecialCrapAmount	= 0;
///////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Constructor.
	 */
	public Bird()
	{
		setPosition( 0, 0, 0 );
		setBoundingBox( new BoundingBox( -0.2f, -0.2f, 0.2f, 0.2f ) );	// TODO: config
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
	
	/**
	 * Bird makes a crap.
	 */
	public void makeSpecialShit() 
	{
		Vector3 birdPos = getPosition();
		
		m_tmpCrapPos.set(Vector3.EX).rotateZ( getFacing() ).scale(-0.3f).add( birdPos );		
		
		
		switch( m_crapType )
		{
			case SpecialGranadeCrap:
			{
				m_world.addEntity( new GranadeCrap( m_tmpCrapPos.m_x, m_tmpCrapPos.m_y ) );
				m_currentSpecialCrapAmount -= 1;
				break;
			}
			
			case SpecialDemolishCrap:
			{
				m_world.addEntity( new DemolisheCrap( m_tmpCrapPos.m_x, m_tmpCrapPos.m_y ) );
				m_currentSpecialCrapAmount -= 1;
				break;
			}
	
		}
	}
	
	/**
	 * Bird gets special crap.
	 */
	public void setSpecialCrap()
	{
		int m_crapTypeNumber = 0;
		
		m_crapTypeNumber = m_randCrapType.nextInt(m_specialCrapTypeAmount);
		
		
		if (m_crapTypeNumber == 0)
		{
			m_crapType = CrapType.SpecialDemolishCrap;
		}
		else
		{
			m_crapType = CrapType.SpecialGranadeCrap;
		}
		
		//restoring maximum amount of special craps
		m_currentSpecialCrapAmount = m_maxSpecialCraps;
		
	}
	
	
	/**
	 * Enables/disables the ability to crap.
	 * @param enable
	 */
	public void enableCrapping( boolean enable ) 
	{
		m_canCrap = enable;
	}
	
	public void pause( boolean enable ) 
	{
		m_paused = enable;
	}
}
