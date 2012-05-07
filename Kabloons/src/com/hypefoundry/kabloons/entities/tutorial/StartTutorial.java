/**
 * 
 */
package com.hypefoundry.kabloons.entities.tutorial;

import com.hypefoundry.engine.math.BoundingBox;
import com.hypefoundry.engine.math.Vector3;
import com.hypefoundry.engine.util.serialization.DataLoader;
import com.hypefoundry.engine.world.Entity;

/**
 * @author Paksas
 *
 */
public class StartTutorial extends Entity 
{
	enum State
	{
		PLACE_FIRST_FAN,
		REMOVE_FAN,
		PLACE_SECOND_FAN,
		RELEASE_GHOST,
		NOTHING
	}

	public String[]			m_stateImagePaths 		= new String[State.values().length];
	public String			m_fingerImage;
	public Vector3[]		m_stateImagePos 		= new Vector3[State.values().length];
	public BoundingBox[]	m_stateImageBounds 		= new BoundingBox[State.values().length];
	public State			m_state 				= State.NOTHING;

	// runtime data
	public Vector3			m_gesturePos 			= new Vector3();

	@Override
	public void onLoad( DataLoader loader )
	{
		State[] stateValues = State.values();
		for ( int i = 0; i < stateValues.length; ++i )
		{
			DataLoader stateNode = loader.getChild( stateValues[i].name() );
			if ( stateNode != null )
			{
				m_stateImagePaths[i] = stateNode.getStringValue( "path" );
				
				m_stateImagePos[i] = new Vector3();
				m_stateImagePos[i].load( "position", stateNode );
				
				m_stateImageBounds[i] = new BoundingBox();
				m_stateImageBounds[i].load( "bounds", stateNode );
			}
		}
		
		m_fingerImage = loader.getStringValue( "fingerImgPath" );
	}
}
