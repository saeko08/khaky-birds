/**
 * 
 */
package com.hypefoundry.kabloons.utils;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.kabloons.entities.background.AnimatedBackground;
import com.hypefoundry.kabloons.entities.baloon.Baloon;
import com.hypefoundry.kabloons.entities.buzzSaw.BuzzSaw;
import com.hypefoundry.kabloons.entities.exitDoor.ExitDoor;
import com.hypefoundry.kabloons.entities.fan.Fan;


/**
 * @author Paksas
 *
 */
public class AssetsFactory 
{
	/**
	 * A factory that creates baloons.
	 * 
	 * @author Paksas
	 *
	 */
	private class BaloonFactory
	{
		String			m_floatingUpAnim;
		String			m_floatingLeftAnim;
		String			m_floatingRightAnim;
		String			m_inVortexAnim;
		String			m_deathAnim;
		BoundingBox		m_localBounds;
		
		/**
		 * Constructor.
		 * 
		 * @param loader
		 */
		BaloonFactory( DataLoader loader )
		{			
			m_localBounds = new BoundingBox();
			m_localBounds.load( "localBounds", loader );
			
			m_floatingUpAnim = loader.getStringValue( "floatingUpAnim" );
			m_floatingLeftAnim = loader.getStringValue( "floatingLeftAnim" );
			m_floatingRightAnim = loader.getStringValue( "floatingRightAnim" );
			m_inVortexAnim = loader.getStringValue( "inVortexAnim" );
			m_deathAnim = loader.getStringValue( "deathAnim" );
		}
		
		/**
		 * Instantiates a new baloon at the specified world position.
		 *
		 * @param pos
		 *  
		 * @return
		 */
		Baloon create( Vector3 pos )
		{
			Baloon baloon = new Baloon();
			baloon.initialize( m_localBounds, pos, m_floatingUpAnim, m_floatingLeftAnim, m_floatingRightAnim, m_inVortexAnim, m_deathAnim );
			return baloon;
		}
	}
	
	/**
	 * A factory that creates fans.
	 * 
	 * @author Paksas
	 *
	 */
	private class FanFactory
	{
		String			m_fanAnim;
		String			m_windFx;
		BoundingBox		m_localBounds;
		BoundingBox		m_windFieldLocalBounds;
		Vector3			m_blowForce;
		
		/**
		 * Constructor.
		 * 
		 * @param loader
		 * @param blowForce
		 */
		FanFactory( DataLoader loader, Vector3 blowForce )
		{
			m_localBounds = new BoundingBox();
			m_localBounds.load( "localBounds", loader );
			
			m_windFieldLocalBounds = new BoundingBox();
			m_windFieldLocalBounds.load( "windFieldBounds", loader );
			
			m_fanAnim = loader.getStringValue( "anim" );
			m_windFx = loader.getStringValue( "windFx" );
			
			m_blowForce = blowForce;
		}
		
		/**
		 * Initializes a fan.
		 *  
		 * @return
		 */
		void initialize( Fan fan )
		{
			fan.initialize( m_localBounds, m_windFieldLocalBounds, m_fanAnim, m_windFx, m_blowForce );
		}
	}
	
	/**
	 * Definition of exit door.
	 * 
	 * @author Paksas
	 */
	public class ExitDoorData
	{
		public String		m_openAnim;
		public BoundingBox	m_localBounds;
		
		/**
		 * Constructor.
		 * 
		 * @param loader
		 */
		ExitDoorData( DataLoader loader )
		{			
			DataLoader exitDoorNode = loader.getChild( "ExitDoor" );
			if ( exitDoorNode != null )
			{
				m_openAnim = exitDoorNode.getStringValue( "openAnim" );
				
				m_localBounds = new BoundingBox();
				m_localBounds.load( "localBounds", exitDoorNode );
			}
		}
		
		public void initialize( ExitDoor door )
		{
			door.m_openAnim = m_openAnim;
			door.setBoundingBox( m_localBounds );
		}
	}
	
	
	/**
	 * Definition of a toggle.
	 * 
	 * @author Paksas
	 */
	public class PuffData
	{
		public String			m_animPath;
		public BoundingBox		m_localBounds;
		
		/**
		 * Constructor.
		 * 
		 * @param loader
		 */
		PuffData( DataLoader loader )
		{			
			DataLoader buzzSawNode = loader.getChild( "Puff" );
			if ( buzzSawNode != null )
			{
				m_animPath = buzzSawNode.getStringValue( "anim" );
				
				m_localBounds = new BoundingBox();
				m_localBounds.load( "localBounds", buzzSawNode );
			}
		}
		
		public void initialize( AnimatedBackground entity ) 
		{
			entity.m_path = m_animPath;
			entity.setBoundingBox( m_localBounds );
		}
	}

	
	// baloon factories
	private BaloonFactory[]		m_baloonTypes;
	private FanFactory[]		m_fanTypes = new FanFactory[Fan.Direction.values().length];
	private ExitDoorData 		m_exitDoorDefinition;
	private PuffData			m_puffData;
	
	// ------------------------------------------------------------------------
	// API
	// ------------------------------------------------------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param loader
	 */
	public AssetsFactory( DataLoader loader ) 
	{
		// load definitions of the baloons
		{
			int baloonTypesCount = loader.getChildrenCount( "Baloon" );
			m_baloonTypes = new BaloonFactory[baloonTypesCount];
			
			int i = 0;
			for ( DataLoader baloonTypeNode = loader.getChild( "Baloon" ); baloonTypeNode != null; baloonTypeNode = baloonTypeNode.getSibling(), ++i )
			{
				m_baloonTypes[i] = new BaloonFactory( baloonTypeNode );
			}
		}
		
		// load fan definitions
		{
			for ( DataLoader fanTypeNode = loader.getChild( "Fan" ); fanTypeNode != null; fanTypeNode = fanTypeNode.getSibling() )
			{
				Fan.Direction direction = loadFanDirection( fanTypeNode );
				Vector3 blowForce = new Vector3();
				blowForce.load( "BlowForce", fanTypeNode );
				blowForce.m_z = 0;
				
				m_fanTypes[ direction.m_idx ] = new FanFactory( fanTypeNode, blowForce );
			}
		}
		
		// devices
		m_exitDoorDefinition = new ExitDoorData( loader );
		m_puffData = new PuffData( loader );
	}
	
	/**
	 * Creates a baloon selected at random and places it at the specified position.
	 * 
	 * @param pos
	 * @return
	 */
	public Baloon createRandomBaloon( Vector3 pos )
	{
		int baloonType = (int)( Math.random() * m_baloonTypes.length );
		pos.m_z = 20;
		return m_baloonTypes[baloonType].create( pos );
	}
	
	/**
	 * Parses the direction of the fan from an XML doc.
	 * 
	 * @param loader
	 * @return
	 */
	public static Fan.Direction loadFanDirection( DataLoader loader )
	{
		String directionStr = loader.getStringValue( "direction" );
		Fan.Direction direction = Fan.Direction.valueOf( directionStr );
		return direction;
	}
	
	/**
	 * Initializes a fan to blow in the speicfied direction.
	 * 
	 * @param pos
	 * @param direction
	 */
	public void initializeFan( Fan fan, Fan.Direction direction )
	{
		m_fanTypes[ direction.m_idx ].initialize( fan );
	}

	/**
	 * Initializes an exit door instance.
	 * 
	 * @param exitDoor
	 */
	public void initializeDoor( ExitDoor exitDoor ) 
	{
		m_exitDoorDefinition.initialize( exitDoor );
	}

	/**
	 * Initializes a puff effect that appears when we add or remove a fan.
	 * 
	 * @param effect
	 * @param position
	 */
	public void initializePuff( AnimatedBackground effect, Vector3 position ) 
	{
		m_puffData.initialize( effect );
		effect.setPosition( position.m_x, position.m_y, 30.0f );
	}
}
